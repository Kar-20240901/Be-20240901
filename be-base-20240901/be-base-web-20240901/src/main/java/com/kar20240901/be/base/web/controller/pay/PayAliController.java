package com.kar20240901.be.base.web.controller.pay;

import com.kar20240901.be.base.web.service.pay.PayAliService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/base/payAli")
@Tag(name = "基础-支付-支付宝")
public class PayAliController {

    @Resource
    PayAliService baseService;

    @Operation(summary = "服务器异步通知，备注：第三方应用调用", hidden = true)
    @PostMapping(value = "/notifyCallBack/{basePayConfigurationId}")
    public String notifyCallBack(HttpServletRequest request,
        @PathVariable(value = "basePayConfigurationId") long basePayConfigurationId) {
        return baseService.notifyCallBack(request, basePayConfigurationId);
    }

}
