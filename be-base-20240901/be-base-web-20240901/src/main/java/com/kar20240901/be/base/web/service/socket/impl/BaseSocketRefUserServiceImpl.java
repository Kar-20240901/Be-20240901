package com.kar20240901.be.base.web.service.socket.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper;
import com.kar20240901.be.base.web.model.domain.socket.BaseSocketRefUserDO;
import com.kar20240901.be.base.web.model.dto.NotEmptyIdSet;
import com.kar20240901.be.base.web.service.socket.BaseSocketRefUserService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class BaseSocketRefUserServiceImpl extends ServiceImpl<BaseSocketRefUserMapper, BaseSocketRefUserDO>
    implements BaseSocketRefUserService {

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseSocketRefUserDO> myPage(SysSocketRefUserPageDTO dto) {

        return lambdaQuery().eq(dto.getUserId() != null, BaseSocketRefUserDO::getUserId, dto.getUserId())
            .eq(dto.getSocketId() != null, BaseSocketRefUserDO::getSocketId, dto.getSocketId())
            .like(StrUtil.isNotBlank(dto.getScheme()), BaseSocketRefUserDO::getScheme, dto.getScheme())
            .like(StrUtil.isNotBlank(dto.getHost()), BaseSocketRefUserDO::getHost, dto.getHost())
            .eq(dto.getPort() != null, BaseSocketRefUserDO::getPort, dto.getPort())
            .eq(dto.getType() != null, BaseSocketRefUserDO::getType, dto.getType())
            .eq(dto.getId() != null, BaseEntity::getId, dto.getId())
            .eq(dto.getOnlineType() != null, BaseSocketRefUserDO::getOnlineType, dto.getOnlineType())
            .like(StrUtil.isNotBlank(dto.getIp()), BaseSocketRefUserDO::getIp, dto.getIp())
            .like(StrUtil.isNotBlank(dto.getRegion()), BaseSocketRefUserDO::getRegion, dto.getRegion())
            .like(StrUtil.isNotBlank(dto.getRemark()), BaseSocketRefUserDO::getRemark, dto.getRemark())
            .in(BaseEntityNoId::getTenantId, dto.getTenantIdSet()) //
            .page(dto.page(true));

    }

    /**
     * 批量：下线用户
     */
    @Override
    public String offlineByIdSet(NotEmptyIdSet notEmptyIdSet) {

        if (CollUtil.isEmpty(notEmptyIdSet.getIdSet())) {
            return BaseBizCodeEnum.OK;
        }

        // 检查：是否非法操作
        SysTenantUtil.checkIllegal(notEmptyIdSet.getIdSet(),
            tenantIdSet -> lambdaQuery().in(BaseEntity::getId, notEmptyIdSet.getIdSet())
                .in(BaseEntityNoId::getTenantId, tenantIdSet).count());

        List<BaseSocketRefUserDO> BaseSocketRefUserDOList =
            lambdaQuery().in(BaseEntity::getId, notEmptyIdSet.getIdSet())
                .select(BaseSocketRefUserDO::getJwtHash, BaseSocketRefUserDO::getJwtHashExpireTs).list();

        if (CollUtil.isNotEmpty(BaseSocketRefUserDOList)) {

            for (BaseSocketRefUserDO BaseSocketRefUserDO : BaseSocketRefUserDOList) {

                CacheRedisKafkaLocalUtil.put(BaseSocketRefUserDO.getJwtHash(), BaseSocketRefUserDO.getJwtHashExpireTs(),
                    () -> "不可用的 jwt：下线");

            }

            lambdaUpdate().in(BaseEntity::getId, notEmptyIdSet.getIdSet()).remove();

        }

        return BaseBizCodeEnum.OK;

    }

    /**
     * 批量：打开控制台
     */
    @Override
    public String changeConsoleFlagByIdSet(NotEmptyIdSet notEmptyIdSet) {

        List<BaseSocketRefUserDO> BaseSocketRefUserDOList =
            lambdaQuery().in(BaseEntity::getId, notEmptyIdSet.getIdSet()).select(BaseSocketRefUserDO::getUserId).list();

        Set<Long> userIdSet =
            BaseSocketRefUserDOList.stream().map(BaseSocketRefUserDO::getUserId).collect(Collectors.toSet());

        if (CollUtil.isEmpty(userIdSet)) {
            return BaseBizCodeEnum.OK;
        }

        SysWebSocketEventBO<NotNullIdAndNotEmptyLongSet> sysWebSocketEventBO = new SysWebSocketEventBO<>();

        sysWebSocketEventBO.setUserIdSet(userIdSet);

        sysWebSocketEventBO.setSysSocketRefUserIdSet(notEmptyIdSet.getIdSet());

        WebSocketMessageDTO<NotNullIdAndNotEmptyLongSet> webSocketMessageDTO =
            WebSocketMessageDTO.okData(BaseWebSocketUriEnum.SYS_SOCKET_REF_USER_CHANGE_CONSOLE_FLAG_BY_ID_SET, null);

        sysWebSocketEventBO.setWebSocketMessageDTO(webSocketMessageDTO);

        // 发送：webSocket事件
        KafkaUtil.sendSysWebSocketEventTopic(sysWebSocketEventBO);

        return BaseBizCodeEnum.OK;

    }

}
