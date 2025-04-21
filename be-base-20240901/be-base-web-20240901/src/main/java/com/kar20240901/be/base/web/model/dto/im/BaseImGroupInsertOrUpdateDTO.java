package com.kar20240901.be.base.web.model.dto.im;

import com.kar20240901.be.base.web.model.dto.base.TempInsertOrUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseImGroupInsertOrUpdateDTO extends TempInsertOrUpdateDTO {

    @NotBlank
    @Schema(description = "群组名称")
    private String name;

}
