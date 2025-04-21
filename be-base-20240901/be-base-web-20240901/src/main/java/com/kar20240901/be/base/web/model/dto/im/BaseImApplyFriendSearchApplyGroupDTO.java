package com.kar20240901.be.base.web.model.dto.im;

import com.kar20240901.be.base.web.model.dto.base.MyPageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseImApplyFriendSearchApplyGroupDTO extends MyPageDTO {

    @Schema(description = "群组主键 id")
    private Long groupId;

    @Schema(description = "群组名称")
    private String name;

}
