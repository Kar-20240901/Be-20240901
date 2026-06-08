package com.kar20240901.be.base.web.model.vo.server;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BaseServerWorkInfoVO {

    @Schema(description = "CPU使用率（0-100）%")
    private String cpuUsage;

    @Schema(description = "内存使用率（0-100）%")
    private String memoryUsage;

    @Schema(description = "磁盘使用率（0-100）%")
    private String diskUsage;

}
