package com.kar20240901.be.base.web.service.socket.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.socket.BaseSocketRefUserMapper;
import com.kar20240901.be.base.web.model.bo.socket.BaseWebSocketEventBO;
import com.kar20240901.be.base.web.model.domain.TempEntity;
import com.kar20240901.be.base.web.model.domain.socket.BaseSocketRefUserDO;
import com.kar20240901.be.base.web.model.dto.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.NotNullIdAndNotEmptyLongSet;
import com.kar20240901.be.base.web.model.dto.socket.BaseSocketRefUserPageDTO;
import com.kar20240901.be.base.web.model.dto.socket.WebSocketMessageDTO;
import com.kar20240901.be.base.web.model.enums.socket.BaseWebSocketUriEnum;
import com.kar20240901.be.base.web.service.socket.BaseSocketRefUserService;
import com.kar20240901.be.base.web.util.kafka.TempKafkaUtil;
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
    public Page<BaseSocketRefUserDO> myPage(BaseSocketRefUserPageDTO dto) {

        return lambdaQuery().eq(dto.getUserId() != null, BaseSocketRefUserDO::getUserId, dto.getUserId()) //
            .eq(dto.getSocketId() != null, BaseSocketRefUserDO::getSocketId, dto.getSocketId()) //
            .like(StrUtil.isNotBlank(dto.getScheme()), BaseSocketRefUserDO::getScheme, dto.getScheme()) //
            .like(StrUtil.isNotBlank(dto.getHost()), BaseSocketRefUserDO::getHost, dto.getHost()) //
            .eq(dto.getPort() != null, BaseSocketRefUserDO::getPort, dto.getPort()) //
            .eq(dto.getType() != null, BaseSocketRefUserDO::getType, dto.getType()) //
            .eq(dto.getId() != null, TempEntity::getId, dto.getId()) //
            .eq(dto.getOnlineType() != null, BaseSocketRefUserDO::getOnlineType, dto.getOnlineType()) //
            .like(StrUtil.isNotBlank(dto.getIp()), BaseSocketRefUserDO::getIp, dto.getIp()) //
            .like(StrUtil.isNotBlank(dto.getRegion()), BaseSocketRefUserDO::getRegion, dto.getRegion()) //
            .like(StrUtil.isNotBlank(dto.getRemark()), BaseSocketRefUserDO::getRemark, dto.getRemark()) //
            .page(dto.pageOrder());

    }

    /**
     * 批量：下线用户
     */
    @Override
    public String offlineByIdSet(NotEmptyIdSet notEmptyIdSet) {

        if (CollUtil.isEmpty(notEmptyIdSet.getIdSet())) {
            return TempBizCodeEnum.OK;
        }

        List<BaseSocketRefUserDO> baseSocketRefUserDoList =
            lambdaQuery().in(TempEntity::getId, notEmptyIdSet.getIdSet()).select(BaseSocketRefUserDO::getUserId).list();

        Set<Long> userIdSet =
            baseSocketRefUserDoList.stream().map(BaseSocketRefUserDO::getUserId).collect(Collectors.toSet());

        if (CollUtil.isEmpty(userIdSet)) {
            return TempBizCodeEnum.OK;
        }

        BaseWebSocketEventBO<NotNullIdAndNotEmptyLongSet> baseWebSocketEventBO = new BaseWebSocketEventBO<>();

        baseWebSocketEventBO.setUserIdSet(userIdSet);

        baseWebSocketEventBO.setSysSocketRefUserIdSet(notEmptyIdSet.getIdSet());

        WebSocketMessageDTO<NotNullIdAndNotEmptyLongSet> webSocketMessageDTO =
            WebSocketMessageDTO.okData(BaseWebSocketUriEnum.BASE_SIGN_OUT, null);

        baseWebSocketEventBO.setWebSocketMessageDTO(webSocketMessageDTO);

        // 发送：webSocket事件
        TempKafkaUtil.sendBaseWebSocketEventTopic(baseWebSocketEventBO);

        lambdaUpdate().in(TempEntity::getId, notEmptyIdSet.getIdSet()).remove();

        return TempBizCodeEnum.OK;

    }

    /**
     * 批量：打开控制台
     */
    @Override
    public String changeConsoleFlagByIdSet(NotEmptyIdSet notEmptyIdSet) {

        List<BaseSocketRefUserDO> baseSocketRefUserDoList =
            lambdaQuery().in(TempEntity::getId, notEmptyIdSet.getIdSet()).select(BaseSocketRefUserDO::getUserId).list();

        Set<Long> userIdSet =
            baseSocketRefUserDoList.stream().map(BaseSocketRefUserDO::getUserId).collect(Collectors.toSet());

        if (CollUtil.isEmpty(userIdSet)) {
            return TempBizCodeEnum.OK;
        }

        BaseWebSocketEventBO<NotNullIdAndNotEmptyLongSet> baseWebSocketEventBO = new BaseWebSocketEventBO<>();

        baseWebSocketEventBO.setUserIdSet(userIdSet);

        baseWebSocketEventBO.setSysSocketRefUserIdSet(notEmptyIdSet.getIdSet());

        WebSocketMessageDTO<NotNullIdAndNotEmptyLongSet> webSocketMessageDTO =
            WebSocketMessageDTO.okData(BaseWebSocketUriEnum.BASE_SOCKET_REF_USER_CHANGE_CONSOLE_FLAG_BY_ID_SET, null);

        baseWebSocketEventBO.setWebSocketMessageDTO(webSocketMessageDTO);

        // 发送：webSocket事件
        TempKafkaUtil.sendBaseWebSocketEventTopic(baseWebSocketEventBO);

        return TempBizCodeEnum.OK;

    }

}
