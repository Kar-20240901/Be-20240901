package com.kar20240901.be.base.web.model.dto.base;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotNullLong {

    @NotNull
    @Schema(description = "值")
    private Long value;

}
