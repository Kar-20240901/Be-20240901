package com.kar20240901.be.base.web.model.dto.pay;

import cn.hutool.core.lang.RegexPool;
import com.kar20240901.be.base.web.model.annotation.base.NotCheckBlankPattern;
import com.kar20240901.be.base.web.model.dto.base.TempInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.enums.pay.BasePayTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BasePayConfigurationInsertOrUpdateDTO extends TempInsertOrUpdateDTO {

    @Schema(description = "是否是默认支付方式，备注：只会有一个默认支付方式")
    private Boolean defaultFlag;

    @NotNull
    @Schema(description = "支付类型：101 支付宝 201 微信 301 云闪付 401 谷歌")
    private BasePayTypeEnum type;

    @NotBlank
    @Schema(description = "支付名")
    private String name;

    @NotCheckBlankPattern(regexp = RegexPool.URL)
    @Schema(description = "支付平台，网关地址，例如：https://openapi.alipay.com/gateway.do")
    private String serverUrl;

    @Schema(description = "支付平台，应用 id")
    private String appId;

    @Schema(description = "支付平台，私钥")
    private String privateKey;

    @Schema(description = "支付平台，公钥")
    private String platformPublicKey;

    @NotCheckBlankPattern(regexp = RegexPool.URL)
    @Schema(description = "支付平台，异步接收地址")
    private String notifyUrl;

    @Schema(description = "支付平台，商户号")
    private String merchantId;

    @Schema(description = "支付平台，商户证书序列号")
    private String merchantSerialNumber;

    @Schema(description = "支付平台，商户APIV3密钥")
    private String apiV3Key;

    @Schema(description = "是否启用")
    private Boolean enableFlag;

    @Schema(description = "备注")
    private String remark;

}
