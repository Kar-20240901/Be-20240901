package com.kar20240901.be.base.web.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictStringVO {

    @Schema(description = "传值用")
    private String id;

    @Schema(description = "显示用")
    private String name;

}
