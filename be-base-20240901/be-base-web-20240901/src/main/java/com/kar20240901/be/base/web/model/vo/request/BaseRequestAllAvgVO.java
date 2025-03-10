package com.kar20240901.be.base.web.model.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseRequestAllAvgVO {

    @Schema(description = "请求的总数")
    private Long count;

    @Schema(description = "请求的平均耗时（毫秒）")
    private Integer avgMs;

}
