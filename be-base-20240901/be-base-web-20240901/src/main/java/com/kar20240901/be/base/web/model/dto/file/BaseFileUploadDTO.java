package com.kar20240901.be.base.web.model.dto.file;

import com.kar20240901.be.base.web.model.enums.file.BaseFileUploadTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BaseFileUploadDTO {

    @Schema(description = "文件")
    private MultipartFile file;

    @Schema(description = "文件上传的类型")
    private BaseFileUploadTypeEnum uploadType;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "额外信息（json格式）")
    private String extraJson;

    @Schema(description = "关联的 id")
    private Long refId;

}
