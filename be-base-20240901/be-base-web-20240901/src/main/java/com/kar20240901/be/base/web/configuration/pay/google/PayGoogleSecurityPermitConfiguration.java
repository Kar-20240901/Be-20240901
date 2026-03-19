package com.kar20240901.be.base.web.configuration.pay.google;

import cn.hutool.core.collection.CollUtil;
import com.kar20240901.be.base.web.model.configuration.base.ISecurityPermitConfiguration;
import java.util.Set;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayGoogleSecurityPermitConfiguration implements ISecurityPermitConfiguration {

    @Override
    public Set<String> anyPermitAllSet() {
        return CollUtil.newHashSet("/base/pay/google/notifyCallBack/**");
    }

}
