package com.kar20240901.be.base.web.util.socket;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kar20240901.be.base.web.model.bo.socket.BaseWebSocketEventBO;
import com.kar20240901.be.base.web.model.configuration.socket.NettyWebSocketBeanPostProcessor;
import com.kar20240901.be.base.web.model.constant.base.OperationDescriptionConstant;
import com.kar20240901.be.base.web.model.domain.request.BaseRequestDO;
import com.kar20240901.be.base.web.model.domain.request.BaseRequestInfoDO;
import com.kar20240901.be.base.web.model.dto.socket.WebSocketMessageDTO;
import com.kar20240901.be.base.web.server.NettyWebSocketServerHandler;
import com.kar20240901.be.base.web.util.base.IdGeneratorUtil;
import com.kar20240901.be.base.web.util.base.Ip2RegionUtil;
import com.kar20240901.be.base.web.util.base.MyUserInfoUtil;
import com.kar20240901.be.base.web.util.base.RequestUtil;
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

        Set<Long> baseSocketRefUserIdSet = bo.getBaseSocketRefUserIdSet();

        boolean checkFlag = CollUtil.isNotEmpty(baseSocketRefUserIdSet);

        for (Long item : userIdSet) {

            ConcurrentHashMap<Long, Channel> channelMap = NettyWebSocketServerHandler.USER_ID_CHANNEL_MAP.get(item);

            if (CollUtil.isEmpty(channelMap)) {
                continue;
            }

            // 再包一层：防止遍历的时候，集合被修改
            List<Channel> channelList = new ArrayList<>(channelMap.values());

            for (Channel subItem : channelList) {

                if (checkFlag) {

                    Long baseSocketRefUserId =
                        subItem.attr(NettyWebSocketServerHandler.BASE_SOCKET_REF_USER_ID_KEY).get();

                    if (!baseSocketRefUserIdSet.contains(baseSocketRefUserId)) {
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

        BaseRequestDO baseRequestDO = new BaseRequestDO();

        BaseRequestInfoDO baseRequestInfoDO = new BaseRequestInfoDO();

        Long id = IdGeneratorUtil.nextId();

        baseRequestDO.setId(id);
        baseRequestInfoDO.setId(id);

        baseRequestDO.setUri(dto.getUri());
        baseRequestDO.setCostMs(costMs);
        baseRequestDO.setName(summary);
        baseRequestDO.setCategory(channel.attr(NettyWebSocketServerHandler.BASE_REQUEST_CATEGORY_ENUM_KEY).get());

        String ip = channel.attr(NettyWebSocketServerHandler.IP_KEY).get();

        baseRequestDO.setIp(ip);
        baseRequestDO.setRegion(Ip2RegionUtil.getRegion(baseRequestDO.getIp()));

        // 更新：用户信息
        MyUserInfoUtil.add(userId, date, ip, baseRequestDO.getRegion());

        baseRequestDO.setSuccessFlag(successFlag);
        baseRequestDO.setType(OperationDescriptionConstant.WEB_SOCKET);

        baseRequestDO.setCreateId(userId);
        baseRequestDO.setCreateTime(date);

        baseRequestInfoDO.setErrorMsg(errorMsg);
        baseRequestInfoDO.setRequestParam(text);
        baseRequestInfoDO.setResponseValue(jsonStr);

        // 添加一个：请求数据
        RequestUtil.add(baseRequestDO, baseRequestInfoDO);

        // 发送数据
        channel.writeAndFlush(new TextWebSocketFrame(jsonStr));

    }

}
