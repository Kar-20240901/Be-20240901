package com.kar20240901.be.base.web.model.vo;

import com.kar20240901.be.base.web.model.domain.BaseDeptDO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseDeptInfoByIdVO extends BaseDeptDO {

    @Schema(description = "用户主键idSet")
    private Set<Long> userIdSet;

}
