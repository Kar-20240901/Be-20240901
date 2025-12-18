package com.kar20240901.be.base.web.model.bo.base;

import com.kar20240901.be.base.web.model.interfaces.base.IBaseDeleteLocalCacheType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseDeleteLocalCacheBO {

    @Schema(description = "类型")
    private IBaseDeleteLocalCacheType type;

    @Schema(description = "关联的 id集合")
    private Set<Long> refIdSet;

}
