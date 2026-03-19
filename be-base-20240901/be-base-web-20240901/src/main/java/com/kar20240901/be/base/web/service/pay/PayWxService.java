package com.kar20240901.be.base.web.service.pay;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PayWxService {

    void notifyCallBackNative(HttpServletRequest request, HttpServletResponse response, long basePayConfigurationId);

    void notifyCallBackJsApi(HttpServletRequest request, HttpServletResponse response, long basePayConfigurationId);

}
