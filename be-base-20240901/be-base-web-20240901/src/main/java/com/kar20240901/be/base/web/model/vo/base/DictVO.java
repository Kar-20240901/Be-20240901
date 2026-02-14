package com.kar20240901.be.base.web.model.vo.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictVO {

    @Schema(description = "传值用")
    private Long id;

    @Schema(description = "显示用")
    private String name;

    @Schema(description = "字符串-1")
    private String str1;

    @Schema(description = "字符串-2")
    private String str2;

    @Schema(description = "Long-1")
    private Long l1;

    @Schema(description = "Long-2")
    private Long l2;

    public DictVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
