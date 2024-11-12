package com.kar20240901.be.base.web.model.dto.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseApiTokenPageDTO extends MyPageDTO {

    @Schema(description = "名称")
    private String name;

    @Schema(description = "用户主键id")
    private Long userId;

}
