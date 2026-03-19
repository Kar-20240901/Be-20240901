package com.kar20240901.be.base.web.model.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseFileUploadFileSystemPreDTO extends BaseFileUploadDTO {

    @NotBlank
    @Schema(description = "文件名")
    private String fileName;

    @NotNull
    @Schema(description = "文件总大小")
    private Long fileSize;

}
