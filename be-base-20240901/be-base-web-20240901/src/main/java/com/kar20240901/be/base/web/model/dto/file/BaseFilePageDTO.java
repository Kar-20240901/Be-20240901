package com.kar20240901.be.base.web.model.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseFilePageDTO extends BaseFilePageSelfDTO {

    @Schema(description = "归属者用户主键 id（拥有全部权限）")
    private Long belongId;

}
