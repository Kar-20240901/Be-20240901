package com.kar20240901.be.base.web.model.domain.file;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.interfaces.file.ISysFileStorageType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_file_storage_configuration")
@Data
@Schema(description = "主表：文件存储配置")
public class SysFileStorageConfigurationDO extends TempEntity {

    @Schema(description = "文件存储配置名")
    private String name;

    /**
     * {@link ISysFileStorageType}
     */
    @Schema(description = "文件存储类型")
    private Integer type;

    @Schema(description = "钥匙")
    private String accessKey;

    @Schema(description = "秘钥")
    private String secretKey;

    @Schema(description = "上传的端点")
    private String uploadEndpoint;

    @Schema(description = "公开下载的端点")
    private String publicDownloadEndpoint;

    @Schema(description = "公开类型的桶名")
    private String bucketPublicName;

    @Schema(description = "私有类型的桶名")
    private String bucketPrivateName;

    @Schema(description = "是否是默认存储，备注：只会有一个默认存储")
    private Boolean defaultFlag;

}
