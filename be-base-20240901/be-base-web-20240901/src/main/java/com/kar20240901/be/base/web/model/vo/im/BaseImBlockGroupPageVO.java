package com.kar20240901.be.base.web.model.vo.im;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@Data
public class BaseImBlockGroupPageVO {

    @Schema(description = "用户主键 id")
    private Long userId;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "拉黑时间")
    private Date createTime;

    @Schema(description = "头像文件主键 id，后端用", hidden = true)
    private Long avatarFileId;

    @Schema(description = "头像地址")
    private String avatarUrl;

}
