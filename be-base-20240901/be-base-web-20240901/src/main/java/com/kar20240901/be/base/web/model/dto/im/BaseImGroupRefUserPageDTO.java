package com.kar20240901.be.base.web.model.dto.im;

import com.kar20240901.be.base.web.model.dto.base.MyPageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseImGroupRefUserPageDTO extends MyPageDTO {

    @NotNull
    @Schema(description = "群组主键 id")
    private Long groupId;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户主键 id")
    private Long userId;

}
