package com.kar20240901.be.base.web.model.domain.file;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_file_transfer_chunk")
@Data
@Schema(description = "子表：文件传输分片表，主表：文件传输表")
public class BaseFileTransferChunkDO extends TempEntity {

    @Schema(description = "文件传输主键 id")
    private Long transferId;

    @Schema(description = "用户主键 id")
    private Long userId;

    @Schema(description = "文件主键 id")
    private Long fileId;

    @Schema(description = "分片开始位置（包含），从 0开始")
    private Long chunkBeginNum;

    @Schema(description = "分片结束位置（包含）")
    private Long chunkEndNum;

    @Schema(description = "分片大小，计算方式：结束位置 - 开始位置 + 1")
    private Long chunkSize;

    @Schema(description = "分片序号，从 1开始")
    private Integer chunkNum;

    @Schema(description = "已经传输的分片大小")
    private Long currentSize;

    @Schema(description = "文件完整路径（包含文件类型，不包含请求端点），例如：avatar/uuid.xxx")
    private String uri;

    @Schema(description = "展示用的文件名，默认为：原始文件名（包含文件类型）")
    private String showFileName;

    @Schema(description = "关联的数据")
    private String refData;

}