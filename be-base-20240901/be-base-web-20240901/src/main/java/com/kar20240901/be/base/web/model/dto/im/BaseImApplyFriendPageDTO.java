package com.kar20240901.be.base.web.model.dto.im;

import com.kar20240901.be.base.web.model.dto.base.MyPageDTO;
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

}
