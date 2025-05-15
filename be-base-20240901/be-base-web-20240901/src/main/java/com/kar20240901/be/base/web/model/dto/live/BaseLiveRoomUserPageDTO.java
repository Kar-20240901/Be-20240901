package com.kar20240901.be.base.web.model.dto.live;

import com.kar20240901.be.base.web.model.dto.base.MyPageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseLiveRoomUserPageDTO extends MyPageDTO {

    @NotNull
    @Schema(description = "房间主键 id")
    private Long roomId;

    @Schema(description = "用户主键id")
    private Long userId;

    @Schema(description = "搜索关键字")
    private String searchKey;

}
