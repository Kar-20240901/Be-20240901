package com.kar20240901.be.base.web.model.dto.im;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import lombok.Data;

@Data
public class BaseImSessionRefUserAddNotDisturbDTO {

    @Schema(description = "会话主键 id集合")
    private Set<Long> sessionIdSet;

    @Schema(description = "好友用户主键 id集合")
    private Set<Long> friendUserIdSet;

    @Schema(description = "群组主键 id集合")
    private Set<Long> groupIdSet;

}
