package com.kar20240901.be.base.web.model.dto.live;

import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseLiveRoomUserAddUserDTO extends NotNullId {

    @Schema(description = "房间验证码")
    private String code;

}
