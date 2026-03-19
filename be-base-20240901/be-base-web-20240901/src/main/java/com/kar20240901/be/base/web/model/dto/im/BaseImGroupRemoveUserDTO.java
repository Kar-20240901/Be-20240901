package com.kar20240901.be.base.web.model.dto.im;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BaseImGroupRemoveUserDTO {

    @NotNull
    @Schema(description = "群组主键 id")
    private Long groupId;

    @NotEmpty
    @Schema(description = "用户主键 id集合")
    private Set<Long> userIdSet;

}
