package com.kar20240901.be.base.web.model.domain.im;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_im_session_ref_user")
@Data
@Schema(description = "关联表：会话表，用户表")
public class BaseImSessionRefUserDO extends TempEntity {

    @Schema(description = "我对会话的备注")
    private String remark;

    @Schema(description = "会话主键 id")
    private Long sessionId;

    @Schema(description = "用户主键 id")
    private Long userId;

    @Schema(description = "我在会话的昵称")
    private String sessionNickname;

    @Schema(description = "我最后一次打开该会话的时间戳")
    private Long lastOpenTs;

    @Schema(description = "是否显示在会话列表")
    private Boolean showFlag;

    @Schema(description = "是私聊时，关联的另外一个用户的主键 id，其他类型时，该值为：-1")
    private Long privateChatRefUserId;

    @Schema(description = "群聊时是否没有被禁言，或者私聊时是否没有被删除")
    private Boolean enableFlag;

    @Schema(description = "是否被拉黑")
    private Boolean blockFlag;

}