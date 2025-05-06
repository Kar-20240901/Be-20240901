package com.kar20240901.be.base.web.socket;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.func.VoidFunc0;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.net.url.UrlQuery;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.kar20240901.be.base.web.configuration.socket.NettyWebSocketProperties;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.exception.TempException;
import com.kar20240901.be.base.web.model.bo.socket.ChannelDataBO;
import com.kar20240901.be.base.web.model.configuration.socket.NettyWebSocketBeanPostProcessor;
import com.kar20240901.be.base.web.model.constant.base.OperationDescriptionConstant;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.domain.request.BaseRequestDO;
import com.kar20240901.be.base.web.model.domain.request.BaseRequestInfoDO;
import com.kar20240901.be.base.web.model.domain.socket.BaseSocketRefUserDO;
import com.kar20240901.be.base.web.model.dto.socket.WebSocketMessageDTO;
import com.kar20240901.be.base.web.model.enums.base.BaseRedisKeyEnum;
import com.kar20240901.be.base.web.model.enums.base.BaseRequestCategoryEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.socket.BaseSocketRefUserService;
import com.kar20240901.be.base.web.util.base.CallBack;
import com.kar20240901.be.base.web.util.base.IdGeneratorUtil;
import com.kar20240901.be.base.web.util.base.Ip2RegionUtil;
import com.kar20240901.be.base.web.util.base.MyExceptionUtil;
import com.kar20240901.be.base.web.util.base.MyThreadUtil;
import com.kar20240901.be.base.web.util.base.MyTryUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.base.MyValidUtil;
import com.kar20240901.be.base.web.util.base.RequestUtil;
import com.kar20240901.be.base.web.util.socket.SocketUtil;
import com.kar20240901.be.base.web.util.socket.WebSocketUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
@Slf4j
public class NettyWebSocketServerHandler extends ChannelInboundHandlerAdapter {

    @Resource
    NettyWebSocketProperties nettyWebSocketProperties;

    @Resource
    RedissonClient redissonClient;

    @Resource
    BaseSocketRefUserService baseSocketRefUserService;

    // UserId key
    public static final AttributeKey<Long> USER_ID_KEY = AttributeKey.valueOf("USER_ID_KEY");

    // BaseSocketRefUserId key
    public static final AttributeKey<Long> BASE_SOCKET_REF_USER_ID_KEY =
        AttributeKey.valueOf("BASE_SOCKET_REF_USER_ID_KEY");

    // BaseRequestCategoryEnum key
    public static final AttributeKey<BaseRequestCategoryEnum> BASE_REQUEST_CATEGORY_ENUM_KEY =
        AttributeKey.valueOf("BASE_REQUEST_CATEGORY_ENUM_KEY");

    // Ip key
    public static final AttributeKey<String> IP_KEY = AttributeKey.valueOf("IP_KEY");

    // 最近活跃时间 key
    public static final AttributeKey<Date> ACTIVITY_TIME_KEY = AttributeKey.valueOf("ACTIVITY_TIME_KEY");

    // 用户通道 map，大key：用户主键 id，小key：baseSocketRefUserId，value：通道
    public static final ConcurrentHashMap<Long, ConcurrentHashMap<Long, Channel>> USER_ID_CHANNEL_MAP =
        MapUtil.newConcurrentHashMap();

    private static CopyOnWriteArraySet<Long> BASE_SOCKET_REMOVE_REF_USER_ID_SET = new CopyOnWriteArraySet<>();

    private static CopyOnWriteArrayList<BaseSocketRefUserDO> BASE_SOCKET_REF_USER_DO_INSERT_LIST =
        new CopyOnWriteArrayList<>();

    /**
     * 定时任务，检查 webSocket活跃状态
     */
    @PreDestroy
    @Scheduled(fixedDelay = TempConstant.MINUTE_1_EXPIRE_TIME)
    public void scheduledCheckActivityTime() {

        long currentTimeMillis = System.currentTimeMillis();

        // 再包一层的原因：防止遍历的时候，被修改了
        List<ConcurrentHashMap<Long, Channel>> allChannelMapList = new ArrayList<>(USER_ID_CHANNEL_MAP.values());

        for (ConcurrentHashMap<Long, Channel> item : allChannelMapList) {

            List<Channel> channelList = new ArrayList<>(item.values());

            for (Channel subItem : channelList) {

                long time = subItem.attr(ACTIVITY_TIME_KEY).get().getTime();

                // 如果：5分钟没有活跃，则关闭该 webSocket
                if (time + TempConstant.MINUTE_5_EXPIRE_TIME < currentTimeMillis) {

                    subItem.close(); // 直接可以关闭该通道，不会影响遍历，因为已经包了一层新的集合

                }

            }

        }

    }

    /**
     * 定时任务，保存数据
     */
    @PreDestroy
    @Scheduled(fixedDelay = 5000)
    public void scheduledSava() {

        List<VoidFunc0> voidFunc0List = new ArrayList<>();

        // 处理：BASE_SOCKET_REF_USER_DO_INSERT_LIST
        handleBaseSocketRefUserDoInsertList(voidFunc0List);

        // 处理：BASE_SOCKET_REMOVE_REF_USER_ID_SET
        handleBaseSocketRemoveRefUserIdSet(voidFunc0List);

        if (CollUtil.isEmpty(voidFunc0List)) {
            return;
        }

        // 目的：防止还有程序往：tempList，里面添加数据，所以这里等待一会
        MyThreadUtil.schedule(() -> {

            for (VoidFunc0 item : voidFunc0List) {

                item.call();

            }

            //            int sum = USER_ID_CHANNEL_MAP.values().stream().mapToInt(it -> it.values().size()).sum();

            //            log.info("NettyWebSocket 当前连接总数：{}", sum);

        }, DateUtil.offsetMillisecond(new Date(), 1500));

    }

    /**
     * 处理：BASE_SOCKET_REF_USER_DO_LIST
     */
    private void handleBaseSocketRefUserDoInsertList(List<VoidFunc0> voidFunc0List) {

        CopyOnWriteArrayList<BaseSocketRefUserDO> tempBaseSocketRefUserDoInsertList;

        synchronized (BASE_SOCKET_REF_USER_DO_INSERT_LIST) {

            if (CollUtil.isEmpty(BASE_SOCKET_REF_USER_DO_INSERT_LIST)) {
                return;
            }

            tempBaseSocketRefUserDoInsertList = BASE_SOCKET_REF_USER_DO_INSERT_LIST;
            BASE_SOCKET_REF_USER_DO_INSERT_LIST = new CopyOnWriteArrayList<>();

        }

        voidFunc0List.add(() -> {

            // 批量操作数据
            baseSocketRefUserService.saveBatch(tempBaseSocketRefUserDoInsertList);

        });

    }

    /**
     * 处理：BASE_SOCKET_REMOVE_REF_USER_ID_SET
     */
    private void handleBaseSocketRemoveRefUserIdSet(List<VoidFunc0> voidFunc0List) {

        CopyOnWriteArraySet<Long> tempBaseSocketRefUserIdSet;

        synchronized (BASE_SOCKET_REMOVE_REF_USER_ID_SET) {

            if (CollUtil.isEmpty(BASE_SOCKET_REMOVE_REF_USER_ID_SET)) {
                return;
            }

            tempBaseSocketRefUserIdSet = BASE_SOCKET_REMOVE_REF_USER_ID_SET;
            BASE_SOCKET_REMOVE_REF_USER_ID_SET = new CopyOnWriteArraySet<>();

        }

        voidFunc0List.add(() -> {

            // 批量操作数据
            baseSocketRefUserService.removeByIds(tempBaseSocketRefUserIdSet);

        });

    }

    /**
     * 连接成功时
     */
    @SneakyThrows
    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx) {

        super.channelActive(ctx);

    }

    /**
     * 调用 close等操作，连接断开时
     */
    @SneakyThrows
    @Override
    public void channelInactive(@NotNull ChannelHandlerContext ctx) {

        Channel channel = ctx.channel();

        Long userId = channel.attr(USER_ID_KEY).get();

        if (userId != null) {

            Long baseSocketRefUserId = channel.attr(BASE_SOCKET_REF_USER_ID_KEY).get();

            ConcurrentHashMap<Long, Channel> channelMap =
                USER_ID_CHANNEL_MAP.computeIfAbsent(userId, k -> MapUtil.newConcurrentHashMap());

            channelMap.remove(baseSocketRefUserId);

            //            log.info("WebSocket 断开，用户：{}，连接数：{}，baseSocketRefUserId：{}", userId, channelMap.size(),
            //                baseSocketRefUserId);

            BASE_SOCKET_REMOVE_REF_USER_ID_SET.add(baseSocketRefUserId);

        }

        super.channelInactive(ctx);

    }

    /**
     * 发生异常时，比如：远程主机强迫关闭了一个现有的连接，或者任何没有被捕获的异常
     */
    @SneakyThrows
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {

        ctx.close(); // 会执行：channelInactive 方法

        super.exceptionCaught(ctx, e); // 会打印日志

    }

    /**
     * 收到消息时
     */
    @Override
    public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) {

        // 首次连接是 FullHttpRequest，处理参数
        if (msg instanceof FullHttpRequest) {

            MyTryUtil.tryCatch(() -> {

                // 处理：FullHttpRequest
                handleFullHttpRequest(ctx, (FullHttpRequest)msg);

                // 传递给下一个 handler，备注：这里不需要释放资源
                ctx.fireChannelRead(msg);

            }, e -> {

                ReferenceCountUtil.release(msg); // 备注：这里需要释放资源

            });

        } else if (msg instanceof TextWebSocketFrame) {

            MyTryUtil.tryCatchFinally(() -> {

                // 处理：TextWebSocketFrame
                handleTextWebSocketFrame((TextWebSocketFrame)msg, ctx.channel());

            }, () -> {

                ReferenceCountUtil.release(msg); // 备注：这里需要释放资源

            });

        } else {

            // 传递给下一个 handler，备注：这里不需要释放资源
            ctx.fireChannelRead(msg);

        }

    }

    /**
     * 处理：TextWebSocketFrame
     */
    private void handleTextWebSocketFrame(@NotNull TextWebSocketFrame textWebSocketFrame, Channel channel) {

        long costMs = System.currentTimeMillis();

        String text = textWebSocketFrame.text();

        WebSocketMessageDTO<?> dto = JSONUtil.toBean(text, WebSocketMessageDTO.class);

        String uri = dto.getUri();

        NettyWebSocketBeanPostProcessor.MappingValue mappingValue =
            NettyWebSocketBeanPostProcessor.getMappingValueByKey(uri);

        if (mappingValue == null) {

            WebSocketMessageDTO<Object> webSocketMessageDTO = WebSocketMessageDTO.errorCode(uri, 404);

            WebSocketUtil.send(channel, webSocketMessageDTO, text, costMs, null, "", false);

            return;

        }

        channel.attr(ACTIVITY_TIME_KEY).set(new Date()); // 设置：活跃时间，备注：404不设置活跃时间，目的：防止随便请求

        Method method = mappingValue.getMethod();

        CallBack<VoidFunc0> validVoidFunc0CallBack = new CallBack<>();

        // 获取：方法的参数数组
        Object[] args = getMethodArgs(method, dto, validVoidFunc0CallBack, channel);

        // 执行
        doHandleTextWebSocketFrame(channel, mappingValue, method, args, uri, text, costMs, validVoidFunc0CallBack);

    }

    /**
     * 执行：处理：TextWebSocketFrame
     */
    private void doHandleTextWebSocketFrame(Channel channel, NettyWebSocketBeanPostProcessor.MappingValue mappingValue,
        Method method, Object[] args, String uri, String text, long costMs,
        CallBack<VoidFunc0> validVoidFunc0CallBack) {

        Long userId = channel.attr(USER_ID_KEY).get();

        // 备注：加了该注解，并且使用代理 bean对象执行该方法，会自动检查权限
        boolean setAuthoritySetFlag = method.getAnnotation(PreAuthorize.class) != null;

        MyUserUtil.securityContextHolderSetAuthenticationAndExecFun(() -> {

            try {

                if (validVoidFunc0CallBack.getValue() != null) {

                    validVoidFunc0CallBack.getValue().call();// 备注：aop 时，@Valid 不会起作用，所以在这里编程式调用

                }

                Object invoke = ReflectUtil.invokeRaw(mappingValue.getBean(), method, args);

                // 获取：WebSocketMessageDTO对象
                WebSocketMessageDTO<Object> webSocketMessageDTO = getWebSocketMessageDTO(uri, invoke);

                WebSocketUtil.send(channel, webSocketMessageDTO, text, costMs, mappingValue, "", true);

            } catch (Throwable e) {

                // 处理：错误
                handleTextWebSocketFrameError(channel, costMs, text, uri, mappingValue, e);

            }

        }, userId, null, null, setAuthoritySetFlag);

    }

    /**
     * 获取：WebSocketMessageDTO对象
     */
    @NotNull
    private static WebSocketMessageDTO<Object> getWebSocketMessageDTO(String uri, Object invoke) {

        WebSocketMessageDTO<Object> webSocketMessageDTO = new WebSocketMessageDTO<>(uri);

        if (invoke instanceof R) {

            R<?> r = (R<?>)invoke;

            webSocketMessageDTO.setCode(r.getCode());
            webSocketMessageDTO.setData(r.getData());

        } else {

            webSocketMessageDTO.setCode(200);
            webSocketMessageDTO.setData(invoke);

        }

        return webSocketMessageDTO;

    }

    /**
     * 获取：方法的参数数组
     */
    private static Object[] getMethodArgs(Method method, WebSocketMessageDTO<?> dto,
        CallBack<VoidFunc0> validVoidFunc0CallBack, Channel channel) {

        Parameter[] parameterArr = method.getParameters();

        Object[] args = null;

        if (ArrayUtil.isEmpty(parameterArr)) {

            return args;

        }

        Parameter parameter = parameterArr[0];

        Object object = BeanUtil.toBean(dto.getData(), parameter.getType());

        Valid validAnnotation = parameter.getAnnotation(Valid.class);

        if (validAnnotation != null) {

            validVoidFunc0CallBack.setValue(() -> {

                MyValidUtil.validWillError(object);

            });

        }

        if (parameterArr.length == 2 && parameterArr[parameterArr.length - 1].getType() == ChannelDataBO.class) {

            ChannelDataBO channelDataBO = new ChannelDataBO();

            Long userId = channel.attr(USER_ID_KEY).get();
            Long socketRefUserId = channel.attr(BASE_SOCKET_REF_USER_ID_KEY).get();
            BaseRequestCategoryEnum category = channel.attr(BASE_REQUEST_CATEGORY_ENUM_KEY).get();
            String ip = channel.attr(IP_KEY).get();

            channelDataBO.setUserId(userId);
            channelDataBO.setSocketRefUserId(socketRefUserId);
            channelDataBO.setCategory(category);
            channelDataBO.setIp(ip);

            args = new Object[] {object, channelDataBO};

        } else {

            args = new Object[] {object};

        }

        return args;

    }

    /**
     * 处理：错误
     */
    private void handleTextWebSocketFrameError(Channel channel, long costMs, String text, String uri,
        NettyWebSocketBeanPostProcessor.MappingValue mappingValue, Throwable e) {

        if (e instanceof InvocationTargetException) {

            e = ((InvocationTargetException)e).getTargetException();

        }

        MyExceptionUtil.printError(e);

        WebSocketMessageDTO<Object> webSocketMessageDTO = new WebSocketMessageDTO<>(uri);

        if (e instanceof TempException) {

            R<?> r = ((TempException)e).getR();

            webSocketMessageDTO.setCode(r.getCode());
            webSocketMessageDTO.setMsg(r.getMsg());

        } else {

            webSocketMessageDTO.setCode(TempBizCodeEnum.RESULT_SYS_ERROR.getCode());
            webSocketMessageDTO.setMsg(TempBizCodeEnum.RESULT_SYS_ERROR.getMsg());

        }

        // 发送消息
        WebSocketUtil.send(channel, webSocketMessageDTO, text, costMs, mappingValue,
            ExceptionUtil.stacktraceToString(e), false);

    }

    /**
     * 处理：FullHttpRequest
     */
    private void handleFullHttpRequest(@NotNull ChannelHandlerContext ctx, @NotNull FullHttpRequest fullHttpRequest) {

        UrlQuery urlQuery = UrlQuery.of(fullHttpRequest.uri(), CharsetUtil.CHARSET_UTF_8);

        String code = Convert.toStr(urlQuery.get("code")); // 随机码

        if (StrUtil.isBlank(code)) {

            handleFullHttpRequestError(ctx, fullHttpRequest.uri(), "code为空", fullHttpRequest);

            return;

        }

        String key = BaseRedisKeyEnum.PRE_WEB_SOCKET_CODE.name() + ":" + code;

        BaseSocketRefUserDO baseSocketRefUserDO = redissonClient.<BaseSocketRefUserDO>getBucket(key).getAndDelete();

        if (baseSocketRefUserDO == null) {

            handleFullHttpRequestError(ctx, fullHttpRequest.uri(), "BaseSocketRefUserDO为null",
                fullHttpRequest); // 处理：非法连接

            return;

        }

        if (!baseSocketRefUserDO.getSocketId().equals(NettyWebSocketServer.baseSocketServerId)) {

            handleFullHttpRequestError(ctx, fullHttpRequest.uri(), "SocketId不相同", fullHttpRequest); // 处理：非法连接

            return;

        }

        // url包含参数，需要舍弃
        fullHttpRequest.setUri(nettyWebSocketProperties.getPath());

        // 处理：上线操作
        onlineHandle(ctx.channel(), baseSocketRefUserDO, fullHttpRequest);

    }

    /**
     * 处理：上线操作
     */
    private void onlineHandle(Channel channel, BaseSocketRefUserDO baseSocketRefUserDO,
        @NotNull FullHttpRequest fullHttpRequest) {

        BASE_SOCKET_REF_USER_DO_INSERT_LIST.add(baseSocketRefUserDO);

        Long userId = baseSocketRefUserDO.getUserId();

        Long baseSocketRefUserDoId = baseSocketRefUserDO.getId();

        // 绑定 UserId
        channel.attr(USER_ID_KEY).set(userId);

        // 绑定 BaseSocketRefUserId
        channel.attr(BASE_SOCKET_REF_USER_ID_KEY).set(baseSocketRefUserDoId);

        // 绑定 BaseRequestCategoryEnum
        channel.attr(BASE_REQUEST_CATEGORY_ENUM_KEY).set(baseSocketRefUserDO.getCategory());

        // 绑定 Ip
        channel.attr(IP_KEY).set(SocketUtil.getIp(fullHttpRequest, channel));

        // 设置：最近活跃时间
        channel.attr(ACTIVITY_TIME_KEY).set(new Date());

        ConcurrentHashMap<Long, Channel> channelMap =
            USER_ID_CHANNEL_MAP.computeIfAbsent(userId, k -> MapUtil.newConcurrentHashMap());

        channelMap.put(baseSocketRefUserDoId, channel);

    }

    /**
     * 处理：非法连接
     */
    private void handleFullHttpRequestError(@NotNull ChannelHandlerContext ctx, String requestParam, String errorMsg,
        @NotNull FullHttpRequest fullHttpRequest) {

        ctx.close(); // 关闭连接

        Date date = new Date();

        BaseRequestDO baseRequestDO = new BaseRequestDO();

        BaseRequestInfoDO baseRequestInfoDO = new BaseRequestInfoDO();

        Long id = IdGeneratorUtil.nextId();

        baseRequestDO.setId(id);
        baseRequestInfoDO.setId(id);

        baseRequestDO.setUri("");
        baseRequestDO.setCostMs(0L);
        baseRequestDO.setName("WebSocket连接错误");
        baseRequestDO.setCategory(BaseRequestCategoryEnum.PC_BROWSER_WINDOWS);

        baseRequestDO.setIp(SocketUtil.getIp(fullHttpRequest, ctx.channel()));
        baseRequestDO.setRegion(Ip2RegionUtil.getRegion(baseRequestDO.getIp()));

        baseRequestDO.setSuccessFlag(false);

        baseRequestDO.setType(OperationDescriptionConstant.WEB_SOCKET_CONNECT_ERROR);

        baseRequestDO.setCreateTime(date);

        baseRequestInfoDO.setErrorMsg(errorMsg);
        baseRequestInfoDO.setRequestParam(requestParam);
        baseRequestInfoDO.setResponseValue("");

        // 添加一个：请求数据
        RequestUtil.add(baseRequestDO, baseRequestInfoDO);

    }

}
