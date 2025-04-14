package com.kar20240901.be.base.web.model.domain.im;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_im_session_content_ref_user")
@Data
@Schema(description = "子表：用户会话内容表，主表：会话表")
public class BaseImSessionContentRefUserDO extends TempEntity {

    @Schema(description = "冗余字段：会话主键 id")
    private Long sessionId;

    @Schema(description = "会话内容主键 id")
    private Long contentId;

    @Schema(description = "用户主键 id")
    private Long userId;

    @Schema(description = "是否显示在：用户消息中")
    private Boolean showFlag;

}