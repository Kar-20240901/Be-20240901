package com.kar20240901.be.base.web.model.dto.file;

import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseFileMoveSelfDTO extends NotEmptyIdSet {

    @NotNull
    @Schema(description = "父节点id（顶级则为0）")
    private Long pid;

}
