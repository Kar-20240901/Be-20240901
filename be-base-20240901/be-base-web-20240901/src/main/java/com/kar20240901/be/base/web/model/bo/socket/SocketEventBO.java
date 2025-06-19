package com.kar20240901.be.base.web.model.bo.socket;

import com.kar20240901.be.base.web.model.enums.base.BaseRequestCategoryEnum;
import io.netty.channel.Channel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SocketEventBO {

    @Schema(description = "用户主键 id")
    private Long userId;

    @Schema(description = "套接字关联用户的关联 id")
    private Long socketRefUserId;

    @Schema(description = "用户终端类型")
    private BaseRequestCategoryEnum category;

    @Schema(description = "用户 ip")
    private String ip;

    @Schema(description = "通道，备注：建议只进行参数绑定和获取")
    private Channel channel;

}
