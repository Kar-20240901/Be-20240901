package com.kar20240901.be.base.web.model.domain.im;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@TableName(value = "base_im_session_ref_user")
@Data
@Schema(description = "子表：会话关联用户表，主表：会话表")
public class BaseImSessionRefUserDO {

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键 id")
    private Long id;

    @Schema(description = "会话主键 id")
    private Long sessionId;

    @Schema(description = "用户主键 id")
    private Long userId;

    @Schema(description = "我最后一次打开该会话的时间戳")
    private Long lastOpenTs;

    @Schema(description = "是否显示")
    private Integer showFlag;

    @Schema(description = "显示的会话名")
    private String name;

    @Schema(description = "最后一次接受到消息时的时间戳，默认为：-1，备注：该字段用于：排序")
    private Long lastReceiveTs;

}