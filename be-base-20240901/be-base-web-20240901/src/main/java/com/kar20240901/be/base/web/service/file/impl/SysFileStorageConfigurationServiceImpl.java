package com.kar20240901.be.base.web.service.file.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.file.SysFileStorageConfigurationMapper;
import com.kar20240901.be.base.web.model.annotation.base.MyTransactional;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoId;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoIdSuper;
import com.kar20240901.be.base.web.model.domain.file.SysFileStorageConfigurationDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.file.SysFileStorageConfigurationInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.file.SysFileStorageConfigurationPageDTO;
import com.kar20240901.be.base.web.service.file.SysFileStorageConfigurationService;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class SysFileStorageConfigurationServiceImpl
    extends ServiceImpl<SysFileStorageConfigurationMapper, SysFileStorageConfigurationDO>
    implements SysFileStorageConfigurationService {

    /**
     * 新增/修改
     */
    @Override
    @MyTransactional
    public String insertOrUpdate(SysFileStorageConfigurationInsertOrUpdateDTO dto) {

        // 如果是默认文件存储，则取消之前的默认文件存储
        if (BooleanUtil.isTrue(dto.getDefaultFlag())) {

            lambdaUpdate().set(SysFileStorageConfigurationDO::getDefaultFlag, false)
                .eq(SysFileStorageConfigurationDO::getDefaultFlag, true)
                .ne(dto.getId() != null, TempEntity::getId, dto.getId()).update();

        }

        SysFileStorageConfigurationDO sysFileStorageConfigurationDO = new SysFileStorageConfigurationDO();

        sysFileStorageConfigurationDO.setName(dto.getName());
        sysFileStorageConfigurationDO.setType(dto.getType());
        sysFileStorageConfigurationDO.setAccessKey(dto.getAccessKey());
        sysFileStorageConfigurationDO.setSecretKey(dto.getSecretKey());
        sysFileStorageConfigurationDO.setUploadEndpoint(dto.getUploadEndpoint());
        sysFileStorageConfigurationDO.setPublicDownloadEndpoint(dto.getPublicDownloadEndpoint());
        sysFileStorageConfigurationDO.setBucketPublicName(dto.getBucketPublicName());
        sysFileStorageConfigurationDO.setBucketPrivateName(dto.getBucketPrivateName());
        sysFileStorageConfigurationDO.setDefaultFlag(BooleanUtil.isTrue(dto.getDefaultFlag()));

        sysFileStorageConfigurationDO.setId(dto.getId());
        sysFileStorageConfigurationDO.setEnableFlag(BooleanUtil.isTrue(dto.getEnableFlag()));
        sysFileStorageConfigurationDO.setRemark(MyEntityUtil.getNotNullStr(dto.getRemark()));

        saveOrUpdate(sysFileStorageConfigurationDO);

        return TempBizCodeEnum.OK;

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<SysFileStorageConfigurationDO> myPage(SysFileStorageConfigurationPageDTO dto) {

        return lambdaQuery() //
            .like(StrUtil.isNotBlank(dto.getName()), SysFileStorageConfigurationDO::getName, dto.getName()) //
            .like(StrUtil.isNotBlank(dto.getAccessKey()), SysFileStorageConfigurationDO::getAccessKey,
                dto.getAccessKey()) //
            .like(StrUtil.isNotBlank(dto.getUploadEndpoint()), SysFileStorageConfigurationDO::getUploadEndpoint,
                dto.getUploadEndpoint()) //
            .like(StrUtil.isNotBlank(dto.getPublicDownloadEndpoint()),
                SysFileStorageConfigurationDO::getPublicDownloadEndpoint, dto.getPublicDownloadEndpoint()) //
            .like(StrUtil.isNotBlank(dto.getBucketPublicName()), SysFileStorageConfigurationDO::getBucketPublicName,
                dto.getBucketPublicName()) //
            .like(StrUtil.isNotBlank(dto.getBucketPrivateName()), SysFileStorageConfigurationDO::getBucketPrivateName,
                dto.getBucketPrivateName()) //
            .like(StrUtil.isNotBlank(dto.getRemark()), TempEntity::getRemark, dto.getRemark())
            .eq(dto.getType() != null, SysFileStorageConfigurationDO::getType, dto.getType())
            .eq(dto.getDefaultFlag() != null, SysFileStorageConfigurationDO::getDefaultFlag, dto.getDefaultFlag())
            .eq(dto.getEnableFlag() != null, TempEntity::getEnableFlag, dto.getEnableFlag())
            .select(TempEntity::getId, TempEntityNoId::getEnableFlag, TempEntityNoId::getRemark,
                TempEntityNoIdSuper::getCreateId, TempEntityNoIdSuper::getCreateTime, TempEntityNoIdSuper::getUpdateId,
                TempEntityNoIdSuper::getUpdateTime, SysFileStorageConfigurationDO::getName,
                SysFileStorageConfigurationDO::getType, SysFileStorageConfigurationDO::getDefaultFlag)
            .orderByDesc(TempEntity::getUpdateTime).page(dto.pageOrder());

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public SysFileStorageConfigurationDO infoById(NotNullId notNullId) {

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
