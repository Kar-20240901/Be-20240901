package com.kar20240901.be.base.web.model.dto.im;

import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseImApplyFriendRejectDTO extends NotNullId {

    @Schema(description = "拒绝理由")
    private String rejectReason;

}
