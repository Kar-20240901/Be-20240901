package com.kar20240901.be.base.web.model.bo.socket;

import com.kar20240901.be.base.web.model.dto.socket.WebSocketMessageDTO;
import java.util.Set;
import lombok.Data;

/**
 * webSocket事件的 bo
 */
@Data
public class BaseWebSocketEventBO<T> {

    /**
     * 用户主键 idSet
     */
    private Set<Long> userIdSet;

    /**
     * socket关联用户主键 idSet
     */
    private Set<Long> baseSocketRefUserIdSet;

    /**
     * 传输的数据
     */
    private WebSocketMessageDTO<T> webSocketMessageDTO;

}
