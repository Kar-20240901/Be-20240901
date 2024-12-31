package com.kar20240901.be.base.web.model.vo.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BaseFileUploadChunkPreVO {

    @Schema(description = "文件主键id")
    private Long fileId;

    @Schema(description = "每个分片的大小")
    private Integer chunkSize;

    @Schema(description = "总分片个数")
    private Integer chunkTotal;

    @Schema(description = "传输id")
    private Long transferId;

}
