package com.kar20240901.be.base.web.security;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SignUserNameSecurityPermitConfiguration extends AbstractSignHelperSecurityPermitConfiguration {

    @Override
    protected String getSignPreUri() {
        return "userName";
    }

}
