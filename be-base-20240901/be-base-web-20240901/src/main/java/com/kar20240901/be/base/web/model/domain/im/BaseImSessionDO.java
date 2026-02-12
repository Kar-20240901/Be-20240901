package com.kar20240901.be.base.web.model.domain.im;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@TableName(value = "base_im_session")
@Data
@Schema(description = "主表：会话表")
public class BaseImSessionDO {

    @TableId(type = IdType.INPUT)
    @Schema(description = "会话主键 id")
    private Long id;

    @Schema(
        description = "来源申请 id，目的：删除好友/群组之后，还可以恢复之前的会话内容，备注：群组不支持会话内容恢复，所以群组该值为 -1")
    private Long sourceApplyId;

    @Schema(description = "来源申请类型：101 好友 201 群组")
    private Integer sourceApplyType;

    @Schema(description = "最后一次接受到消息时的时间戳，默认为：当前时间，备注：该字段用于：排序")
    private Long lastReceiveTs;

}