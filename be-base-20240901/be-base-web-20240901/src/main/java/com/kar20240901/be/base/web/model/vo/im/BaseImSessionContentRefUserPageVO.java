package com.kar20240901.be.base.web.model.vo.im;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BaseImSessionContentRefUserPageVO {

    @Schema(description = "主键 id")
    private Long id;

    @Schema(description = "创建人id")
    private Long createId;

    @Schema(description = "会话内容")
    private String content;

    @Schema(description = "内容类型")
    private Integer type;

    @Schema(description = "创建时间的时间戳，UTC+8")
    private Long createTs;

    @Schema(description = "引用的内容主键 id，不引用时为 -1")
    private Long refId;

}
