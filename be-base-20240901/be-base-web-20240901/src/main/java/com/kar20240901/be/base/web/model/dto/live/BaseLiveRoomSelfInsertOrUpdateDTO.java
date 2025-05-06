package com.kar20240901.be.base.web.model.dto.live;

import com.kar20240901.be.base.web.model.dto.base.TempInsertOrUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseLiveRoomSelfInsertOrUpdateDTO extends TempInsertOrUpdateDTO {

    @NotBlank
    @Schema(description = "实时房间名")
    private String name;

    @Schema(description = "是否有房间验证码，默认：true，备注：只有在新增的时候才有用")
    private Boolean codeFlag;

}
