package com.kar20240901.be.base.web.model.dto.pay;

import com.kar20240901.be.base.web.model.dto.base.MyPageDTO;
import com.kar20240901.be.base.web.model.enums.pay.BasePayTradeStatusEnum;
import com.kar20240901.be.base.web.model.interfaces.pay.IBasePayRefStatus;
import com.kar20240901.be.base.web.model.interfaces.pay.IBasePayType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BasePayPageDTO extends MyPageDTO {

    @Schema(description = "主键 id")
    private Long id;

    /**
     * {@link IBasePayType}
     */
    @Schema(description = "支付方式")
    private Integer payType;

    @Schema(description = "支付配置主键 id")
    private Long basePayConfigurationId;

    @Schema(description = "支付状态")
    private BasePayTradeStatusEnum status;

    @Schema(description = "用户主键 id")
    private Long userId;

    /**
     * {@link IBasePayRefStatus}
     */
    @Schema(description = "关联的状态，建议：修改")
    private Integer refStatus;

}
