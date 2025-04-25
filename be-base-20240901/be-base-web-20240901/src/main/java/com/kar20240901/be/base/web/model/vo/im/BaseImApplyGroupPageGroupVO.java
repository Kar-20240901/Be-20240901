package com.kar20240901.be.base.web.model.vo.im;

import com.kar20240901.be.base.web.model.enums.im.BaseImApplyStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@Data
public class BaseImApplyGroupPageGroupVO {

    @Schema(description = "主键 id")
    private Long id;

    @Schema(description = "状态：101 申请中 201 已通过 301 已拒绝")
    private BaseImApplyStatusEnum status;

    @Schema(description = "申请时间")
    private Date applyTime;

    @Schema(description = "申请内容")
    private String applyContent;

    @Schema(description = "目标用户昵称")
    private String nickname;

    @Schema(description = "目标用户头像文件 id")
    private Long avatarFileId;

    @Schema(description = "目标用户头像")
    private String avatarUrl;

}
