package com.kar20240901.be.base.web.model.vo.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BaseFileUploadFileSystemPreVO {

    @Schema(description = "文件主键id")
    private Long fileId;

    @Schema(description = "传输id")
    private Long transferId;

}
