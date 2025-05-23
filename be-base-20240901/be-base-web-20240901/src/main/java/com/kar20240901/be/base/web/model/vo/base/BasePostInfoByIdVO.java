package com.kar20240901.be.base.web.model.vo.base;

import com.kar20240901.be.base.web.model.domain.base.BasePostDO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BasePostInfoByIdVO extends BasePostDO {

    @Schema(description = "用户主键idSet")
    private Set<Long> userIdSet;

}
