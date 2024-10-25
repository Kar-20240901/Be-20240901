package com.kar20240901.be.base.web.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.jwt.JWT;
import com.kar20240901.be.base.web.configuration.base.BaseConfiguration;
import com.kar20240901.be.base.web.configuration.security.SecurityConfiguration;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.model.configuration.base.IJwtGenerateConfiguration;
import com.kar20240901.be.base.web.model.interfaces.base.IJwtFilterHandler;
import com.kar20240901.be.base.web.model.vo.base.SignInVO;
import com.kar20240901.be.base.web.util.base.BaseJwtUtil;
import com.kar20240901.be.base.web.util.base.MyExceptionUtil;
import com.kar20240901.be.base.web.util.base.MyJwtUtil;
import com.kar20240901.be.base.web.util.base.MyThreadUtil;
import com.kar20240901.be.base.web.util.base.RequestUtil;
import com.kar20240901.be.base.web.util.base.ResponseUtil;
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

    @SneakyThrows
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
        @NotNull FilterChain filterChain) {

        if (SecurityConfiguration.permitAllCheck(request)) {

            filterChain.doFilter(request, response);
            return;

        }

        // 从请求头里，获取：jwt字符串，备注：就算加了不需要登录就可以访问，但是也会走该方法
        String jwtStr = MyJwtUtil.getJwtStrByRequest(request);

        if (jwtStr == null) {

            filterChain.doFilter(request, response);
            return;

        }

        jwtStr = handleJwtStr(jwtStr, request); // 处理：jwtStr

        JWT jwt;

        try {

            jwt = JWT.of(jwtStr);

        } catch (Exception e) {

            MyExceptionUtil.printError(e);
            ResponseUtil.out(response, TempBizCodeEnum.LOGIN_EXPIRED);
            return;

        }

        // 获取：userId的值
        Long userId = MyJwtUtil.getPayloadMapUserIdValue(jwt.getPayload().getClaimsJson());

        if (userId == null) {

            ResponseUtil.out(response, TempBizCodeEnum.LOGIN_EXPIRED);
            return;

        }

        handleIjwtFilterList(userId, jwt, request); // 扩展处理 jwt

        Set<String> authSet = MyJwtUtil.getAuthSetByUserId(userId); // 获取：权限

        List<GrantedAuthority> authoritieList =
            authSet.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(jwt.getPayload().getClaimsJson(), null, authoritieList));

        filterChain.doFilter(request, response);

    }

    /**
     * 扩展处理 jwt
     */
    private void handleIjwtFilterList(Long userId, JWT jwt, HttpServletRequest request) {

        if (CollUtil.isEmpty(iJwtFilterHandlerList)) {
            return;
        }

        MyThreadUtil.execute(() -> {

            String ip = RequestUtil.getIp(request);

            for (IJwtFilterHandler iJwtFilterHandler : iJwtFilterHandlerList) {

                iJwtFilterHandler.handleJwt(userId, ip, jwt);

            }

        });

    }

    /**
     * 处理：jwtStr
     */
    @Nullable
    private String handleJwtStr(String jwtStr, HttpServletRequest request) {

        // 如果不是正式环境：Authorization Bearer 0
        if (BaseConfiguration.prodFlag() == false && NumberUtil.isNumber(jwtStr)) {

            SignInVO signInVO;

            if (iJwtGenerateConfiguration != null) {

                signInVO = iJwtGenerateConfiguration.generateJwt(Convert.toLong(jwtStr), null, false,
                    RequestUtil.getRequestCategoryEnum(request));

            } else {

                signInVO = BaseJwtUtil.generateJwt(Convert.toLong(jwtStr), null, false,
                    RequestUtil.getRequestCategoryEnum(request), null);

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
