package com.kar20240901.be.base.web.model.bo.socket;

import com.kar20240901.be.base.web.model.dto.socket.WebSocketMessageDTO;
import java.util.Set;
import lombok.Data;

/**
 * webSocket二进制事件的 bo
 */
@Data
public class BaseWebSocketByteEventBO<T> {

    /**
     * 用户主键 idSet，如果为 null，则给所有在线的用户发，为空为空集合，则不发送任何消息
     */
    private Set<Long> userIdSet;

    /**
     * socket关联用户主键 idSet
     */
    private Set<Long> baseSocketRefUserIdSet;

    /**
     * 传输的数据，如果为 null，则不会发送任何消息
     */
    private WebSocketMessageDTO<T> webSocketMessageDTO;

}
