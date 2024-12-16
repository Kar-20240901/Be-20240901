package com.kar20240901.be.base.web.model.dto.pay;

import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BasePayGooglePayConsumeDTO extends NotNullId {

    @NotNull
    @Schema(description = "支付配置主键 id")
    private Long basePayConfigurationId;

}
