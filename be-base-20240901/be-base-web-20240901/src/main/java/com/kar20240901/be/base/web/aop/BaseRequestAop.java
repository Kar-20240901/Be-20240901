package com.kar20240901.be.base.web.aop;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import com.kar20240901.be.base.web.exception.NoLogException;
import com.kar20240901.be.base.web.exception.TempException;
import com.kar20240901.be.base.web.model.constant.base.OperationDescriptionConstant;
import com.kar20240901.be.base.web.model.constant.base.SecurityConstant;
import com.kar20240901.be.base.web.model.domain.request.BaseRequestDO;
import com.kar20240901.be.base.web.model.domain.request.BaseRequestInfoDO;
import com.kar20240901.be.base.web.model.enums.base.BaseRequestMethodEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.base.SignInVO;
import com.kar20240901.be.base.web.util.base.IdGeneratorUtil;
import com.kar20240901.be.base.web.util.base.Ip2RegionUtil;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import com.kar20240901.be.base.web.util.base.MyExceptionUtil;
import com.kar20240901.be.base.web.util.base.MyJwtUtil;
import com.kar20240901.be.base.web.util.base.MyUserInfoUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.base.RequestUtil;
import com.kar20240901.be.base.web.util.base.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class BaseRequestAop {

    /**
     * 切入点
     */
    @Pointcut("@annotation(io.swagger.v3.oas.annotations.Operation)")
    public void pointcut() {
    }

    @Around("pointcut() && @annotation(operation)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, Operation operation) throws Throwable {

        HttpServletRequest httpServletRequest = RequestUtil.getRequest();

        // 目的：因为 socket也会走这里，但是 socket没有 httpServletRequest对象
        if (httpServletRequest == null) {

            if (((MethodSignature)proceedingJoinPoint.getSignature()).getReturnType() == void.class) {

                proceedingJoinPoint.proceed();

                return null;

            } else {

                return proceedingJoinPoint.proceed();

            }

        }

        long costMs = System.currentTimeMillis();

        String uri = httpServletRequest.getRequestURI();

        Date date = new Date();

        BaseRequestDO baseRequestDO = new BaseRequestDO();

        BaseRequestInfoDO baseRequestInfoDO = new BaseRequestInfoDO();

        Long id = IdGeneratorUtil.nextId();

        baseRequestDO.setId(id);
        baseRequestInfoDO.setId(id);

        baseRequestDO.setUri(uri);
        baseRequestDO.setMethod(BaseRequestMethodEnum.getByCode(httpServletRequest.getMethod()));
        baseRequestDO.setCostMs(0L);
        baseRequestDO.setName(operation.summary());

        Long currentUserIdDefault = MyUserUtil.getCurrentUserIdDefault();

        baseRequestDO.setCreateId(currentUserIdDefault);
        baseRequestDO.setCreateTime(date);

        // 设置：类型
        baseRequestDO.setType(MyEntityUtil.getNotNullAndTrimStr(operation.description()));

        baseRequestDO.setCategory(RequestUtil.getRequestCategoryEnum(httpServletRequest));

        baseRequestDO.setIp(ServletUtil.getClientIP(httpServletRequest));

        baseRequestDO.setRegion(Ip2RegionUtil.getRegion(baseRequestDO.getIp()));

        // 更新：用户信息
        MyUserInfoUtil.add(currentUserIdDefault, date, baseRequestDO.getIp(), baseRequestDO.getRegion());

        baseRequestDO.setSuccessFlag(true);

        baseRequestInfoDO.setErrorMsg("");

        baseRequestInfoDO.setResponseValue("");

        baseRequestInfoDO.setRequestHeader(JSONUtil.toJsonStr(ServletUtil.getHeaderMap(httpServletRequest)));

        baseRequestInfoDO.setResponseHeader("");

        JSONObject jsonObject = JSONUtil.createObj();

        for (Object item : proceedingJoinPoint.getArgs()) {

            jsonObject.set(item.getClass().getSimpleName(), JSONUtil.toJsonStr(item));

        }

        baseRequestInfoDO.setRequestParam(jsonObject.toString());

        Object object = null;

        try {

            if (((MethodSignature)proceedingJoinPoint.getSignature()).getReturnType() == void.class) {

                proceedingJoinPoint.proceed(); // 执行方法，备注：如果执行方法时抛出了异常，catch可以捕获到

            } else {

                object = proceedingJoinPoint.proceed(); // 执行方法，备注：如果执行方法时抛出了异常，catch可以捕获到

                baseRequestInfoDO.setResponseValue(MyEntityUtil.getNotNullStr(JSONUtil.toJsonStr(object)));

            }

            setResponseHeader(baseRequestInfoDO); // 设置：响应头

        } catch (Throwable e) {

            if (e instanceof TempException) {

                baseRequestInfoDO.setResponseValue(JSONUtil.toJsonStr(((TempException)e).getR()));

                handleThrowable(baseRequestDO, e, costMs, baseRequestInfoDO); // 处理：异常

                throw e;

            }

            if (e instanceof IllegalArgumentException) { // 参数校验异常：断言

                baseRequestInfoDO.setResponseValue(JSONUtil.toJsonStr(R.errorMsgOrigin(e.getMessage())));

                handleThrowable(baseRequestDO, e, costMs, baseRequestInfoDO); // 处理：异常

                throw e;

            }

            if (e instanceof UndeclaredThrowableException) {

                e = ((UndeclaredThrowableException)e).getUndeclaredThrowable();

            }

            MyExceptionUtil.printError(e, "，uri：" + uri);

            handleThrowable(baseRequestDO, e, costMs, baseRequestInfoDO); // 处理：异常

            throw new NoLogException();

        }

        // 处理：登录相关请求
        handleSignIn(baseRequestDO, object);

        // 处理：耗时相关
        handleCostMs(costMs, baseRequestDO);

        log.info("uri：{}，耗时：{}ms", baseRequestDO.getUri(), baseRequestDO.getCostMs());

        // 添加一个：请求数据
        RequestUtil.add(baseRequestDO, baseRequestInfoDO);

        return object;

    }

    /**
     * 设置：响应头
     */
    public static void setResponseHeader(BaseRequestInfoDO baseRequestInfoDO) {

        HttpServletResponse httpServletResponse = ResponseUtil.getResponse();

        if (httpServletResponse == null) {
            return;
        }

        httpServletResponse.setHeader(SecurityConstant.BE_REQUEST_ID, baseRequestInfoDO.getId().toString());

        baseRequestInfoDO.setResponseHeader(JSONUtil.toJsonStr(ResponseUtil.getHeaderMap(httpServletResponse)));

    }

    /**
     * 处理：耗时相关
     */
    private void handleCostMs(long costMs, BaseRequestDO baseRequestDO) {

        costMs = System.currentTimeMillis() - costMs; // 耗时（毫秒）

        baseRequestDO.setCostMs(costMs);

    }

    /**
     * 处理：异常
     */
    private void handleThrowable(BaseRequestDO baseRequestDO, Throwable e, long costMs,
        BaseRequestInfoDO baseRequestInfoDO) {

        baseRequestDO.setSuccessFlag(false); // 设置：请求失败

        String errorMsg = ExceptionUtil.stacktraceToString(e);

        baseRequestInfoDO.setErrorMsg(errorMsg);

        setResponseHeader(baseRequestInfoDO); // 设置：响应头

        // 处理：耗时相关
        handleCostMs(costMs, baseRequestDO);

        // 添加一个：请求数据
        RequestUtil.add(baseRequestDO, baseRequestInfoDO);

    }

    /**
     * 处理：登录相关请求
     */
    private void handleSignIn(BaseRequestDO baseRequestDO, Object object) {

        if (BooleanUtil.isFalse(OperationDescriptionConstant.SIGN_IN.equals(baseRequestDO.getType()))) {
            return;
        }

        // 登录时需要额外处理来获取 用户id
        R<SignInVO> r = (R<SignInVO>)object;

        if (r.getData() == null || StrUtil.isBlank(r.getData().getJwt())) {
            return;
        }

        JWT jwt = JWT.of(MyJwtUtil.getJwtStrByHeadAuthorization(r.getData().getJwt()));

        // 获取：userId的值
        Long userId = MyJwtUtil.getPayloadMapUserIdValue(jwt.getPayload().getClaimsJson());

        baseRequestDO.setCreateId(userId);

    }

}
