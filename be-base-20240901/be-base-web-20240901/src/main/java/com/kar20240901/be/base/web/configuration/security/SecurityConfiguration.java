package com.kar20240901.be.base.web.configuration.security;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.kar20240901.be.base.web.configuration.base.BaseConfiguration;
import com.kar20240901.be.base.web.filter.JwtAuthorizationFilter;
import com.kar20240901.be.base.web.model.configuration.ISecurityPermitConfiguration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.intercept.aopalliance.MethodSecurityInterceptor;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启 @PreAuthorize 权限注解
@EnableWebSecurity
@Slf4j
public class SecurityConfiguration {

    private static final List<AntPathRequestMatcher> PERMIT_ALL_ANT_PATH_REQUEST_MATCHER_LIST = new ArrayList<>();

    /**
     * 权限检查的 urlMap
     */
    private static final Map<String, Boolean> PERMIT_URL_MAP = new ConcurrentHashMap<>();

    /**
     * 检查：是否不需要权限检查
     *
     * @return true 不需要 false 需要
     */
    public static boolean permitAllCheck(HttpServletRequest request) {

        String uri = request.getRequestURI();

        Boolean passFlag = PERMIT_URL_MAP.get(uri);

        if (passFlag != null) {

            return passFlag;

        }

        for (AntPathRequestMatcher item : PERMIT_ALL_ANT_PATH_REQUEST_MATCHER_LIST) {

            if (item.matcher(request).isMatch()) {

                PERMIT_URL_MAP.put(uri, true);

                return true;

            }

        }

        PERMIT_URL_MAP.put(uri, false);

        return false;

    }

    /**
     * @param methodSecurityInterceptor 注意：这个名字不要改
     */
    public SecurityConfiguration(@Autowired(required = false) @Nullable MethodInterceptor methodSecurityInterceptor) {

        if (methodSecurityInterceptor instanceof MethodSecurityInterceptor) {

            AffirmativeBased accessDecisionManager =
                (AffirmativeBased)((MethodSecurityInterceptor)methodSecurityInterceptor).getAccessDecisionManager();

            accessDecisionManager.getDecisionVoters().add(0, new MyAccessDecisionVoter()); // 添加：自定义投票者

        }

    }

    /**
     * @param baseConfiguration 不要删除，目的：让 springboot实例化该对象
     */
    @SneakyThrows
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, BaseConfiguration baseConfiguration,
        List<ISecurityPermitConfiguration> iSecurityPermitConfigurationList,
        JwtAuthorizationFilter jwtAuthorizationFilter) {

        boolean prodFlag = BaseConfiguration.prodFlag();

        Set<String> permitAllSet = new HashSet<>();

        if (CollUtil.isNotEmpty(iSecurityPermitConfigurationList)) {

            for (ISecurityPermitConfiguration item : iSecurityPermitConfigurationList) {

                if (prodFlag) {

                    CollUtil.addAll(permitAllSet, item.prodPermitAllSet());

                } else {

                    CollUtil.addAll(permitAllSet, item.devPermitAllSet());

                }

                CollUtil.addAll(permitAllSet, item.anyPermitAllSet());

            }

        }

        log.info("permitAllSet：{}", permitAllSet);

        if (CollUtil.isNotEmpty(permitAllSet) && CollUtil.isEmpty(PERMIT_ALL_ANT_PATH_REQUEST_MATCHER_LIST)) {

            for (String item : permitAllSet) {

                PERMIT_ALL_ANT_PATH_REQUEST_MATCHER_LIST.add(new AntPathRequestMatcher(item));

            }

        }

        httpSecurity.authorizeRequests().antMatchers(ArrayUtil.toArray(permitAllSet, String.class))
            .permitAll() // 可以匿名访问的请求
            .anyRequest().authenticated(); // 拦截所有请求

        httpSecurity.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 不需要session

        // 用户没有登录，但是访问需要权限的资源时，而报出的错误
        httpSecurity.exceptionHandling().authenticationEntryPoint(new MyAuthenticationEntryPoint());

        httpSecurity.csrf().disable(); // 禁用 CSRF保护

        httpSecurity.logout().disable(); // 禁用 logout

        httpSecurity.formLogin().disable(); // 禁用 login

        httpSecurity.httpBasic().disable(); // 禁用 http basic认证

        return httpSecurity.build();

    }

}
