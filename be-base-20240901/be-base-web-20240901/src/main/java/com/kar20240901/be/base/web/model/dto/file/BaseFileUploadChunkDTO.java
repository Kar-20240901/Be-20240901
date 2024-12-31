package com.kar20240901.be.base.web.model.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseFileUploadChunkDTO extends BaseFileUploadDTO {

    @NotNull
    @Schema(description = "上传id")
    private Long uploadId;

    @NotNull
    @Schema(description = "分片开始位置（包含）")
    private Long chunkBeginNum;

    @NotNull
    @Schema(description = "分片结束位置（包含）")
    private Long chunkEndNum;

    @NotNull
    @Schema(description = "当前分片在整个文件中的顺序编号，每个分片都有一个唯一的编号（从1开始）")
    private Integer chunkNum;

}
