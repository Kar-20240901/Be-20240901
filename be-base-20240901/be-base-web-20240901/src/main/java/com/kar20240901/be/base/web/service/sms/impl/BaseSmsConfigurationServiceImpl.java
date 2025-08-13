package com.kar20240901.be.base.web.service.sms.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.sms.BaseSmsConfigurationMapper;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoId;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoIdSuper;
import com.kar20240901.be.base.web.model.domain.sms.BaseSmsConfigurationDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.sms.BaseSmsConfigurationInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.sms.BaseSmsConfigurationPageDTO;
import com.kar20240901.be.base.web.service.sms.BaseSmsConfigurationService;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class BaseSmsConfigurationServiceImpl extends ServiceImpl<BaseSmsConfigurationMapper, BaseSmsConfigurationDO>
    implements BaseSmsConfigurationService {

    /**
     * 新增/修改
     */
    @Override
    @DSTransactional
    public String insertOrUpdate(BaseSmsConfigurationInsertOrUpdateDTO dto) {

        // 如果是默认支付方式，则取消之前的默认支付方式
        if (BooleanUtil.isTrue(dto.getDefaultFlag())) {

            lambdaUpdate().set(BaseSmsConfigurationDO::getDefaultFlag, false)
                .eq(BaseSmsConfigurationDO::getDefaultFlag, true)
                .ne(dto.getId() != null, TempEntity::getId, dto.getId()).update();

        }

        BaseSmsConfigurationDO baseSmsConfigurationDO = new BaseSmsConfigurationDO();

        baseSmsConfigurationDO.setDefaultFlag(BooleanUtil.isTrue(dto.getDefaultFlag()));

        baseSmsConfigurationDO.setType(dto.getType());
        baseSmsConfigurationDO.setName(dto.getName());

        baseSmsConfigurationDO.setSecretId(MyEntityUtil.getNotNullStr(dto.getSecretId()));
        baseSmsConfigurationDO.setSecretKey(MyEntityUtil.getNotNullStr(dto.getSecretKey()));
        baseSmsConfigurationDO.setSdkAppId(MyEntityUtil.getNotNullStr(dto.getSdkAppId()));
        baseSmsConfigurationDO.setSignName(MyEntityUtil.getNotNullStr(dto.getSignName()));

        baseSmsConfigurationDO.setId(dto.getId());
        baseSmsConfigurationDO.setEnableFlag(BooleanUtil.isTrue(dto.getEnableFlag()));
        baseSmsConfigurationDO.setRemark(MyEntityUtil.getNotNullStr(dto.getRemark()));

        baseSmsConfigurationDO.setSendCommon(MyEntityUtil.getNotNullStr(dto.getSendCommon()));
        baseSmsConfigurationDO.setSendSignUp(MyEntityUtil.getNotNullStr(dto.getSendSignUp()));
        baseSmsConfigurationDO.setSendSignIn(MyEntityUtil.getNotNullStr(dto.getSendSignIn()));
        baseSmsConfigurationDO.setSendSetPassword(MyEntityUtil.getNotNullStr(dto.getSendSetPassword()));
        baseSmsConfigurationDO.setSendUpdatePassword(MyEntityUtil.getNotNullStr(dto.getSendUpdatePassword()));
        baseSmsConfigurationDO.setSendSetUserName(MyEntityUtil.getNotNullStr(dto.getSendSetUserName()));
        baseSmsConfigurationDO.setSendUpdateUserName(MyEntityUtil.getNotNullStr(dto.getSendUpdateUserName()));
        baseSmsConfigurationDO.setSendSetEmail(MyEntityUtil.getNotNullStr(dto.getSendSetEmail()));
        baseSmsConfigurationDO.setSendUpdateEmail(MyEntityUtil.getNotNullStr(dto.getSendUpdateEmail()));
        baseSmsConfigurationDO.setSendSetWx(MyEntityUtil.getNotNullStr(dto.getSendSetWx()));
        baseSmsConfigurationDO.setSendUpdateWx(MyEntityUtil.getNotNullStr(dto.getSendUpdateWx()));
        baseSmsConfigurationDO.setSendSetPhone(MyEntityUtil.getNotNullStr(dto.getSendSetPhone()));
        baseSmsConfigurationDO.setSendUpdatePhone(MyEntityUtil.getNotNullStr(dto.getSendUpdatePhone()));
        baseSmsConfigurationDO.setSendForgetPassword(MyEntityUtil.getNotNullStr(dto.getSendForgetPassword()));
        baseSmsConfigurationDO.setSendSignDelete(MyEntityUtil.getNotNullStr(dto.getSendSignDelete()));

        saveOrUpdate(baseSmsConfigurationDO);

        return TempBizCodeEnum.OK;

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseSmsConfigurationDO> myPage(BaseSmsConfigurationPageDTO dto) {

        return lambdaQuery().like(StrUtil.isNotBlank(dto.getName()), BaseSmsConfigurationDO::getName, dto.getName())
            .like(StrUtil.isNotBlank(dto.getRemark()), TempEntityNoId::getRemark, dto.getRemark())
            .eq(dto.getType() != null, BaseSmsConfigurationDO::getType, dto.getType())
            .eq(dto.getDefaultFlag() != null, BaseSmsConfigurationDO::getDefaultFlag, dto.getDefaultFlag())
            .eq(dto.getEnableFlag() != null, TempEntityNoId::getEnableFlag, dto.getEnableFlag())
            .select(TempEntity::getId, BaseSmsConfigurationDO::getType, BaseSmsConfigurationDO::getName,
                TempEntityNoIdSuper::getCreateTime, TempEntityNoIdSuper::getUpdateTime, TempEntityNoId::getEnableFlag,
                BaseSmsConfigurationDO::getDefaultFlag).page(dto.createTimeDescDefaultOrderPage());

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseSmsConfigurationDO infoById(NotNullId notNullId) {

        return lambdaQuery().eq(TempEntity::getId, notNullId.getId()).one();

    }

    /**
     * 批量删除
     */
    @Override
    public String deleteByIdSet(NotEmptyIdSet notEmptyIdSet) {

        Set<Long> idSet = notEmptyIdSet.getIdSet();

        if (CollUtil.isEmpty(idSet)) {
            return TempBizCodeEnum.OK;
        }

        removeByIds(idSet); // 根据 idSet删除

        return TempBizCodeEnum.OK;

    }

}
