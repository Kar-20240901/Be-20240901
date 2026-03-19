package com.kar20240901.be.base.web.model.dto.im;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BaseImGroupRefUserDeleteMuteDTO {

    @NotNull
    @Schema(description = "群组主键 id")
    private Long groupId;

    @Schema(description = "需要解除禁言的用户主键 id集合")
    private Set<Long> userIdSet;

}
