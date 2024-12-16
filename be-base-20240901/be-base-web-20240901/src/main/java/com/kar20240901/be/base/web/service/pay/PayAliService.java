package com.kar20240901.be.base.web.service.pay;

import javax.servlet.http.HttpServletRequest;

public interface PayAliService {

    String notifyCallBack(HttpServletRequest request, long basePayConfigurationId);

}
