package com.kar20240901.be.base.web.model.dto.im;

import com.kar20240901.be.base.web.model.dto.base.MyPageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseImBlockGroupPageDTO extends MyPageDTO {

    @Schema(description = "群组主键 id")
    private Long groupId;

    @Schema(description = "用户主键 id")
    private Long userId;

    @Schema(description = "后端使用，前端传递无用", hidden = true)
    private Integer sourceType;

}
