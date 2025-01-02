package com.kar20240901.be.base.web.model.bo.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseFileUploadChunkBO {

    @Schema(description = "上传id")
    private String uploadId;

    @Schema(description = "当前分片在整个文件中的顺序编号，每个分片都有一个唯一的编号（从1开始）")
    private Integer partNumber;

}
