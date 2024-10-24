package com.kar20240901.be.base.web.model.domain.file;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240901.be.base.web.model.domain.base.TempEntityTree;
import com.kar20240901.be.base.web.model.enums.file.BaseFileTypeEnum;
import com.kar20240901.be.base.web.model.interfaces.file.IBaseFileStorageType;
import com.kar20240901.be.base.web.model.interfaces.file.IBaseFileUploadType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_file")
@Data
@Schema(description = "主表：文件表")
public class BaseFileDO extends TempEntityTree<BaseFileDO> {

    @Schema(description = "归属者用户主键 id（拥有全部权限）")
    private Long belongId;

    @Schema(description = "桶名，例如：be-bucket")
    private String bucketName;

    @Schema(description = "文件完整路径（包含文件类型，不包含请求端点），例如：avatar/uuid.xxx")
    private String uri;

    @Schema(description = "文件原始名（包含文件类型）")
    private String originFileName;

    @Schema(description = "新的文件名（包含文件类型），例如：uuid.xxx")
    private String newFileName;

    @Schema(description = "文件类型（不含点），备注：这个是读取文件流的头部信息获得文件类型")
    private String fileExtName;

    @Schema(description = "额外信息（json格式）")
    private String extraJson;

    /**
     * {@link IBaseFileUploadType}
     */
    @Schema(description = "文件上传类型")
    private Integer uploadType;

    @Schema(description = "存储文件配置主键 id")
    private Long storageConfigurationId;

    /**
     * {@link IBaseFileStorageType}
     */
    @Schema(description = "存放文件的服务器类型")
    private Integer storageType;

    @TableField(exist = false)
    @Schema(description = "排序号（值越大越前面，默认为 0）")
    private Integer orderNo;

    @Schema(description = "上级文件夹的文件主键 id，默认为 0")
    private Long pid;

    @Schema(description = "类型")
    private BaseFileTypeEnum type;

    @Schema(description = "展示用的文件名，默认为：原始文件名（包含文件类型）")
    private String showFileName;

    @Schema(description = "是否公开访问")
    private Boolean publicFlag;

    @Schema(description = "文件大小，单位：byte")
    private Long fileSize;

    @Schema(description = "关联的 id")
    private Long refId;

}
