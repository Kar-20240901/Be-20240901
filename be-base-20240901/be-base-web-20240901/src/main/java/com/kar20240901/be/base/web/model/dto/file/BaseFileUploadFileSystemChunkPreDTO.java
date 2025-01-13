package com.kar20240901.be.base.web.model.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseFileUploadFileSystemChunkPreDTO extends BaseFileUploadDTO {

    @NotBlank
    @Schema(description = "文件名")
    private String fileName;

    @NotNull
    @Schema(description = "文件总大小")
    private Long fileSize;

    @NotBlank
    @Schema(description = "文件签名，用于校验文件是否完整，一般采用 md5的方式")
    private String fileSign;

}
