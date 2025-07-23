package com.kar20240901.be.base.web.service.base.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.base.BaseUserDeleteLogMapper;
import com.kar20240901.be.base.web.model.annotation.base.MyTransactional;
import com.kar20240901.be.base.web.model.domain.base.BaseUserDeleteLogDO;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoIdSuper;
import com.kar20240901.be.base.web.model.dto.base.BaseUserDeleteLogPageDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.service.base.BaseUserDeleteLogService;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class BaseUserDeleteLogServiceImpl extends ServiceImpl<BaseUserDeleteLogMapper, BaseUserDeleteLogDO>
    implements BaseUserDeleteLogService {

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseUserDeleteLogDO> myPage(BaseUserDeleteLogPageDTO dto) {

        return lambdaQuery().eq(dto.getId() != null, BaseUserDeleteLogDO::getId, dto.getId())
            .like(StrUtil.isNotBlank(dto.getEmail()), BaseUserDeleteLogDO::getEmail, dto.getEmail())
            .like(StrUtil.isNotBlank(dto.getUsername()), BaseUserDeleteLogDO::getUsername, dto.getUsername())
            .like(StrUtil.isNotBlank(dto.getPhone()), BaseUserDeleteLogDO::getPhone, dto.getPhone())
            .like(StrUtil.isNotBlank(dto.getWxOpenId()), BaseUserDeleteLogDO::getWxOpenId, dto.getWxOpenId())
            .like(StrUtil.isNotBlank(dto.getWxAppId()), BaseUserDeleteLogDO::getWxAppId, dto.getWxAppId())
            .like(StrUtil.isNotBlank(dto.getUuid()), BaseUserDeleteLogDO::getUuid, dto.getUuid())
            .like(StrUtil.isNotBlank(dto.getNickname()), BaseUserDeleteLogDO::getNickname, dto.getNickname())
            .select(BaseUserDeleteLogDO::getId, TempEntityNoIdSuper::getCreateTime, BaseUserDeleteLogDO::getNickname,
                BaseUserDeleteLogDO::getUserCreateTime, BaseUserDeleteLogDO::getSignUpType)
            .page(dto.createTimeDescDefaultOrderPage());

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseUserDeleteLogDO infoById(NotNullId notNullId) {

        return lambdaQuery().eq(BaseUserDeleteLogDO::getId, notNullId.getId()).one();

    }

    /**
     * 批量删除
     */
    @Override
    @MyTransactional
    public String deleteByIdSet(NotEmptyIdSet notEmptyIdSet) {

        Set<Long> idSet = notEmptyIdSet.getIdSet();

        if (CollUtil.isEmpty(idSet)) {
            return TempBizCodeEnum.OK;
        }

        removeByIds(idSet); // 根据 idSet删除

        return TempBizCodeEnum.OK;

    }

}
