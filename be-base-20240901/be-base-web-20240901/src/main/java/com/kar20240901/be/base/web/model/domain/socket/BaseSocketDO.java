package com.kar20240901.be.base.web.model.domain.socket;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240901.be.base.web.model.domain.TempEntity;
import com.kar20240901.be.base.web.model.enums.socket.BaseSocketTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_socket")
@Data
@Schema(description = "主表：socket")
public class BaseSocketDO extends TempEntity {

    @Schema(description = "协议：例如：ws://，wss:// 等")
    private String scheme;

    @Schema(description = "主机")
    private String host;

    @Schema(description = "端口")
    private Integer port;

    @Schema(description = "路径，备注：以 / 开头")
    private String path;

    @Schema(description = "socket类型")
    private BaseSocketTypeEnum type;

    @Schema(description = "mac地址，用于：和 port一起判断是否是重复启动，如果是，则需要移除之前的 socket信息")
    private String macAddress;

}
