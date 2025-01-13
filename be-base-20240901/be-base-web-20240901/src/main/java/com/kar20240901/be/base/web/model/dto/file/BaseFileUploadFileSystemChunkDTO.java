package com.kar20240901.be.base.web.model.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseFileUploadFileSystemChunkDTO extends BaseFileUploadDTO {

    @NotNull
    @Schema(description = "传输id")
    private Long transferId;

    @NotNull
    @Schema(description = "当前分片在整个文件中的顺序编号，每个分片都有一个唯一的编号（从1开始）")
    private Integer chunkNum;

}
