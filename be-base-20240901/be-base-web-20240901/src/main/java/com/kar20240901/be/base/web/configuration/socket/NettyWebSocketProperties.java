package com.kar20240901.be.base.web.configuration.socket;

import com.kar20240901.be.base.web.model.configuration.socket.BaseSocketBaseProperties;
import com.kar20240901.be.base.web.model.constant.PropertiesPrefixConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@Data
@Component
@ConfigurationProperties(prefix = PropertiesPrefixConstant.SOCKET_WEB_SOCKET)
public class NettyWebSocketProperties extends BaseSocketBaseProperties {

}
