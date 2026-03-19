package com.kar20240901.be.base.web.controller.pay;

import com.kar20240901.be.base.web.service.pay.PayWxService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/base/payWx")
@Tag(name = "基础-支付-微信")
public class PayWxController {

    @Resource
    PayWxService baseService;

    @Operation(summary = "服务器异步通知-native，备注：第三方应用调用", hidden = true)
    @PostMapping(value = "/notifyCallBack/native/{basePayConfigurationId}")
    public void notifyCallBackNative(HttpServletRequest request, HttpServletResponse response,
        @PathVariable(value = "basePayConfigurationId") long basePayConfigurationId) {
        baseService.notifyCallBackNative(request, response, basePayConfigurationId);
    }

    @Operation(summary = "服务器异步通知-jsApi，备注：第三方应用调用", hidden = true)
    @PostMapping(value = "/notifyCallBack/jsApi/{basePayConfigurationId}")
    public void notifyCallBackJsApi(HttpServletRequest request, HttpServletResponse response,
        @PathVariable(value = "basePayConfigurationId") long basePayConfigurationId) {
        baseService.notifyCallBackJsApi(request, response, basePayConfigurationId);
    }

}
