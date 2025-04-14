package com.kar20240901.be.base.web.model.domain.im;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_im_session")
@Data
@Schema(description = "主表：会话表")
public class BaseImSessionDO extends TempEntity {

    @Schema(description = "会话名")
    private String name;

    @Schema(description = "会话类型：101 私聊 201 群聊 301 客服")
    private Integer type;

    @Schema(description = "归属者主键 id（群主），备注：如果为客服类型时，群主必须是用户，私聊时，该值为申请人userId")
    private Long belongId;

    @Schema(description = "最后一次接受到消息时的时间戳，默认为：-1，备注：该字段用于：排序")
    private Long lastReceiveContentTs;

    @Schema(description = "头像 fileId（文件主键 id）")
    private Long avatarFileId;

}