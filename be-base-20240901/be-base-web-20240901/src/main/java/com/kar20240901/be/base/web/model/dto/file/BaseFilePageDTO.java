package com.kar20240901.be.base.web.model.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseFilePageDTO extends BaseFilePageSelfDTO {

    @Schema(description = "归属者用户主键 id，只用于删除操作")
    private Long belongId;

    @Schema(description = "是否向后查询，默认：false 根据 id，往前查询 true 根据 id，往后查询", hidden = true)
    private Boolean backwardFlag;

    @Schema(description = "滚动查询的主键 id", hidden = true)
    private Long scrollId;

}
