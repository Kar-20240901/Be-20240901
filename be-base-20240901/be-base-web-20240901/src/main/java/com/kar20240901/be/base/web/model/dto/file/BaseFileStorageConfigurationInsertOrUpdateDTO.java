package com.kar20240901.be.base.web.model.dto.file;

import com.kar20240901.be.base.web.model.dto.base.TempInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.interfaces.file.IBaseFileStorageType;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseFileStorageConfigurationInsertOrUpdateDTO extends TempInsertOrUpdateDTO {

    @NotBlank
    @Schema(description = "文件存储配置名")
    private String name;

    /**
     * {@link IBaseFileStorageType}
     */
    @NotNull
    @Schema(description = "文件存储类型")
    private Integer type;

    @NotBlank
    @Schema(description = "钥匙")
    private String accessKey;

    @NotBlank
    @Schema(description = "秘钥")
    private String secretKey;

    @NotBlank
    @Schema(description = "上传的端点")
    private String uploadEndpoint;

    @NotBlank
    @Schema(description = "公开下载的端点")
    private String publicDownloadEndpoint;

    @NotBlank
    @Schema(description = "公开类型的桶名")
    private String bucketPublicName;

    @NotBlank
    @Schema(description = "私有类型的桶名")
    private String bucketPrivateName;

    @Schema(description = "是否是默认存储，备注：只会有一个默认存储")
    private Boolean defaultFlag;

    @Schema(description = "是否启用")
    private Boolean enableFlag;

    @Schema(description = "备注")
    private String remark;

}
