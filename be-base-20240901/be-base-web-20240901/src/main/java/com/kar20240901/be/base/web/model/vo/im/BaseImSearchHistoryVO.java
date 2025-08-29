package com.kar20240901.be.base.web.model.vo.im;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BaseImSearchHistoryVO {

    private Long id;

    @Schema(description = "搜索的内容")
    private String searchHistory;

}
