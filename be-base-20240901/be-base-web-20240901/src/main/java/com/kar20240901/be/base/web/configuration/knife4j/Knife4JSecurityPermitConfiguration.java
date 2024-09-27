package com.kar20240901.be.base.web.configuration.knife4j;

import cn.hutool.core.collection.CollUtil;
import com.kar20240901.be.base.web.model.configuration.ISecurityPermitConfiguration;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class Knife4JSecurityPermitConfiguration implements ISecurityPermitConfiguration {

    @Override
    public Set<String> anyPermitAllSet() {
        return CollUtil.newHashSet("/v3/api-docs/**", "/webjars/**", "/doc.html/**", "/favicon.ico");
    }

}
