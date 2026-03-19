package com.kar20240901.be.base.web.model.dto.im;

import com.kar20240901.be.base.web.model.dto.base.MyPageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseImSessionContentRefUserPageDTO extends MyPageDTO {

    @NotNull
    @Schema(description = "会话主键 id")
    private Long sessionId;

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "消息内容主键 id")
    private Long contentId;

    @Schema(description = "是否向后查询，默认：false 根据 id，往前查询 true 根据 id，往后查询", hidden = true)
    private Boolean backwardFlag;

}
