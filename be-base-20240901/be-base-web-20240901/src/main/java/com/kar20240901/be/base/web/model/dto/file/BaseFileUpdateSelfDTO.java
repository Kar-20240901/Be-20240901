package com.kar20240901.be.base.web.model.dto.file;

import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseFileUpdateSelfDTO extends NotEmptyIdSet {

    @Schema(description = "文件名")
    private String fileName;

}
