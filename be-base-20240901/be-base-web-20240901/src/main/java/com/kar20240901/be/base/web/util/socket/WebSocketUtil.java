package com.kar20240901.be.base.web.util.socket;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kar20240901.be.base.web.model.bo.socket.BaseWebSocketEventBO;
import com.kar20240901.be.base.web.model.configuration.socket.NettyWebSocketBeanPostProcessor;
import com.kar20240901.be.base.web.model.dto.socket.WebSocketMessageDTO;
import com.kar20240901.be.base.web.server.NettyWebSocketServerHandler;
import com.kar20240901.be.base.web.util.MyUserInfoUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.swagger.v3.oas.annotations.Operation;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

@Component
public class WebSocketUtil {

    // 目的：Long 转 String，Enum 转 code
    private static ObjectMapper objectMapper;

    @Resource
    public void setObjectMapper(ObjectMapper objectMapper) {
        WebSocketUtil.objectMapper = objectMapper;
    }

    /**
     * 发送消息
     */
    @SneakyThrows
    public static void send(@Nullable BaseWebSocketEventBO<?> bo) {

        if (bo == null) {
            return;
        }

        Set<Long> userIdSet = bo.getUserIdSet();

        if (CollUtil.isEmpty(userIdSet) || bo.getWebSocketMessageDTO() == null) {
            return;
        }

        String jsonStr = objectMapper.writeValueAsString(bo.getWebSocketMessageDTO());

        Set<Long> sysSocketRefUserIdSet = bo.getSysSocketRefUserIdSet();

        boolean checkFlag = CollUtil.isNotEmpty(sysSocketRefUserIdSet);

        for (Long item : userIdSet) {

            ConcurrentHashMap<Long, Channel> channelMap = NettyWebSocketServerHandler.USER_ID_CHANNEL_MAP.get(item);

            if (CollUtil.isEmpty(channelMap)) {
                continue;
            }

            // 再包一层：防止遍历的时候，集合被修改
            List<Channel> channelList = new ArrayList<>(channelMap.values());

            for (Channel subItem : channelList) {

                if (checkFlag) {

                    Long sysSocketRefUserId =
                        subItem.attr(NettyWebSocketServerHandler.BASE_SOCKET_REF_USER_ID_KEY).get();

                    if (!sysSocketRefUserIdSet.contains(sysSocketRefUserId)) {
                        continue;
                    }

                }

                // 发送数据
                subItem.writeAndFlush(new TextWebSocketFrame(jsonStr));

            }

        }

    }

    /**
     * 发送消息
     */
    @SneakyThrows
    public static <T> void send(Channel channel, WebSocketMessageDTO<T> dto, String text, long costMs,
        @Nullable NettyWebSocketBeanPostProcessor.MappingValue mappingValue, String errorMsg, boolean successFlag) {

        Long userId = channel.attr(NettyWebSocketServerHandler.USER_ID_KEY).get();

        Date date = new Date();

        costMs = System.currentTimeMillis() - costMs; // 耗时（毫秒）

        String summary;

        if (mappingValue == null) {

            summary = "";

        } else {

            Operation operation = mappingValue.getMethod().getAnnotation(Operation.class);

            summary = operation.summary();

        }

        String jsonStr = objectMapper.writeValueAsString(dto);

        String ip = channel.attr(NettyWebSocketServerHandler.IP_KEY).get();

        MyUserInfoUtil.updateUserInfo(userId, date, ip);

        // 发送数据
        channel.writeAndFlush(new TextWebSocketFrame(jsonStr));

    }

}
