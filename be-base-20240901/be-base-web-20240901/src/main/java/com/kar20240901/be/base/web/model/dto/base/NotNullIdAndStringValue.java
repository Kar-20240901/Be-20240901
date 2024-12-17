package com.kar20240901.be.base.web.model.dto.base;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotNullIdAndStringValue extends NotNullId {

    public NotNullIdAndStringValue(Long id, String value) {

        super(id);
        this.value = value;

    }

    @NotBlank
    @Schema(description = "值")
    private String value;

}
