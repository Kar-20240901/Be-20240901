package com.kar20240901.be.base.web.controller.pay;

import com.kar20240901.be.base.web.model.dto.pay.BasePayGooglePayConsumeDTO;
import com.kar20240901.be.base.web.model.dto.pay.BasePayGooglePaySuccessDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.pay.PayGoogleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/base/payGoogle")
@Tag(name = "基础-支付-谷歌")
public class PayGoogleController {

    @Resource
    PayGoogleService baseService;

    @Operation(summary = "支付成功的回调，备注：由客户端调用")
    @PostMapping(value = "/paySuccess")
    public R<Boolean> paySuccess(@RequestBody @Valid BasePayGooglePaySuccessDTO dto) {
        return R.okData(baseService.paySuccess(dto));
    }

    @Operation(summary = "支付核销的回调，备注：由客户端调用")
    @PostMapping(value = "/payConsume")
    public R<Boolean> payConsume(@RequestBody @Valid BasePayGooglePayConsumeDTO dto) {
        return R.okData(baseService.payConsume(dto));
    }

}
