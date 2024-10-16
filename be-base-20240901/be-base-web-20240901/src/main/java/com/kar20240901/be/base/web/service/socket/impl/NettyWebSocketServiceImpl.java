package com.kar20240901.be.base.web.service.socket.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.Header;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.hutool.json.JSONUtil;
import com.kar20240901.be.base.web.model.domain.socket.BaseSocketDO;
import com.kar20240901.be.base.web.model.dto.NotNullIdAndIntegerValue;
import com.kar20240901.be.base.web.model.enums.SysSocketOnlineTypeEnum;
import com.kar20240901.be.base.web.model.enums.SysSocketTypeEnum;
import com.kar20240901.be.base.web.service.socket.BaseSocketService;
import com.kar20240901.be.base.web.service.socket.NettyWebSocketService;
import com.kar20240901.be.base.web.util.CallBack;
import com.kar20240901.be.base.web.util.IdGeneratorUtil;
import com.kar20240901.be.base.web.util.Ip2RegionUtil;
import com.kar20240901.be.base.web.util.MyMapUtil;
import com.kar20240901.be.base.web.util.RequestUtil;
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
        return handleGetAllWebSocketUrl(null, SysSocketOnlineTypeEnum.PING_TEST);

    }

    /**
     * 获取：webSocket连接地址
     */
    @NotNull
    private HashSet<String> handleGetAllWebSocketUrl(@Nullable List<BaseSocketDO> BaseSocketDOList,
        @NotNull SysSocketOnlineTypeEnum sysSocketOnlineTypeEnum) {

        CallBack<Long> expireTsCallBack = new CallBack<>();

        // 获取：请求里面的 jwtHash值
        String jwtHash = MyJwtUtil.getJwtHashByRequest(httpServletRequest, null, expireTsCallBack);

        if (StrUtil.isBlank(jwtHash)) {
            return new HashSet<>();
        }

        // 获取：所有 webSocket
        if (BaseSocketDOList == null) {

            BaseSocketDOList = baseSocketService.lambdaQuery().eq(BaseSocketDO::getType, SysSocketTypeEnum.WEB_SOCKET)
                .eq(BaseEntityNoId::getEnableFlag, true).list();

        }

        if (CollUtil.isEmpty(BaseSocketDOList)) {
            return new HashSet<>();
        }

        String currentUserNickName = UserUtil.getCurrentUserNickName();

        Long currentUserId = UserUtil.getCurrentUserId();

        String ip = ServletUtil.getClientIP(httpServletRequest);

        String region = Ip2RegionUtil.getRegion(ip);

        SysRequestCategoryEnum sysRequestCategoryEnum = RequestUtil.getRequestCategoryEnum(httpServletRequest);

        String userAgentStr = httpServletRequest.getHeader(Header.USER_AGENT.getValue());

        UserAgent userAgent = UserAgentUtil.parse(userAgentStr);

        String userAgentJsonStr = JSONUtil.toJsonStr(userAgent);

        HashSet<String> resSet = new HashSet<>(MyMapUtil.getInitialCapacity(BaseSocketDOList.size()));

        Long currentTenantIdDefault = UserUtil.getCurrentTenantIdDefault();

        for (BaseSocketDO item : BaseSocketDOList) {

            // 处理：获取：所有 webSocket连接地址
            doHandleGetAllWebSocketUrl(expireTsCallBack, jwtHash, currentUserNickName, currentUserId, ip, region,
                sysRequestCategoryEnum, userAgentJsonStr, resSet, item, sysSocketOnlineTypeEnum,
                currentTenantIdDefault);

        }

        return resSet;

    }

    /**
     * 通过主键 id，获取：webSocket连接地址，格式：scheme://ip:port/path?code=xxx
     */
    @Override
    public String getWebSocketUrlById(NotNullIdAndIntegerValue notNullIdAndIntegerValue) {

        BaseSocketDO BaseSocketDO =
            baseSocketService.lambdaQuery().eq(BaseEntity::getId, notNullIdAndIntegerValue.getId())
                .eq(BaseSocketDO::getType, SysSocketTypeEnum.WEB_SOCKET).eq(BaseEntityNoId::getEnableFlag, true).one();

        Integer value = notNullIdAndIntegerValue.getValue();

        SysSocketOnlineTypeEnum sysSocketOnlineTypeEnum = SysSocketOnlineTypeEnum.getByCode(value);

        // 获取：webSocket连接地址
        Set<String> webSocketUrlSet =
            handleGetAllWebSocketUrl(CollUtil.newArrayList(BaseSocketDO), sysSocketOnlineTypeEnum);

        return CollUtil.getFirst(webSocketUrlSet);

    }

    /**
     * 处理：获取：所有 webSocket连接地址
     */
    private void doHandleGetAllWebSocketUrl(CallBack<Long> expireTsCallBack, String jwtHash, String currentUserNickName,
        Long currentUserId, String ip, String region, SysRequestCategoryEnum sysRequestCategoryEnum,
        String userAgentJsonStr, HashSet<String> resSet, BaseSocketDO BaseSocketDO,
        SysSocketOnlineTypeEnum sysSocketOnlineTypeEnum, Long currentTenantIdDefault) {

        String code = IdUtil.simpleUUID();

        StrBuilder strBuilder = StrBuilder.create();

        strBuilder.append(BaseSocketDO.getScheme()).append(BaseSocketDO.getHost());

        if ("ws://".equals(BaseSocketDO.getScheme())) { // ws，才需要端口号

            strBuilder.append(":").append(BaseSocketDO.getPort());

        }

        strBuilder.append(BaseSocketDO.getPath()).append("?code=").append(code);

        resSet.add(strBuilder.toString()); // 添加到返回值里

        String key = BaseRedisKeyEnum.PRE_WEB_SOCKET_CODE.name() + code;

        SysSocketRefUserDO sysSocketRefUserDO = new SysSocketRefUserDO();

        Long nextId = IdGeneratorUtil.nextId();
        sysSocketRefUserDO.setId(nextId); // 备注：这里手动设置 id

        sysSocketRefUserDO.setUserId(currentUserId);
        sysSocketRefUserDO.setSocketId(BaseSocketDO.getId());
        sysSocketRefUserDO.setNickname(currentUserNickName);
        sysSocketRefUserDO.setScheme(BaseSocketDO.getScheme());
        sysSocketRefUserDO.setHost(BaseSocketDO.getHost());
        sysSocketRefUserDO.setPort(BaseSocketDO.getPort());
        sysSocketRefUserDO.setPath(BaseSocketDO.getPath());
        sysSocketRefUserDO.setType(BaseSocketDO.getType());

        sysSocketRefUserDO.setOnlineType(sysSocketOnlineTypeEnum);
        sysSocketRefUserDO.setIp(ip);
        sysSocketRefUserDO.setRegion(region);

        sysSocketRefUserDO.setJwtHash(jwtHash);
        sysSocketRefUserDO.setJwtHashExpireTs(expireTsCallBack.getValue());

        sysSocketRefUserDO.setCategory(sysRequestCategoryEnum);

        sysSocketRefUserDO.setUserAgentJsonStr(userAgentJsonStr);

        sysSocketRefUserDO.setTenantId(currentTenantIdDefault);

        sysSocketRefUserDO.setCreateId(currentUserId);
        sysSocketRefUserDO.setUpdateId(currentUserId);

        sysSocketRefUserDO.setEnableFlag(sysSocketOnlineTypeEnum.equals(SysSocketOnlineTypeEnum.PING_TEST) == false);

        sysSocketRefUserDO.setDelFlag(false);
        sysSocketRefUserDO.setRemark("");

        // 设置到：redis里面，用于连接的时候用
        redissonClient.<SysSocketRefUserDO>getBucket(key)
            .set(sysSocketRefUserDO, Duration.ofMillis(BaseConstant.SHORT_CODE_EXPIRE_TIME));

    }

}
