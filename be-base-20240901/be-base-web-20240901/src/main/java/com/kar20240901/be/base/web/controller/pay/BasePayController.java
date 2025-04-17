package com.kar20240901.be.base.web.controller.pay;

import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.enums.pay.BasePayTradeStatusEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.pay.BasePayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/base/pay")
@RestController
@Tag(name = "基础-支付-管理")
public class BasePayController {

    @Resource
    BasePayService baseService;

    @Operation(summary = "通过主键id，查看支付状态-本平台")
    @PostMapping("/payTradeStatusById")
    public R<BasePayTradeStatusEnum> payTradeStatusById(@RequestBody @Valid NotNullId notNullId) {
        return R.okData(baseService.payTradeStatusById(notNullId));
    }

    @Operation(summary = "通过主键id，查看支付状态-第三方支付平台")
    @PostMapping("/payTradeStatusById/third")
    public R<BasePayTradeStatusEnum> payTradeStatusByIdThird(@RequestBody @Valid NotNullId notNullId) {
        return R.okData(baseService.payTradeStatusByIdThird(notNullId));
    }

}
