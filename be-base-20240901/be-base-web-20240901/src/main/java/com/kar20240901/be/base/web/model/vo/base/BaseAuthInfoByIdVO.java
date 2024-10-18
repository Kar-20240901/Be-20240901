package com.kar20240901.be.base.web.model.vo.base;

import com.kar20240901.be.base.web.model.domain.base.BaseAuthDO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseAuthInfoByIdVO extends BaseAuthDO {

    @Schema(description = "角色idSet")
    private Set<Long> roleIdSet;

}
