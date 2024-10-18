package com.kar20240901.be.base.web.service.socket.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.kar20240901.be.base.web.configuration.base.BaseConfiguration;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoId;
import com.kar20240901.be.base.web.model.domain.socket.BaseSocketDO;
import com.kar20240901.be.base.web.model.domain.socket.BaseSocketRefUserDO;
import com.kar20240901.be.base.web.model.dto.base.NotNullIdAndIntegerValue;
import com.kar20240901.be.base.web.model.enums.base.BaseRedisKeyEnum;
import com.kar20240901.be.base.web.model.enums.base.BaseRequestCategoryEnum;
import com.kar20240901.be.base.web.model.enums.socket.BaseSocketOnlineTypeEnum;
import com.kar20240901.be.base.web.model.enums.socket.BaseSocketTypeEnum;
import com.kar20240901.be.base.web.service.socket.BaseSocketService;
import com.kar20240901.be.base.web.service.socket.NettyWebSocketService;
import com.kar20240901.be.base.web.util.base.IdGeneratorUtil;
import com.kar20240901.be.base.web.util.base.Ip2RegionUtil;
import com.kar20240901.be.base.web.util.base.MyMapUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.base.RequestUtil;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
public class NettyWebSocketServiceImpl implements NettyWebSocketService {

    @Resource
    BaseSocketService baseSocketService;

    @Resource
    RedissonClient redissonClient;

    @Resource
    HttpServletRequest httpServletRequest;

    /**
     * 获取：所有 webSocket连接地址，格式：scheme://ip:port/path?code=xxx
     */
    @Override
    public Set<String> getAllWebSocketUrl() {

        // 获取：webSocket连接地址
        return handleGetAllWebSocketUrl(null, BaseSocketOnlineTypeEnum.PING_TEST);

    }

    /**
     * 获取：webSocket连接地址
     */
    @NotNull
    private HashSet<String> handleGetAllWebSocketUrl(@Nullable List<BaseSocketDO> baseSocketDoList,
        @NotNull BaseSocketOnlineTypeEnum baseSocketOnlineTypeEnum) {

        // 获取：所有 webSocket
        if (baseSocketDoList == null) {

            baseSocketDoList = baseSocketService.lambdaQuery().eq(BaseSocketDO::getType, BaseSocketTypeEnum.WEB_SOCKET)
                .eq(TempEntityNoId::getEnableFlag, true).list();

        }

        if (CollUtil.isEmpty(baseSocketDoList)) {
            return new HashSet<>();
        }

        String currentUserNickName = MyUserUtil.getCurrentUserNickName();

        Long currentUserId = MyUserUtil.getCurrentUserId();

        String ip = ServletUtil.getClientIP(httpServletRequest);

        String region = Ip2RegionUtil.getRegion(ip);

        BaseRequestCategoryEnum baseRequestCategoryEnum = RequestUtil.getRequestCategoryEnum(httpServletRequest);

        HashSet<String> resSet = new HashSet<>(MyMapUtil.getInitialCapacity(baseSocketDoList.size()));

        for (BaseSocketDO item : baseSocketDoList) {

            // 处理：获取：所有 webSocket连接地址
            doHandleGetAllWebSocketUrl(currentUserNickName, currentUserId, ip, region, baseRequestCategoryEnum, resSet,
                item, baseSocketOnlineTypeEnum);

        }

        return resSet;

    }

    /**
     * 处理：获取：所有 webSocket连接地址
     */
    private void doHandleGetAllWebSocketUrl(String currentUserNickName, Long currentUserId, String ip, String region,
        BaseRequestCategoryEnum baseRequestCategoryEnum, HashSet<String> resSet, BaseSocketDO baseSocketDO,
        BaseSocketOnlineTypeEnum baseSocketOnlineTypeEnum) {

        String code = IdUtil.simpleUUID();

        StrBuilder strBuilder = StrBuilder.create();

        strBuilder.append(baseSocketDO.getScheme()).append(baseSocketDO.getHost());

        if ("ws://".equals(baseSocketDO.getScheme())) { // ws，才需要端口号

            strBuilder.append(":").append(baseSocketDO.getPort());

        }

        strBuilder.append(baseSocketDO.getPath()).append("?code=").append(code);

        resSet.add(strBuilder.toString()); // 添加到返回值里

        String key = BaseRedisKeyEnum.PRE_WEB_SOCKET_CODE.name() + code;

        BaseSocketRefUserDO baseSocketRefUserDO = new BaseSocketRefUserDO();

        Long nextId = IdGeneratorUtil.nextId();

        baseSocketRefUserDO.setId(nextId); // 备注：这里手动设置 id

        baseSocketRefUserDO.setUserId(currentUserId);
        baseSocketRefUserDO.setSocketId(baseSocketDO.getId());
        baseSocketRefUserDO.setNickname(currentUserNickName);
        baseSocketRefUserDO.setScheme(baseSocketDO.getScheme());
        baseSocketRefUserDO.setHost(baseSocketDO.getHost());
        baseSocketRefUserDO.setPort(baseSocketDO.getPort());
        baseSocketRefUserDO.setPath(baseSocketDO.getPath());
        baseSocketRefUserDO.setMacAddress(BaseConfiguration.MAC_ADDRESS);
        baseSocketRefUserDO.setType(baseSocketDO.getType());

        baseSocketRefUserDO.setOnlineType(baseSocketOnlineTypeEnum);
        baseSocketRefUserDO.setIp(ip);
        baseSocketRefUserDO.setRegion(region);

        baseSocketRefUserDO.setCategory(baseRequestCategoryEnum);

        baseSocketRefUserDO.setCreateId(currentUserId);
        baseSocketRefUserDO.setUpdateId(currentUserId);

        baseSocketRefUserDO.setEnableFlag(baseSocketOnlineTypeEnum.equals(BaseSocketOnlineTypeEnum.PING_TEST) == false);

        baseSocketRefUserDO.setRemark("");

        // 设置到：redis里面，用于连接的时候用
        redissonClient.<BaseSocketRefUserDO>getBucket(key)
            .set(baseSocketRefUserDO, Duration.ofMillis(TempConstant.SHORT_CODE_EXPIRE_TIME));

    }

    /**
     * 通过主键 id，获取：webSocket连接地址，格式：scheme://ip:port/path?code=xxx
     */
    @Override
    public String getWebSocketUrlById(NotNullIdAndIntegerValue notNullIdAndIntegerValue) {

        BaseSocketDO baseSocketDO =
            baseSocketService.lambdaQuery().eq(BaseSocketDO::getId, notNullIdAndIntegerValue.getId())
                .eq(BaseSocketDO::getType, BaseSocketTypeEnum.WEB_SOCKET).eq(BaseSocketDO::getEnableFlag, true).one();

        Integer value = notNullIdAndIntegerValue.getValue();

        BaseSocketOnlineTypeEnum baseSocketOnlineTypeEnum = BaseSocketOnlineTypeEnum.getByCode(value);

        // 获取：webSocket连接地址
        Set<String> webSocketUrlSet =
            handleGetAllWebSocketUrl(CollUtil.newArrayList(baseSocketDO), baseSocketOnlineTypeEnum);

        return CollUtil.getFirst(webSocketUrlSet);

    }

}
