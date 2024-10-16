package com.kar20240901.be.base.web.service.socket;

import com.kar20240901.be.base.web.model.dto.NotNullIdAndIntegerValue;
import java.util.Set;

public interface NettyWebSocketService {

    Set<String> getAllWebSocketUrl();

    String getWebSocketUrlById(NotNullIdAndIntegerValue notNullIdAndIntegerValue);

}
