package com.kar20240901.be.base.web.model.dto.im;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BaseImSearchBaseDTO {

    @NotBlank
    @Schema(description = "搜索内容")
    private String searchKey;

    @Schema(description = "是否搜索好友，默认：true")
    private Boolean searchFriendFlag;

    @Schema(description = "是否搜索群组，默认：true")
    private Boolean searchGroupFlag;

    @Schema(description = "是否搜索聊天记录，默认：true")
    private Boolean searchContentFlag;

}
