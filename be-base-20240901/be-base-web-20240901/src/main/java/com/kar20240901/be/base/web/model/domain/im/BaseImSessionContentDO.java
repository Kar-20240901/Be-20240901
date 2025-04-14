package com.kar20240901.be.base.web.model.domain.im;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_im_session_content")
@Data
@Schema(description = "子表：会话内容表，主表：会话表")
public class BaseImSessionContentDO extends TempEntity {

    @Schema(description = "会话主键 id")
    private Long sessionId;

    @Schema(description = "会话内容")
    private String content;

    @Schema(description = "内容类型")
    private Integer type;

    @Schema(description = "创建时间的时间戳")
    private Long createTs;

}