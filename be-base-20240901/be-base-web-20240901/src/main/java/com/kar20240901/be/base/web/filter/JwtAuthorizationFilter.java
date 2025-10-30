package com.kar20240901.be.base.web.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.configuration.base.BaseConfiguration;
import com.kar20240901.be.base.web.configuration.security.SecurityConfiguration;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.base.BaseApiTokenMapper;
import com.kar20240901.be.base.web.model.bo.base.BaseAuthorizationBO;
import com.kar20240901.be.base.web.model.configuration.base.IJwtGenerateConfiguration;
import com.kar20240901.be.base.web.model.domain.base.BaseApiTokenDO;
import com.kar20240901.be.base.web.model.interfaces.base.IBizCode;
import com.kar20240901.be.base.web.model.interfaces.base.IJwtFilterHandler;
import com.kar20240901.be.base.web.model.vo.base.SignInVO;
import com.kar20240901.be.base.web.util.base.BaseJwtUtil;
import com.kar20240901.be.base.web.util.base.MyExceptionUtil;
import com.kar20240901.be.base.web.util.base.MyJwtUtil;
import com.kar20240901.be.base.web.util.base.MyThreadUtil;
import com.kar20240901.be.base.web.util.base.RequestUtil;
import com.kar20240901.be.base.web.util.base.ResponseUtil;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 自定义 jwt过滤器，备注：后续接口方法，无需判断账号是否封禁或者不存在
 */
@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Resource
    IJwtGenerateConfiguration iJwtGenerateConfiguration;

    @Autowired(required = false)
    @Nullable
    List<IJwtFilterHandler> iJwtFilterHandlerList;

    @Resource
    RedissonClient redissonClient;

    @Resource
    BaseApiTokenMapper baseApiTokenMapper;

    @SneakyThrows
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
        @NotNull FilterChain filterChain) {

        if (SecurityConfiguration.permitAllCheck(request)) {

            filterChain.doFilter(request, response);
            return;

        }

        // 获取赋权所需要的对象
        BaseAuthorizationBO baseAuthorizationBO = getBaseAuthorizationBO(request);

        if (baseAuthorizationBO == null) {

            filterChain.doFilter(request, response);
            return;

        }

        if (baseAuthorizationBO.getIBizCode() != null) {

            ResponseUtil.out(response, baseAuthorizationBO.getIBizCode());
            return;

        }

        Set<String> authSet = MyJwtUtil.getAuthSetByUserId(baseAuthorizationBO.getUserId()); // 获取：权限

        List<GrantedAuthority> authoritieList =
            authSet.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(baseAuthorizationBO.getClaimsJson(), null, authoritieList));

        filterChain.doFilter(request, response);

    }

    /**
     * 获取赋权所需要的对象
     */
    @SneakyThrows
    private BaseAuthorizationBO getBaseAuthorizationBO(HttpServletRequest request) {

        // 从请求头里，获取：jwt字符串，备注：就算加了不需要登录就可以访问，但是也会走该方法
        String jwtStr = MyJwtUtil.getJwtStrByRequest(request);

        if (StrUtil.isBlank(jwtStr)) {

            String apiToken = MyJwtUtil.getApiTokenByRequest(request);

            if (StrUtil.isBlank(apiToken)) {
                return null;
            }

            // 通过 apiToken获取
            return getByApiToken(request, apiToken);

        } else {

            // 通过 jwt获取
            return getByJwt(request, jwtStr);

        }

    }

    /**
     * 通过 apiToken获取
     */
    private BaseAuthorizationBO getByApiToken(HttpServletRequest request, String apiToken) {

        BaseAuthorizationBO baseAuthorizationBO = new BaseAuthorizationBO();

        BaseApiTokenDO baseApiTokenDO =
            ChainWrappers.lambdaQueryChain(baseApiTokenMapper).eq(BaseApiTokenDO::getToken, apiToken)
                .eq(BaseApiTokenDO::getEnableFlag, true).select(BaseApiTokenDO::getId, BaseApiTokenDO::getUserId).one();

        if (baseApiTokenDO == null) {

            baseAuthorizationBO.setIBizCode(TempBizCodeEnum.LOGIN_EXPIRED);
            return baseAuthorizationBO;

        }

        MyThreadUtil.execute(() -> {

            // 更新：最新使用时间
            ChainWrappers.lambdaUpdateChain(baseApiTokenMapper).eq(BaseApiTokenDO::getId, baseApiTokenDO.getId())
                .set(BaseApiTokenDO::getLastUseTime, new Date()).update();

        });

        Long userId = baseApiTokenDO.getUserId();

        JSONObject payloadMap = BaseJwtUtil.getPayloadMap(userId, null);

        // 处理：BaseAuthorizationBO对象
        return handleBaseAuthorizationBO(request, userId, payloadMap, baseAuthorizationBO);

    }

    /**
     * 通过 jwt获取
     */
    private BaseAuthorizationBO getByJwt(HttpServletRequest request, String jwtStr) {

        BaseAuthorizationBO baseAuthorizationBO = new BaseAuthorizationBO();

        jwtStr = handleJwtStr(jwtStr, request); // 处理：jwtStr

        JWT jwt;

        try {

            jwt = JWT.of(jwtStr);

        } catch (Exception e) {

            MyExceptionUtil.printError(e, StrUtil.format("，uri：{}，jwtStr：{}", request.getRequestURI(), jwtStr));
            baseAuthorizationBO.setIBizCode(TempBizCodeEnum.LOGIN_EXPIRED);
            return baseAuthorizationBO;

        }

        // 获取：userId的值
        Long userId = MyJwtUtil.getPayloadMapUserIdValue(jwt.getPayload().getClaimsJson());

        if (userId == null) {

            baseAuthorizationBO.setIBizCode(TempBizCodeEnum.LOGIN_EXPIRED);
            return baseAuthorizationBO;

        }

        boolean exists = redissonClient.getBucket(
            MyJwtUtil.generateRedisJwt(jwtStr, userId, RequestUtil.getRequestCategoryEnum(request))).isExists();

        if (!exists) {

            baseAuthorizationBO.setIBizCode(TempBizCodeEnum.LOGIN_EXPIRED);
            return baseAuthorizationBO;

        }

        // 处理：BaseAuthorizationBO对象
        return handleBaseAuthorizationBO(request, userId, jwt.getPayload().getClaimsJson(), baseAuthorizationBO);

    }

    /**
     * 处理：BaseAuthorizationBO对象
     */
    private BaseAuthorizationBO handleBaseAuthorizationBO(HttpServletRequest request, Long userId,
        JSONObject claimsJson, BaseAuthorizationBO baseAuthorizationBO) {

        // 扩展处理 jwt
        IBizCode iBizCode = handleIjwtFilterList(userId, claimsJson, request);

        if (iBizCode != null) {

            baseAuthorizationBO.setIBizCode(iBizCode);
            return baseAuthorizationBO;

        }

        baseAuthorizationBO.setUserId(userId);
        baseAuthorizationBO.setClaimsJson(claimsJson);

        return baseAuthorizationBO;

    }

    /**
     * 扩展处理 jwt
     *
     * @return null 表示成功，反之表示失败
     */
    private IBizCode handleIjwtFilterList(Long userId, JSONObject claimsJson, HttpServletRequest request) {

        if (CollUtil.isEmpty(iJwtFilterHandlerList)) {
            return null;
        }

        String ip = RequestUtil.getIp(request);

        for (IJwtFilterHandler iJwtFilterHandler : iJwtFilterHandlerList) {

            IBizCode iBizCode = iJwtFilterHandler.handleJwt(userId, ip, claimsJson, request);

            if (iBizCode != null) {

                return iBizCode;

            }

        }

        return null;

    }

    /**
     * 处理：jwtStr
     */
    @Nullable
    private String handleJwtStr(String jwtStr, HttpServletRequest request) {

        // 如果是开发环境：Authorization Bearer 0
        if (BaseConfiguration.devFlag() && NumberUtil.isNumber(jwtStr)) {

            SignInVO signInVO;

            if (iJwtGenerateConfiguration != null) {

                signInVO = iJwtGenerateConfiguration.generateJwt(Convert.toLong(jwtStr), null, false,
                    RequestUtil.getRequestCategoryEnum(request));

            } else {

                signInVO = BaseJwtUtil.generateJwt(Convert.toLong(jwtStr), null, false,
                    RequestUtil.getRequestCategoryEnum(request), null, false);

            }

            if (signInVO != null) {

                String jwtStrTmp = signInVO.getJwt();

                log.info("jwtStrTmp：{}", jwtStrTmp);

                jwtStr = MyJwtUtil.getJwtStrByHeadAuthorization(jwtStrTmp);

            }

        }

        return jwtStr;

    }

}
