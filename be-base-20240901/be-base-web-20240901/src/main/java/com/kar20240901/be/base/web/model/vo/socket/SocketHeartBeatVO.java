package com.kar20240901.be.base.web.model.vo.socket;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SocketHeartBeatVO {

    @Schema(description = "套接字主键 id")
    private Long socketId;

    @Schema(description = "套接字关联用户的关联 id")
    private Long socketRefUserId;

}
