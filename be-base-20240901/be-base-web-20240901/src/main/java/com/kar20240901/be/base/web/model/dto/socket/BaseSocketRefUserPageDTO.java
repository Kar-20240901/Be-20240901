package com.kar20240901.be.base.web.model.dto.socket;

import com.kar20240901.be.base.web.model.dto.MyPageDTO;
import com.kar20240901.be.base.web.model.enums.socket.BaseSocketOnlineTypeEnum;
import com.kar20240901.be.base.web.model.enums.socket.BaseSocketTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseSocketRefUserPageDTO extends MyPageDTO {

    @Schema(description = "主键 id")
    private Long id;

    @Schema(description = "用户主键 id")
    private Long userId;

    @Schema(description = "socket主键 id")
    private Long socketId;

    @Schema(description = "协议")
    private String scheme;

    @Schema(description = "主机")
    private String host;

    @Schema(description = "端口")
    private Integer port;

    @Schema(description = "socket类型")
    private BaseSocketTypeEnum type;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "socket 在线状态")
    private BaseSocketOnlineTypeEnum onlineType;

    @Schema(description = "ip")
    private String ip;

    @Schema(description = "Ip2RegionUtil.getRegion() 获取到的 ip所处区域")
    private String region;

}
