package com.kar20240901.be.base.web.controller.pay;

import cn.hutool.json.JSONObject;
import com.kar20240901.be.base.web.service.pay.PayApplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/base/payApply")
@Tag(name = "基础-支付-苹果")
public class PayApplyController {

    @Resource
    PayApplyService baseService;

    @Operation(summary = "服务器异步通知，备注：第三方应用调用", hidden = true)
    @PostMapping(value = "/notifyCallBack")
    public String notifyCallBack(@RequestBody JSONObject jsonObject) {
        return baseService.notifyCallBack(jsonObject);
    }

}
