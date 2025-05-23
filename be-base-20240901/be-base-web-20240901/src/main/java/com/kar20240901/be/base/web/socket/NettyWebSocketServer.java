package com.kar20240901.be.base.web.socket;

import com.kar20240901.be.base.web.configuration.base.BaseConfiguration;
import com.kar20240901.be.base.web.configuration.socket.NettyWebSocketProperties;
import com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper;
import com.kar20240901.be.base.web.model.configuration.socket.BaseSocketBaseProperties;
import com.kar20240901.be.base.web.model.configuration.socket.NettyWebSocketBeanPostProcessor;
import com.kar20240901.be.base.web.model.enums.socket.BaseSocketTypeEnum;
import com.kar20240901.be.base.web.service.socket.BaseSocketService;
import com.kar20240901.be.base.web.util.base.IdGeneratorUtil;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import com.kar20240901.be.base.web.util.base.MyThreadUtil;
import com.kar20240901.be.base.web.util.socket.SocketUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NettyWebSocketServer {

    private static NettyWebSocketProperties nettyWebSocketProperties;

    @Resource
    public void setNettyWebSocketProperties(NettyWebSocketProperties nettyWebSocketProperties) {
        NettyWebSocketServer.nettyWebSocketProperties = nettyWebSocketProperties;
    }

    private static NettyWebSocketServerHandler nettyWebSocketServerHandler;

    @Resource
    public void setNettyWebSocketServerHandler(NettyWebSocketServerHandler nettyWebSocketServerHandler) {
        NettyWebSocketServer.nettyWebSocketServerHandler = nettyWebSocketServerHandler;
    }

    private static MyThreadUtil myThreadUtil;

    @Resource
    public void setMyThreadUtil(MyThreadUtil myThreadUtil) {
        NettyWebSocketServer.myThreadUtil = myThreadUtil;
    }

    private static BaseSocketService baseSocketService;

    @Resource
    public void setBaseSocketService(BaseSocketService baseSocketService) {
        NettyWebSocketServer.baseSocketService = baseSocketService;
    }

    private static BaseConfiguration baseConfiguration;

    @Resource
    public void setBaseConfiguration(BaseConfiguration baseConfiguration) {
        NettyWebSocketServer.baseConfiguration = baseConfiguration;
    }

    private static IdGeneratorUtil idGeneratorUtil;

    @Resource
    public void setIdGeneratorUtil(IdGeneratorUtil idGeneratorUtil) {
        NettyWebSocketServer.idGeneratorUtil = idGeneratorUtil;
    }

    private static BaseSocketRefUserMapper baseSocketRefUserMapper;

    @Resource
    public void setBaseSocketRefUserMapper(BaseSocketRefUserMapper baseSocketRefUserMapper) {
        NettyWebSocketServer.baseSocketRefUserMapper = baseSocketRefUserMapper;
    }

    private static SocketUtil socketUtil;

    @Resource
    public void setSocketUtil(SocketUtil socketUtil) {
        NettyWebSocketServer.socketUtil = socketUtil;
    }

    private static BaseSocketBaseProperties baseSocketBaseProperties;

    @Resource
    public void setBaseSocketBaseProperties(BaseSocketBaseProperties baseSocketBaseProperties) {
        NettyWebSocketServer.baseSocketBaseProperties = baseSocketBaseProperties;
    }

    public static Long baseSocketServerId = null; // 备注：启动完成之后，这个属性才有值

    private static ChannelFuture channelFuture = null; // 备注：启动完成之后，这个属性才有值

    private static EventLoopGroup parentGroup = null; // 备注：启动完成之后，这个属性才有值

    private static EventLoopGroup childGroup = null; // 备注：启动完成之后，这个属性才有值

    /**
     * 重启 socket
     */
    public synchronized static void restart() {

        stop(false); // 关闭 socket

        start(); // 启动 socket

    }

    /**
     * 关闭 socket
     */
    public synchronized static void stop(boolean disableFlag) {

        if (channelFuture == null || parentGroup == null || childGroup == null) {
            return;
        }

        // 关闭 socket
        SocketUtil.closeSocket(channelFuture, parentGroup, childGroup, baseSocketServerId,
            NettyWebSocketServerHandler.USER_ID_CHANNEL_MAP, "NettyWebSocket", disableFlag, getConnectPort());

        channelFuture = null;
        parentGroup = null;
        childGroup = null;

    }

    @PostConstruct
    public void postConstruct() {

        MyThreadUtil.execute(() -> {

            // 启动 socket，备注：如果是本地启动，请配置：--be.socket.web-socket.scheme=ws:// --be.socket.web-socket.host=127.0.0.1
            start();

        });

    }

    @PreDestroy
    public void preDestroy() {

        // 关闭 socket
        stop(false);

    }

    /**
     * 实际启动占用的端口
     */
    public static int getRealPort() {

        return BaseConfiguration.port + 1;

    }

    /**
     * 连接时使用的端口，默认为启动占用的端口
     */
    public static int getConnectPort() {

        return MyEntityUtil.getNotNullInt(baseSocketBaseProperties.getPort(), getRealPort());

    }

    /**
     * 启动 socket
     */
    @SneakyThrows
    private synchronized static void start() {

        if (parentGroup != null) {
            return;
        }

        int port = getRealPort();

        parentGroup = new NioEventLoopGroup(nettyWebSocketProperties.getParentSize());

        childGroup = new NioEventLoopGroup(nettyWebSocketProperties.getChildSize());

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024); // 半连接池大小
        serverBootstrap.option(ChannelOption.SO_REUSEADDR, true); // 允许重复使用本地地址和端口
        serverBootstrap.childOption(ChannelOption.ALLOW_HALF_CLOSURE, false); // 一个连接的远端关闭时，本地端自动关闭

        serverBootstrap.group(parentGroup, childGroup) // 绑定线程池

            .channel(NioServerSocketChannel.class) // 指定使用的channel

            .localAddress(port) // 绑定监听端口

            .childHandler(new ChannelInitializer<SocketChannel>() { // 绑定客户端连接时候触发操作

                @Override
                protected void initChannel(SocketChannel ch) { // 绑定客户端连接时候触发操作

                    // webSocket协议本身是基于http协议的，所以这边也要使用http解编码器
                    ch.pipeline().addLast(new HttpServerCodec());

                    // 以块的方式来写的处理器
                    ch.pipeline().addLast(new ChunkedWriteHandler());

                    ch.pipeline().addLast(new HttpObjectAggregator(8192));

                    ch.pipeline().addLast(nettyWebSocketServerHandler);

                    ch.pipeline().addLast(
                        new WebSocketServerProtocolHandler(nettyWebSocketProperties.getPath(), null, true, 65536 * 10));

                }

            });

        channelFuture = serverBootstrap.bind().sync(); // 服务器同步创建绑定

        baseSocketServerId =
            SocketUtil.getBaseSocketServerId(getConnectPort(), nettyWebSocketProperties, BaseSocketTypeEnum.WEB_SOCKET);

        log.info("NettyWebSocket 启动完成：端口：{}，总接口个数：{}个", port,
            NettyWebSocketBeanPostProcessor.getMappingMapSize());

    }

}
