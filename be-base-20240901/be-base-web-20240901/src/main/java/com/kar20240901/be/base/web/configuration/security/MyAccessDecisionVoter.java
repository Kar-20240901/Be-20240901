package com.kar20240901.be.base.web.configuration.security;

import com.kar20240901.be.base.web.util.base.MyUserUtil;
import java.util.Collection;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

/**
 * 自定义的 投票者
 */
public class MyAccessDecisionVoter implements AccessDecisionVoter<Object> {

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {

        // 如果是 admin，则可以访问所有接口
        if (MyUserUtil.getCurrentUserAdminFlag()) {
            return ACCESS_GRANTED; // 同意票
        }

        return ACCESS_ABSTAIN; // 中立票

    }

}
