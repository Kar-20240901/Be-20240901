package com.kar20240901.be.base.web.model.dto.base;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChangeBigDecimalNumberIdSetDTO extends NotEmptyIdSet {

    @NotNull
    @Schema(description = "需要改变的数值")
    private BigDecimal number;

}
