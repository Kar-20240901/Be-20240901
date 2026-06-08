package com.kar20240901.be.base.web.model.vo.im;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BaseImApplyGroupSearchApplyGroupVO {

    @Schema(description = "群组主键 id")
    private Long groupId;

    @Schema(description = "头像地址")
    private String avatarUrl;

    @Schema(description = "群组名称")
    private String name;

    @Schema(description = "群聊的 uuid")
    private String uuid;

    @Schema(description = "群组简介")
    private String bio;

    @Schema(description = "群主用户昵称")
    private String groupBelongNickname;

    @Schema(description = "群主头像文件主键 id，后端用", hidden = true)
    private Long groupBelongAvatarFileId;

    @Schema(description = "群主用户头像url")
    private String groupBelongAvatarUrl;

}
