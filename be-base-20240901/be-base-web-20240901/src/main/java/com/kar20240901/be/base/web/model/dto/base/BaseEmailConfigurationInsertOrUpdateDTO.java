package com.kar20240901.be.base.web.model.dto.base;

import com.kar20240901.be.base.web.model.constant.base.TempRegexConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BaseEmailConfigurationInsertOrUpdateDTO {

    @NotBlank
    @Schema(description = "正文前缀")
    private String contentPre;

    @NotNull
    @Schema(description = "端口")
    private Integer port;

    @Pattern(regexp = TempRegexConstant.EMAIL)
    @NotBlank
    @Schema(description = "发送人邮箱")
    private String fromEmail;

    @Schema(description = "发送人密码，备注：如果为 null，则表示不修改，但是新增的时候，必须有值")
    private String pass;

    @NotNull
    @Schema(description = " 是否使用：SSL")
    private Boolean sslFlag;

}
