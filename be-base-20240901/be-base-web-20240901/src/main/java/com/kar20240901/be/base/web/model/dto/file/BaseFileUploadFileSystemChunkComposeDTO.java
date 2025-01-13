package com.kar20240901.be.base.web.model.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class BaseFileUploadFileSystemChunkComposeDTO {

    @NotNull
    @Schema(description = "传输id")
    private Long transferId;

}
