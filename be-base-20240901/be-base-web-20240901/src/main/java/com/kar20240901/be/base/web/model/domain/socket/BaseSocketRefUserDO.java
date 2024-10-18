package com.kar20240901.be.base.web.model.domain.socket;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.enums.base.BaseRequestCategoryEnum;
import com.kar20240901.be.base.web.model.enums.socket.BaseSocketOnlineTypeEnum;
import com.kar20240901.be.base.web.model.enums.socket.BaseSocketTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_socket_ref_user")
@Data
@Schema(description = "关联表：socket，用户")
public class BaseSocketRefUserDO extends TempEntity {

    @Schema(description = "用户主键 id")
    private Long userId;

    @Schema(description = "socket主键 id")
    private Long socketId;

    @Schema(description = "冗余字段，用户昵称")
    private String nickname;

    @Schema(description = "冗余字段，协议")
    private String scheme;

    @Schema(description = "冗余字段，主机")
    private String host;

    @Schema(description = "冗余字段，端口")
    private Integer port;

    @Schema(description = "冗余字段，路径")
    private String path;

    @Schema(description = "冗余字段，mac地址")
    private String macAddress;

    @Schema(description = "冗余字段，socket类型")
    private BaseSocketTypeEnum type;

    @Schema(description = "socket 在线状态")
    private BaseSocketOnlineTypeEnum onlineType;

    @Schema(description = "ip")
    private String ip;

    @Schema(description = "Ip2RegionUtil.getRegion() 获取到的 ip所处区域")
    private String region;

    @Schema(description = "请求类别")
    private BaseRequestCategoryEnum category;

}
