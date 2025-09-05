package com.kar20240901.be.base.web.model.dto.im;

import com.kar20240901.be.base.web.model.dto.base.MyPageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseImSessionRefUserPageDTO extends MyPageDTO {

    @Schema(description = "是否查询：未读消息数量，最新一条消息，默认：false")
    private Boolean queryContentFlag;

    @Schema(description = "会话主键 id")
    private Long sessionId;

    @Schema(description = "是否向后查询，默认：false 根据 id，往前查询 true 根据 id，往后查询", hidden = true)
    private Boolean backwardFlag;

    @Schema(description = "搜索内容：会话名")
    private String searchKey;

    @Schema(description = "会话主键 id集合")
    private Set<Long> sessionIdSet;

}
