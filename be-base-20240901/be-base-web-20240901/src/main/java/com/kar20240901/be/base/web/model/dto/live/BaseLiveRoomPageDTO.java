package com.kar20240901.be.base.web.model.dto.live;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseLiveRoomPageDTO extends BaseLiveRoomSelfPageDTO {

    @Schema(description = "归属用户主键 id")
    private Long belongId;

}
