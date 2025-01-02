package com.kar20240901.be.base.web.model.domain.file;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.enums.file.BaseFileTransferStatusEnum;
import com.kar20240901.be.base.web.model.enums.file.BaseFileTransferTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_file_transfer")
@Data
@Schema(description = "子表：文件传输表，主表：文件表")
public class BaseFileTransferDO extends TempEntity {

    @TableId(type = IdType.INPUT)
    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "用户主键 id")
    private Long userId;

    @Schema(description = "文件主键 id")
    private Long fileId;

    @Schema(description = "类型：101 上传 201 下载")
    private BaseFileTransferTypeEnum type;

    @Schema(description = "冗余字段：新的文件名（包含文件类型），例如：uuid.xxx")
    private String newFileName;

    @Schema(description = "冗余字段：展示用的文件名，默认为：原始文件名（包含文件类型）")
    private String showFileName;

    @Schema(description = "冗余字段：文件大小")
    private Long fileSize;

    @Schema(description = "状态：101 传输中 201 传输暂停 301 传输完成 401 传输取消 501 合并中 601 合并完成")
    private BaseFileTransferStatusEnum status;

    @Schema(description = "文件签名，用于校验文件是否完整，一般采用 md5的方式")
    private String fileSign;

    @Schema(description = "每个分片的大小")
    private Long chunkSize;

    @Schema(description = "总分片个数")
    private Integer chunkTotal;

    @Schema(description = "冗余字段：桶名，例如：be-bucket")
    private String bucketName;

    @Schema(description = "冗余字段：存储文件配置主键 id")
    private Long storageConfigurationId;

    @Schema(description = "冗余字段：存放文件的服务器类型：101 阿里云oss 201 minio")
    private Integer storageType;

    @Schema(description = "冗余字段：文件完整路径（包含文件类型，不包含请求端点），例如：avatar/uuid.xxx")
    private String uri;

}