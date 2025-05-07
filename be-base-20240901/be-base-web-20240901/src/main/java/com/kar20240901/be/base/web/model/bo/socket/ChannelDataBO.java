package com.kar20240901.be.base.web.model.bo.socket;

import com.kar20240901.be.base.web.model.enums.base.BaseRequestCategoryEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ChannelDataBO {

    @Schema(description = "用户主键 id")
    private Long userId;

    @Schema(description = "套接字关联用户的关联 id")
    private Long socketRefUserId;

    @Schema(description = "用户终端类型")
    private BaseRequestCategoryEnum category;

    @Schema(description = "用户 ip")
    private String ip;

    @Schema(description = "二进制数据，即：blob格式数据")
    private byte[] byteArr;

}
