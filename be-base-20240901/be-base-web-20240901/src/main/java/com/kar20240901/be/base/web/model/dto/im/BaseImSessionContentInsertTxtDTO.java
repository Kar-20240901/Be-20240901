package com.kar20240901.be.base.web.model.dto.im;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "创建时间的时间戳，UTC+8")
    private Long createTs;

    @Schema(description = "引用的内容主键 id，不引用时为 -1")
    private Long refId;

    @Schema(description = "排序号（值越大越前面，默认为 0）")
    private Integer orderNo;

}
