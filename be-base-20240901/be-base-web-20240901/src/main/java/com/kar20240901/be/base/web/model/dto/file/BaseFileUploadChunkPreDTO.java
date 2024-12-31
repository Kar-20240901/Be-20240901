package com.kar20240901.be.base.web.model.dto.file;

import com.kar20240901.be.base.web.model.enums.file.BaseFileUploadTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class BaseFileUploadChunkPreDTO {

    @NotBlank
    @Schema(description = "文件名")
    private String fileName;

    @NotNull
    @Schema(description = "文件总大小")
    private Long fileSize;

    @NotNull
    @Schema(description = "每个分片的大小")
    private Integer chunkSize;

    @NotNull
    @Schema(description = "总分片个数")
    private Integer chunkTotal;

    @NotNull
    @Schema(description = "文件签名，用于校验文件是否完整，一般采用 md5的方式")
    private Integer fileSign;

    @Schema(description = "文件上传的类型")
    private BaseFileUploadTypeEnum uploadType;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "额外信息（json格式）")
    private String extraJson;

    @Schema(description = "关联的 id")
    private Long refId;

    @Schema(description = "父节点id（顶级则为0）")
    private Long pid;

}
