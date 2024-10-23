package com.kar20240901.be.base.web.model.bo.file;

import com.kar20240901.be.base.web.model.interfaces.file.IBaseFileUploadType;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BaseFileUploadBO {

    @NotNull
    @Schema(description = "文件")
    private MultipartFile file;

    @NotNull
    @Schema(description = "文件上传的类型")
    private IBaseFileUploadType uploadType;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "额外信息（json格式）")
    private String extraJson;

    @NotNull
    @Schema(description = "用户主键 id")
    private Long userId;

    @Schema(description = "关联的 id")
    private Long refId;

}
