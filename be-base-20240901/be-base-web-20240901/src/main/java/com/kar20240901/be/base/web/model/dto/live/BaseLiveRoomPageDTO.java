package com.kar20240901.be.base.web.model.dto.live;

import com.kar20240901.be.base.web.model.dto.base.MyPageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseLiveRoomPageDTO extends MyPageDTO {

    @Schema(description = "房间名")
    public String name;

    @Schema(description = "归属用户主键 id")
    private Long belongId;

}
