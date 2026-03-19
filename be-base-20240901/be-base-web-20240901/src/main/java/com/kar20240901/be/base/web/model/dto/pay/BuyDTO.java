package com.kar20240901.be.base.web.model.dto.pay;

import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.interfaces.pay.IBasePayType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BuyDTO extends NotNullId {

    /**
     * {@link IBasePayType}
     */
    @Schema(description = "支付方式，备注：如果为 null，则表示用默认支付方式")
    private Integer payType;

}
