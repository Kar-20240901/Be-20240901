package com.kar20240901.be.base.web.model.vo.live;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@Data
public class BaseLiveRoomUserSelfPageVO {

    @Schema(description = "主键 id")
    private Long id;

    @Schema(description = "用户主键 id")
    private Long userId;

    @Schema(description = "套接字关联用户主键 id")
    private Long socketRefUserId;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "目标用户头像文件 id", hidden = true)
    private Long avatarFileId;

    @Schema(description = "用户头像 url")
    private String avatarUrl;

    @Schema(description = "第一个 blob，用于播放时，需要先传递该 blob")
    private String firstBlobStr;

}
