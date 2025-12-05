package com.kar20240901.be.base.web.model.dto.im;

import com.kar20240901.be.base.web.model.dto.base.MyPageDTO;
import com.kar20240901.be.base.web.model.enums.im.BaseImApplyStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseImApplyFriendPageDTO extends MyPageDTO {

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "申请内容")
    private String applyContent;

    @Schema(description = "搜索关键字")
    private String searchKey;

    @Schema(description = "true 查询对我的申请（默认），false 查询我发起的申请")
    private Boolean toMeFlag;

    @Schema(description = "后端查询用", hidden = true)
    private BaseImApplyStatusEnum statusTemp;

}
