package com.kar20240901.be.base.web.util.socket;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import com.kar20240901.be.base.web.configuration.base.BaseConfiguration;
import com.kar20240901.be.base.web.model.configuration.socket.BaseSocketBaseProperties;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.socket.BaseSocketDO;
import com.kar20240901.be.base.web.model.domain.socket.BaseSocketRefUserDO;
import com.kar20240901.be.base.web.model.enums.socket.BaseSocketTypeEnum;
import com.kar20240901.be.base.web.service.socket.BaseSocketRefUserService;
import com.kar20240901.be.base.web.service.socket.BaseSocketService;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import com.kar20240901.be.base.web.util.base.RequestUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SocketUtil {

    private static BaseSocketService baseSocketService;

    @Resource
    public void setBaseSocketService(BaseSocketService baseSocketService) {
        SocketUtil.baseSocketService = baseSocketService;
    }

    private static BaseSocketRefUserService baseSocketRefUserService;

    @Resource
    public void setBaseSocketRefUserService(BaseSocketRefUserService baseSocketRefUserService) {
        SocketUtil.baseSocketRefUserService = baseSocketRefUserService;
    }

    /**
     * 获取：ip
     */
    public static String getIp(Channel channel) {

        InetSocketAddress inetSocketAddress = (InetSocketAddress)channel.remoteAddress();

        return inetSocketAddress.getAddress().getHostAddress();

    }

    /**
     * 获取：ip
     */
    @NotNull
    public static String getIp(FullHttpRequest fullHttpRequest, Channel channel) {

        String ip = "";

        for (String item : RequestUtil.IP_HEADER_ARR) {

            ip = fullHttpRequest.headers().get(item);

            if (NetUtil.isUnknown(ip) == false) {

                return NetUtil.getMultistageReverseProxyIp(ip);

            }

        }

        if (StrUtil.isBlank(ip)) {

            ip = getIp(channel);

        }

        if (StrUtil.isBlank(ip)) {

            ip = "";

        }

        return ip;

    }

    /**
     * 关闭 socket
     *
     * @param disableFlag 是否是禁用，即：不删除数据库里面的数据
     */
    public static void closeSocket(ChannelFuture channelFuture, EventLoopGroup parentGroup, EventLoopGroup childGroup,
        Long baseSocketServerId, ConcurrentHashMap<Long, ConcurrentHashMap<Long, Channel>> userIdChannelMap,
        String name, boolean disableFlag, int port) {

        long closeChannelCount = 0;

        for (ConcurrentHashMap<Long, Channel> item : userIdChannelMap.values()) {

            for (Channel subItem : item.values()) {

                subItem.close();

                closeChannelCount++;

            }

        }

        boolean removeFlag = false;

        if (baseSocketServerId != null) {

            if (disableFlag) {

                removeFlag = true;

            } else {

                removeFlag = baseSocketService.removeById(baseSocketServerId);

            }

        }

        // 移除：相关的用户连接数据
        baseSocketRefUserService.lambdaUpdate()
            .eq(baseSocketServerId != null, BaseSocketRefUserDO::getSocketId, baseSocketServerId)
            .or(i -> i.eq(BaseSocketRefUserDO::getMacAddress, BaseConfiguration.MAC_ADDRESS)
                .eq(BaseSocketRefUserDO::getPort, port)).remove();

        log.info("{}，id：{}，mac：{}，port：{}，下线{}，移除连接：{}", name, baseSocketServerId, BaseConfiguration.MAC_ADDRESS,
            port, removeFlag ? "成功" : "失败", closeChannelCount);

        if (channelFuture != null) {

            channelFuture.channel().close().syncUninterruptibly();

        }

        if (parentGroup != null) {

            parentGroup.shutdownGracefully().syncUninterruptibly(); // 释放线程池资源

        }

        if (childGroup != null) {

            childGroup.shutdownGracefully().syncUninterruptibly(); // 释放线程池资源

        }

    }

    /**
     * 获取：baseSocketServerId
     */
    public static Long getBaseSocketServerId(int port, BaseSocketBaseProperties baseSocketBaseProperties,
        BaseSocketTypeEnum baseSocketTypeEnum) {

        BaseSocketDO baseSocketDO = new BaseSocketDO();

        baseSocketDO.setScheme(MyEntityUtil.getNotNullStr(baseSocketBaseProperties.getScheme()));
        baseSocketDO.setHost(MyEntityUtil.getNotNullStr(baseSocketBaseProperties.getHost()));
        baseSocketDO.setPort(port);
        baseSocketDO.setPath(MyEntityUtil.getNotNullStr(baseSocketBaseProperties.getPath()));
        baseSocketDO.setType(baseSocketTypeEnum);

        baseSocketDO.setMacAddress(BaseConfiguration.MAC_ADDRESS);

        baseSocketDO.setEnableFlag(true);
        baseSocketDO.setRemark("");

        // 移除：mac地址，port，相同的 socket数据
        List<BaseSocketDO> baseSocketDoList =
            baseSocketService.lambdaQuery().eq(BaseSocketDO::getMacAddress, baseSocketDO.getMacAddress())
                .eq(BaseSocketDO::getPort, baseSocketDO.getPort()).select(TempEntity::getId).list();

        Set<Long> socketIdSet = null;

        if (CollUtil.isNotEmpty(baseSocketDoList)) {

            socketIdSet = baseSocketDoList.stream().map(TempEntity::getId).collect(Collectors.toSet());

            baseSocketService.removeBatchByIds(socketIdSet);

        }

        baseSocketRefUserService.lambdaUpdate()
            .in(CollUtil.isNotEmpty(socketIdSet), BaseSocketRefUserDO::getSocketId, socketIdSet)
            .or(i -> i.eq(BaseSocketRefUserDO::getMacAddress, BaseConfiguration.MAC_ADDRESS)
                .eq(BaseSocketRefUserDO::getPort, port)).remove();

        baseSocketService.save(baseSocketDO);

        return baseSocketDO.getId();

    }

}
