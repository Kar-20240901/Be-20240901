package com.kar20240901.be.base.web.model.vo.base;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LongObjectMapVO<T> {

    @Schema(description = "map对象")
    private Map<Long, T> map;

}
