package com.kar20240901.be.base.web.model.dto.live;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @NotNull
    @Schema(description = "数据")
    private Byte[] data;

    @NotNull
    @Schema(description = "时间，单位：毫秒")
    private Integer ms;

}
