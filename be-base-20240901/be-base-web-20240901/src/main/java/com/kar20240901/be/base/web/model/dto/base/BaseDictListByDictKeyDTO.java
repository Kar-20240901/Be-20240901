package com.kar20240901.be.base.web.model.dto.base;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BaseDictListByDictKeyDTO {

    @NotBlank
    @Schema(description = "字典 key")
    private String dictKey;

}
