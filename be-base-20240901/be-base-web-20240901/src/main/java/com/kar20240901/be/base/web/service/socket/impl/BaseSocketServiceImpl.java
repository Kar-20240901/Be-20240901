package com.kar20240901.be.base.web.service.socket.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.socket.BaseSocketMapper;
import com.kar20240901.be.base.web.model.annotation.base.MyTransactional;
import com.kar20240901.be.base.web.model.domain.TempEntity;
import com.kar20240901.be.base.web.model.domain.TempEntityNoId;
import com.kar20240901.be.base.web.model.domain.socket.BaseSocketDO;
import com.kar20240901.be.base.web.model.dto.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.socket.BaseSocketPageDTO;
import com.kar20240901.be.base.web.service.socket.BaseSocketService;
import com.kar20240901.be.base.web.util.kafka.TempKafkaUtil;
import org.springframework.stereotype.Service;

@Service
public class BaseSocketServiceImpl extends ServiceImpl<BaseSocketMapper, BaseSocketDO> implements BaseSocketService {

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseSocketDO> myPage(BaseSocketPageDTO dto) {

        return lambdaQuery().like(StrUtil.isNotBlank(dto.getScheme()), BaseSocketDO::getScheme, dto.getScheme())
            .like(StrUtil.isNotBlank(dto.getHost()), BaseSocketDO::getHost, dto.getHost()) //
            .eq(dto.getPort() != null, BaseSocketDO::getPort, dto.getPort()) //
            .eq(dto.getType() != null, BaseSocketDO::getType, dto.getType()) //
            .eq(dto.getEnableFlag() != null, TempEntityNoId::getEnableFlag, dto.getEnableFlag()) //
            .eq(dto.getId() != null, TempEntity::getId, dto.getId()) //
            .like(StrUtil.isNotBlank(dto.getRemark()), BaseSocketDO::getRemark, dto.getRemark()) //
            .page(dto.pageOrder());

    }

    /**
     * 批量：禁用socket
     */
    @Override
    public String disableByIdSet(NotEmptyIdSet notEmptyIdSet) {

        if (CollUtil.isEmpty(notEmptyIdSet.getIdSet())) {
            return TempBizCodeEnum.OK;
        }

        lambdaUpdate().in(TempEntity::getId, notEmptyIdSet.getIdSet()).set(TempEntityNoId::getEnableFlag, false)
            .update();

        // 发送消息：socket禁用的 topic
        TempKafkaUtil.sendSocketDisableTopic(notEmptyIdSet.getIdSet());

        return TempBizCodeEnum.OK;

    }

    /**
     * 批量：启用socket
     */
    @Override
    public String enableByIdSet(NotEmptyIdSet notEmptyIdSet) {

        if (CollUtil.isEmpty(notEmptyIdSet.getIdSet())) {
            return TempBizCodeEnum.OK;
        }

        lambdaUpdate().in(TempEntity::getId, notEmptyIdSet.getIdSet()).set(TempEntityNoId::getEnableFlag, true)
            .update();

        // 发送消息：socket启用的 topic
        TempKafkaUtil.sendSocketEnableTopic(notEmptyIdSet.getIdSet());

        return TempBizCodeEnum.OK;

    }

    /**
     * 批量：删除socket
     */
    @Override
    @MyTransactional
    public String deleteByIdSet(NotEmptyIdSet notEmptyIdSet) {

        removeBatchByIds(notEmptyIdSet.getIdSet());

        // 发送消息：socket禁用的 topic
        TempKafkaUtil.sendSocketDisableTopic(notEmptyIdSet.getIdSet());

        return TempBizCodeEnum.OK;

    }

}
