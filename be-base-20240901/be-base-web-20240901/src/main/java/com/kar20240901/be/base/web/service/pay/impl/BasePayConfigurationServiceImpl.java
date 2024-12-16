package com.kar20240901.be.base.web.service.pay.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.pay.BasePayConfigurationMapper;
import com.kar20240901.be.base.web.model.annotation.base.MyTransactional;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoId;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoIdSuper;
import com.kar20240901.be.base.web.model.domain.pay.BasePayConfigurationDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.pay.BasePayConfigurationInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.pay.BasePayConfigurationPageDTO;
import com.kar20240901.be.base.web.model.vo.base.DictVO;
import com.kar20240901.be.base.web.service.pay.BasePayConfigurationService;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class BasePayConfigurationServiceImpl extends ServiceImpl<BasePayConfigurationMapper, BasePayConfigurationDO>
    implements BasePayConfigurationService {

    /**
     * 新增/修改
     */
    @Override
    @MyTransactional
    public String insertOrUpdate(BasePayConfigurationInsertOrUpdateDTO dto) {

        // 每个支付方式，需要单独检查 dto
        Consumer<BasePayConfigurationInsertOrUpdateDTO> checkBasePayConfigurationInsertOrUpdateDtoConsumer =
            dto.getType().getCheckBasePayConfigurationInsertOrUpdateDtoConsumer();

        if (checkBasePayConfigurationInsertOrUpdateDtoConsumer != null) {
            checkBasePayConfigurationInsertOrUpdateDtoConsumer.accept(dto);
        }

        // 如果是默认支付方式，则取消之前的默认支付方式
        if (BooleanUtil.isTrue(dto.getDefaultFlag())) {

            lambdaUpdate().set(BasePayConfigurationDO::getDefaultFlag, false)
                .eq(BasePayConfigurationDO::getDefaultFlag, true)
                .ne(dto.getId() != null, BasePayConfigurationDO::getId, dto.getId()).update();

        }

        BasePayConfigurationDO basePayConfigurationDO = new BasePayConfigurationDO();

        basePayConfigurationDO.setDefaultFlag(BooleanUtil.isTrue(dto.getDefaultFlag()));

        basePayConfigurationDO.setType(dto.getType().getCode());
        basePayConfigurationDO.setName(dto.getName());
        basePayConfigurationDO.setServerUrl(MyEntityUtil.getNotNullStr(dto.getServerUrl()));
        basePayConfigurationDO.setAppId(MyEntityUtil.getNotNullStr(dto.getAppId()));
        basePayConfigurationDO.setPrivateKey(MyEntityUtil.getNotNullStr(dto.getPrivateKey()));

        basePayConfigurationDO.setPlatformPublicKey(MyEntityUtil.getNotNullStr(dto.getPlatformPublicKey()));
        basePayConfigurationDO.setNotifyUrl(MyEntityUtil.getNotNullStr(dto.getNotifyUrl()));
        basePayConfigurationDO.setMerchantId(MyEntityUtil.getNotNullStr(dto.getMerchantId()));
        basePayConfigurationDO.setMerchantSerialNumber(MyEntityUtil.getNotNullStr(dto.getMerchantSerialNumber()));
        basePayConfigurationDO.setApiV3Key(MyEntityUtil.getNotNullStr(dto.getApiV3Key()));

        basePayConfigurationDO.setId(dto.getId());
        basePayConfigurationDO.setEnableFlag(BooleanUtil.isTrue(dto.getEnableFlag()));
        basePayConfigurationDO.setRemark(MyEntityUtil.getNotNullStr(dto.getRemark()));

        saveOrUpdate(basePayConfigurationDO);

        return TempBizCodeEnum.OK;

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BasePayConfigurationDO> myPage(BasePayConfigurationPageDTO dto) {

        return lambdaQuery().like(StrUtil.isNotBlank(dto.getName()), BasePayConfigurationDO::getName, dto.getName())
            .like(StrUtil.isNotBlank(dto.getAppId()), BasePayConfigurationDO::getAppId, dto.getAppId())
            .like(StrUtil.isNotBlank(dto.getRemark()), TempEntityNoId::getRemark, dto.getRemark())
            .eq(dto.getType() != null, BasePayConfigurationDO::getType, dto.getType())
            .eq(dto.getDefaultFlag() != null, BasePayConfigurationDO::getDefaultFlag, dto.getDefaultFlag())
            .eq(dto.getEnableFlag() != null, TempEntityNoId::getEnableFlag, dto.getEnableFlag())
            .select(TempEntity::getId, BasePayConfigurationDO::getAppId, BasePayConfigurationDO::getType,
                BasePayConfigurationDO::getName, TempEntityNoIdSuper::getCreateId, TempEntityNoIdSuper::getCreateTime,
                TempEntityNoIdSuper::getUpdateId, TempEntityNoIdSuper::getUpdateTime, TempEntityNoId::getEnableFlag,
                TempEntityNoId::getRemark, BasePayConfigurationDO::getDefaultFlag)
            .page(dto.updateTimeDescDefaultOrderPage());

    }

    /**
     * 下拉列表
     */
    @Override
    public Page<DictVO> dictList() {

        List<BasePayConfigurationDO> basePayConfigurationDOList =
            lambdaQuery().select(TempEntity::getId, BasePayConfigurationDO::getName)
                .orderByDesc(TempEntityNoIdSuper::getUpdateTime).list();

        List<DictVO> dictVOList = basePayConfigurationDOList.stream().map(it -> new DictVO(it.getId(), it.getName()))
            .collect(Collectors.toList());

        return new Page<DictVO>().setTotal(dictVOList.size()).setRecords(dictVOList);

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BasePayConfigurationDO infoById(NotNullId notNullId) {

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
