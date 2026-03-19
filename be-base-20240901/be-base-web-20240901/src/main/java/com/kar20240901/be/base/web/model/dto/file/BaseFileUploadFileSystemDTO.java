package com.kar20240901.be.base.web.model.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BaseFileUploadFileSystemDTO {

    @Schema(description = "文件")
    private MultipartFile file;

    @Schema(description = "传输id")
    private Long transferId;

}
