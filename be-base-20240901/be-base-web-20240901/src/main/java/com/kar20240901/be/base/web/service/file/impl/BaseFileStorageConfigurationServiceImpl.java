package com.kar20240901.be.base.web.service.file.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.file.BaseFileStorageConfigurationMapper;
import com.kar20240901.be.base.web.model.bo.base.BaseDeleteLocalCacheBO;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoId;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoIdSuper;
import com.kar20240901.be.base.web.model.domain.file.BaseFileStorageConfigurationDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.file.BaseFileStorageConfigurationInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileStorageConfigurationPageDTO;
import com.kar20240901.be.base.web.model.enums.base.BaseDeleteLocalCacheTypeEnum;
import com.kar20240901.be.base.web.service.file.BaseFileStorageConfigurationService;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import com.kar20240901.be.base.web.util.kafka.TempKafkaUtil;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class BaseFileStorageConfigurationServiceImpl
    extends ServiceImpl<BaseFileStorageConfigurationMapper, BaseFileStorageConfigurationDO>
    implements BaseFileStorageConfigurationService {

    /**
     * 新增/修改
     */
    @Override
    @DSTransactional
    public String insertOrUpdate(BaseFileStorageConfigurationInsertOrUpdateDTO dto) {

        // 如果是默认文件存储，则取消之前的默认文件存储
        if (BooleanUtil.isTrue(dto.getDefaultFlag())) {

            lambdaUpdate().set(BaseFileStorageConfigurationDO::getDefaultFlag, false)
                .eq(BaseFileStorageConfigurationDO::getDefaultFlag, true)
                .ne(dto.getId() != null, TempEntity::getId, dto.getId()).update();

        }

        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO = new BaseFileStorageConfigurationDO();

        baseFileStorageConfigurationDO.setName(dto.getName());
        baseFileStorageConfigurationDO.setType(dto.getType());
        baseFileStorageConfigurationDO.setAccessKey(dto.getAccessKey());
        baseFileStorageConfigurationDO.setSecretKey(dto.getSecretKey());
        baseFileStorageConfigurationDO.setUploadEndpoint(dto.getUploadEndpoint());
        baseFileStorageConfigurationDO.setPublicDownloadEndpoint(dto.getPublicDownloadEndpoint());
        baseFileStorageConfigurationDO.setBucketPublicName(dto.getBucketPublicName());
        baseFileStorageConfigurationDO.setBucketPrivateName(dto.getBucketPrivateName());
        baseFileStorageConfigurationDO.setDefaultFlag(BooleanUtil.isTrue(dto.getDefaultFlag()));

        baseFileStorageConfigurationDO.setId(dto.getId());
        baseFileStorageConfigurationDO.setEnableFlag(BooleanUtil.isTrue(dto.getEnableFlag()));
        baseFileStorageConfigurationDO.setRemark(MyEntityUtil.getNotNullStr(dto.getRemark()));

        baseFileStorageConfigurationDO.setCustomDomain(MyEntityUtil.getNotNullStr(dto.getCustomDomain()));

        saveOrUpdate(baseFileStorageConfigurationDO);

        if (dto.getId() != null) {

            // 删除客户端缓存
            TempKafkaUtil.sendDeleteLocalCacheTopic(
                new BaseDeleteLocalCacheBO(BaseDeleteLocalCacheTypeEnum.DELETE_FILE_SYSTEM_CLIENT_CACHE.getCode(),
                    CollUtil.newHashSet(dto.getId())));

        }

        return TempBizCodeEnum.OK;

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseFileStorageConfigurationDO> myPage(BaseFileStorageConfigurationPageDTO dto) {

        return lambdaQuery() //
            .like(StrUtil.isNotBlank(dto.getName()), BaseFileStorageConfigurationDO::getName, dto.getName()) //
            .like(StrUtil.isNotBlank(dto.getAccessKey()), BaseFileStorageConfigurationDO::getAccessKey,
                dto.getAccessKey()) //
            .like(StrUtil.isNotBlank(dto.getUploadEndpoint()), BaseFileStorageConfigurationDO::getUploadEndpoint,
                dto.getUploadEndpoint()) //
            .like(StrUtil.isNotBlank(dto.getPublicDownloadEndpoint()),
                BaseFileStorageConfigurationDO::getPublicDownloadEndpoint, dto.getPublicDownloadEndpoint()) //
            .like(StrUtil.isNotBlank(dto.getBucketPublicName()), BaseFileStorageConfigurationDO::getBucketPublicName,
                dto.getBucketPublicName()) //
            .like(StrUtil.isNotBlank(dto.getBucketPrivateName()), BaseFileStorageConfigurationDO::getBucketPrivateName,
                dto.getBucketPrivateName()) //
            .like(StrUtil.isNotBlank(dto.getRemark()), TempEntity::getRemark, dto.getRemark())
            .eq(dto.getType() != null, BaseFileStorageConfigurationDO::getType, dto.getType())
            .eq(dto.getDefaultFlag() != null, BaseFileStorageConfigurationDO::getDefaultFlag, dto.getDefaultFlag())
            .eq(dto.getEnableFlag() != null, TempEntity::getEnableFlag, dto.getEnableFlag())
            .select(TempEntity::getId, TempEntityNoId::getEnableFlag, TempEntityNoId::getRemark,
                TempEntityNoIdSuper::getCreateId, TempEntityNoIdSuper::getCreateTime, TempEntityNoIdSuper::getUpdateId,
                TempEntityNoIdSuper::getUpdateTime, BaseFileStorageConfigurationDO::getName,
                BaseFileStorageConfigurationDO::getType, BaseFileStorageConfigurationDO::getDefaultFlag)
            .page(dto.updateTimeDescDefaultOrderPage());

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseFileStorageConfigurationDO infoById(NotNullId notNullId) {

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

        // 删除客户端缓存
        TempKafkaUtil.sendDeleteLocalCacheTopic(
            new BaseDeleteLocalCacheBO(BaseDeleteLocalCacheTypeEnum.DELETE_FILE_SYSTEM_CLIENT_CACHE.getCode(), idSet));

        return TempBizCodeEnum.OK;

    }

}
