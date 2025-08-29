package com.kar20240901.be.base.web.model.dto.im;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BaseImSearchHistoryAddDTO {

    @Schema(description = "搜索的内容")
    private String searchHistory;

}
