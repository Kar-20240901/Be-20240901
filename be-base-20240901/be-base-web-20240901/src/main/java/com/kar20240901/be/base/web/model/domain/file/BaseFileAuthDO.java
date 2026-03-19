package com.kar20240901.be.base.web.model.domain.file;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_file_auth")
@Data
@Schema(description = "子表：文件操作权限表，主表：文件表")
public class BaseFileAuthDO extends TempEntity {

    @Schema(description = "文件主键 id")
    private Long fileId;

    @Schema(description = "此权限拥有者的 userId")
    private Long userId;

    @Schema(description = "是否可读：0 否 1 是")
    private Boolean readFlag;

    @Schema(description = "是否可写：0 否 1 是")
    private Boolean writeFlag;

}
