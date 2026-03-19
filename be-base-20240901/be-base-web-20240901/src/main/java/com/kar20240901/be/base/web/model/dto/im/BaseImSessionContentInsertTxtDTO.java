package com.kar20240901.be.base.web.model.dto.im;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BaseImSessionContentInsertTxtDTO {

    @NotNull
    @Schema(description = "会话主键 id")
    private Long sessionId;

    @NotNull
    @Schema(description = "文字内容")
    private String txt;

    @NotNull
    @Schema(description = "创建时间的时间戳，UTC+8")
    private Long createTs;

    @Schema(description = "引用的内容主键 id，不引用时为 -1")
    private Long refId;

    @Schema(description = "排序号（值越大越前面，默认为 0）")
    private Integer orderNo;

    @Schema(description = "创建者用户主键 id，备注：后端用", hidden = true)
    private Long createId;

    @Schema(description = "开启了免打扰的用户主键 id集合，备注：后端用", hidden = true)
    private Set<Long> notDisturbFlagUserIdSet;

    @Schema(description = "消息主键 id，备注：后端用", hidden = true)
    private Long contentId;

    @NotNull
    @Schema(description = "消息类型")
    private Integer type;

}
