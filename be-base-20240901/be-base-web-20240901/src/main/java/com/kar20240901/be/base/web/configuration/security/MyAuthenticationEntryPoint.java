package com.kar20240901.be.base.web.configuration.security;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.json.JSONUtil;
import com.kar20240901.be.base.web.exception.ExceptionAdvice;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.util.base.ResponseUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * 未登录异常
 */
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) {

        // 尚未登录，请先登录
        R<?> r = ResponseUtil.out(response, TempBizCodeEnum.NOT_LOGGED_IN_YET);

        ExceptionAdvice.handleRequest(request, null, ExceptionUtil.stacktraceToString(authException), "",
            JSONUtil.toJsonStr(r));

    }

}
