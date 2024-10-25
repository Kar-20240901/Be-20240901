package com.kar20240901.be.base.web.exception;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.kar20240901.be.base.web.model.domain.request.BaseRequestDO;
import com.kar20240901.be.base.web.model.domain.request.BaseRequestInfoDO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.util.base.IdGeneratorUtil;
import com.kar20240901.be.base.web.util.base.Ip2RegionUtil;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import com.kar20240901.be.base.web.util.base.MyExceptionUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.base.RequestUtil;
import io.swagger.v3.oas.annotations.Operation;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @Resource
    HttpServletRequest httpServletRequest;

    /**
     * 参数校验异常：@Valid
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R<?> handleValidException(MethodArgumentNotValidException e) {

        MyExceptionUtil.printError(e);

        // 返回详细的参数校验错误信息
        Map<String, String> map = MapUtil.newHashMap(e.getBindingResult().getFieldErrors().size());

        BindingResult bindingResult = e.getBindingResult();

        for (FieldError item : bindingResult.getFieldErrors()) {
            map.put(item.getField(), item.getDefaultMessage());
        }

        R<Map<String, String>> r = R.errorOrigin(TempBizCodeEnum.PARAMETER_CHECK_ERROR, map);

        Method method = e.getParameter().getMethod();

        if (method != null) {

            // 处理：请求
            handleRequest(httpServletRequest, method.getAnnotation(Operation.class),
                ExceptionUtil.stacktraceToString(e), JSONUtil.toJsonStr(e.getBindingResult().getTarget()),
                JSONUtil.toJsonStr(r));

        }

        return r;

    }

    /**
     * 参数校验异常：断言
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    public R<?> handleIllegalArgumentException(IllegalArgumentException e) {

        MyExceptionUtil.printError(e);

        return R.errorMsgOrigin(e.getMessage());

    }

    /**
     * 参数校验异常：springframework
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public R<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {

        MyExceptionUtil.printError(e);

        R<String> r = R.errorOrigin(TempBizCodeEnum.PARAMETER_CHECK_ERROR, e.getMessage());

        // 处理：请求
        handleRequest(httpServletRequest, null, ExceptionUtil.stacktraceToString(e), "", JSONUtil.toJsonStr(r));

        return r;

    }

    /**
     * 自定义异常
     */
    @ExceptionHandler(value = TempException.class)
    public R<?> handleBaseException(TempException e) {

        MyExceptionUtil.printError(e);

        return e.getR();

    }

    /**
     * 权限不够时的异常处理
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public R<?> handleAccessDeniedException(AccessDeniedException e) {

        Long currentUserIdDefault = MyUserUtil.getCurrentUserIdDefault();

        log.info("权限不足：{}，uri：{}", currentUserIdDefault, httpServletRequest.getRequestURI());

        R<String> r = R.errorOrigin(TempBizCodeEnum.INSUFFICIENT_PERMISSIONS);

        // 处理：请求
        handleRequest(httpServletRequest, null, ExceptionUtil.stacktraceToString(e), "", JSONUtil.toJsonStr(r));

        return r;

    }

    /**
     * 不记录日志的异常
     */
    @ExceptionHandler(value = NoLogException.class)
    public R<?> handleNoLogException(NoLogException e) {

        return R.errorOrigin(TempBizCodeEnum.RESULT_SYS_ERROR);

    }

    /**
     * 缺省异常处理，直接提示系统异常
     */
    @ExceptionHandler(value = Throwable.class)
    public R<?> handleThrowable(Throwable e) {

        MyExceptionUtil.printError(e);

        R<String> r = R.errorOrigin(TempBizCodeEnum.RESULT_SYS_ERROR);

        // 处理：请求
        handleRequest(httpServletRequest, null, ExceptionUtil.stacktraceToString(e), "", JSONUtil.toJsonStr(r));

        return r;

    }

    /**
     * 处理：请求
     */
    public static void handleRequest(HttpServletRequest httpServletRequest, @Nullable Operation operation,
        String errorMsg, String requestParam, String responseValue) {

        Date date = new Date();

        Long currentUserIdDefault = MyUserUtil.getCurrentUserIdDefault();

        String uri = httpServletRequest.getRequestURI();

        BaseRequestDO baseRequestDO = new BaseRequestDO();

        BaseRequestInfoDO baseRequestInfoDO = new BaseRequestInfoDO();

        Long id = IdGeneratorUtil.nextId();

        baseRequestDO.setId(id);
        baseRequestInfoDO.setId(id);

        baseRequestDO.setUri(uri);
        baseRequestDO.setCostMs(0L);
        baseRequestDO.setName(operation == null ? "" : operation.summary());

        baseRequestDO.setCategory(RequestUtil.getRequestCategoryEnum(httpServletRequest));
        baseRequestDO.setIp(ServletUtil.getClientIP(httpServletRequest));
        baseRequestDO.setRegion(Ip2RegionUtil.getRegion(baseRequestDO.getIp()));

        baseRequestDO.setSuccessFlag(false);

        // 设置：类型
        baseRequestDO.setType(operation == null ? "" : MyEntityUtil.getNotNullAndTrimStr(operation.description()));

        baseRequestDO.setCreateId(currentUserIdDefault);
        baseRequestDO.setCreateTime(date);

        baseRequestInfoDO.setErrorMsg(MyEntityUtil.getNotNullStr(errorMsg));
        baseRequestInfoDO.setRequestParam(MyEntityUtil.getNotNullStr(requestParam));
        baseRequestInfoDO.setResponseValue(MyEntityUtil.getNotNullStr(responseValue));

        // 添加一个：请求数据
        RequestUtil.add(baseRequestDO, baseRequestInfoDO);

    }

}
