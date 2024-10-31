package com.kar20240901.be.base.web.model.dto.base;

import com.kar20240901.be.base.web.model.constant.base.TempRegexConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignEmailSetWxDTO {

    @Min(1)
    @NotNull
    @Schema(description = "二维码 id")
    private Long qrCodeId;

    @Pattern(regexp = TempRegexConstant.CODE_6_REGEXP)
    @NotBlank
    @Schema(description = "邮箱验证码")
    private String emailCode;

}
