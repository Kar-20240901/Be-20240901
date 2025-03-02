package com.kar20240901.be.base.web.model.dto.pay;

import com.kar20240901.be.base.web.model.dto.base.MyPageDTO;
import com.kar20240901.be.base.web.model.enums.pay.BasePayTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BasePayConfigurationPageDTO extends MyPageDTO {

    @Schema(description = "是否是默认支付方式，备注：只会有一个默认支付方式")
    private Boolean defaultFlag;

    @Schema(description = "支付类型：101 支付宝 201 微信 301 云闪付 401 谷歌")
    private BasePayTypeEnum type;

    @Schema(description = "支付名（不可重复）")
    private String name;

    @Schema(description = "支付平台，应用 id")
    private String appId;

    @Schema(description = "是否启用")
    private Boolean enableFlag;

    @Schema(description = "备注")
    private String remark;

}
