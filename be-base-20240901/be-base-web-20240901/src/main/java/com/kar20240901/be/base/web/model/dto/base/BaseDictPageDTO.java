package com.kar20240901.be.base.web.model.dto.base;

import com.kar20240901.be.base.web.model.enums.base.BaseDictTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseDictPageDTO extends MyPageDTO {

    @Schema(description = "字典 key（不能重复），备注：字典项要冗余这个 key，目的：方便操作")
    private String dictKey;

    @Schema(description = "字典/字典项 名")
    private String name;

    @Schema(description = "字典类型")
    private BaseDictTypeEnum type;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "是否启用")
    private Boolean enableFlag;

    @Schema(description = "字典项 value（数字 123...）备注：字典为 -1")
    private Integer value;

}
