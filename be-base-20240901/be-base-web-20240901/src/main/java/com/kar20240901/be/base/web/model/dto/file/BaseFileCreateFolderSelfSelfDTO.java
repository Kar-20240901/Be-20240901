package com.kar20240901.be.base.web.model.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BaseFileCreateFolderSelfSelfDTO {

    @Schema(description = "父节点id（顶级则为0）")
    private Long pid;

    @NotBlank
    @Schema(description = "文件夹名")
    private String folderName;

}
