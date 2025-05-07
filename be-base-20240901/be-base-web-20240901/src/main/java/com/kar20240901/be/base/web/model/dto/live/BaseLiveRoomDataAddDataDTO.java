package com.kar20240901.be.base.web.model.dto.live;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BaseLiveRoomDataAddDataDTO {

    @NotNull
    @Schema(description = "实时房间主键 id")
    private Long roomId;

    @NotNull
    @Schema(description = "创建时间的时间戳，UTC+8")
    private Long createTs;

    @NotBlank
    @Schema(description = "媒体类型")
    private String mediaType;

}
