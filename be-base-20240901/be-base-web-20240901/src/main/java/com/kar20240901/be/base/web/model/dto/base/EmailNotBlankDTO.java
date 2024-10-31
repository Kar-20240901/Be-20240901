package com.kar20240901.be.base.web.model.dto.base;

import com.kar20240901.be.base.web.model.constant.base.TempRegexConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class EmailNotBlankDTO {

    @Size(max = 200)
    @NotBlank
    @Pattern(regexp = TempRegexConstant.EMAIL)
    @Schema(description = "邮箱")
    private String email;

}
