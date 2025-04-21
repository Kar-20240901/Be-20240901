package com.kar20240901.be.base.web.model.dto.im;

import com.kar20240901.be.base.web.model.dto.base.MyPageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseImSessionRefUserPageDTO extends MyPageDTO {

    @Schema(description = "是否查询：未读消息数量，最新一条消息，默认：false")
    private Boolean queryContentFlag;

}
