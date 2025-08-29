package com.kar20240901.be.base.web.model.dto.im;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BaseImSearchBaseDTO {

    @NotBlank
    @Schema(description = "搜索内容")
    private String searchKey;

}
