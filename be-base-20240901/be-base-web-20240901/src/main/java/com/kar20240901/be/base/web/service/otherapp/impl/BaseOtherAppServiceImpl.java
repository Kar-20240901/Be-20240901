package com.kar20240901.be.base.web.service.otherapp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.otherapp.BaseOtherAppMapper;
import com.kar20240901.be.base.web.model.annotation.base.MyTransactional;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.otherapp.BaseOtherAppDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.service.otherapp.BaseOtherAppService;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import com.kar20240901.be.base.web.util.base.RedissonUtil;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class BaseOtherAppServiceImpl extends ServiceImpl<BaseOtherAppMapper, BaseOtherAppDO>
    implements BaseOtherAppService {

    /**
     * 新增/修改
     */
    @Override
    @MyTransactional
    public String insertOrUpdate(BaseOtherAppInsertOrUpdateDTO dto) {

        return RedissonUtil.doLock(
            BaseRedisKeyEnum.PRE_OTHER_APP_TYPE_AND_APP_ID.name() + ":" + dto.getType() + ":" + dto.getAppId(), () -> {

                // 同一个类型下，第三方 appId，不能重复
                boolean exists = lambdaQuery().eq(SysOtherAppDO::getAppId, dto.getAppId())
                    .ne(dto.getId() != null, BaseEntity::getId, dto.getId()).eq(SysOtherAppDO::getType, dto.getType())
                    .exists();

                if (exists) {
                    ApiResultVO.errorMsg("操作失败：第三方 appId不能重复");
                }

                if (StrUtil.isNotBlank(dto.getOpenId())) {

                    // 同一个类型下，第三方 openId，不能重复
                    exists = lambdaQuery().eq(SysOtherAppDO::getOpenId, dto.getOpenId())
                        .ne(dto.getId() != null, BaseEntity::getId, dto.getId())
                        .eq(SysOtherAppDO::getType, dto.getType()).exists();

                    if (exists) {
                        ApiResultVO.errorMsg("操作失败：第三方 openId不能重复");
                    }

                }

                SysOtherAppDO sysOtherAppDO = new SysOtherAppDO();

                sysOtherAppDO.setType(dto.getType());
                sysOtherAppDO.setName(dto.getName());
                sysOtherAppDO.setAppId(dto.getAppId());
                sysOtherAppDO.setSecret(dto.getSecret());

                sysOtherAppDO.setSubscribeReplyContent(MyEntityUtil.getNotNullStr(dto.getSubscribeReplyContent()));
                sysOtherAppDO.setTextReplyContent(MyEntityUtil.getNotNullStr(dto.getTextReplyContent()));
                sysOtherAppDO.setImageReplyContent(MyEntityUtil.getNotNullStr(dto.getImageReplyContent()));

                sysOtherAppDO.setQrCode(MyEntityUtil.getNotNullStr(dto.getQrCode()));
                sysOtherAppDO.setOpenId(MyEntityUtil.getNotNullStr(dto.getOpenId()));

                sysOtherAppDO.setId(dto.getId());
                sysOtherAppDO.setEnableFlag(BooleanUtil.isTrue(dto.getEnableFlag()));
                sysOtherAppDO.setDelFlag(false);
                sysOtherAppDO.setRemark(MyEntityUtil.getNotNullStr(dto.getRemark()));

                saveOrUpdate(sysOtherAppDO);

                return BaseBizCodeEnum.OK;

            });

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseOtherAppDO> myPage(BaseOtherAppPageDTO dto) {

        return lambdaQuery().like(StrUtil.isNotBlank(dto.getName()), SysOtherAppDO::getName, dto.getName()) //
            .like(StrUtil.isNotBlank(dto.getAppId()), SysOtherAppDO::getAppId, dto.getAppId()) //
            .like(StrUtil.isNotBlank(dto.getSubscribeReplyContent()), SysOtherAppDO::getSubscribeReplyContent,
                dto.getSubscribeReplyContent()) //
            .like(StrUtil.isNotBlank(dto.getTextReplyContent()), SysOtherAppDO::getTextReplyContent,
                dto.getTextReplyContent()) //
            .like(StrUtil.isNotBlank(dto.getImageReplyContent()), SysOtherAppDO::getImageReplyContent,
                dto.getImageReplyContent()) //
            .like(StrUtil.isNotBlank(dto.getQrCode()), SysOtherAppDO::getQrCode, dto.getQrCode()) //
            .like(StrUtil.isNotBlank(dto.getOpenId()), SysOtherAppDO::getOpenId, dto.getOpenId()) //
            .like(StrUtil.isNotBlank(dto.getRemark()), BaseEntity::getRemark, dto.getRemark()) //
            .eq(dto.getType() != null, SysOtherAppDO::getType, dto.getType()) //
            .eq(dto.getEnableFlag() != null, BaseEntity::getEnableFlag, dto.getEnableFlag()) //
            .in(BaseEntityNoId::getTenantId, dto.getTenantIdSet()) //
            .select(BaseEntity::getId, BaseEntityNoIdSuper::getTenantId, BaseEntityNoId::getEnableFlag,
                BaseEntityNoId::getRemark, BaseEntityNoIdSuper::getCreateId, BaseEntityNoIdSuper::getCreateTime,
                BaseEntityNoIdSuper::getUpdateId, BaseEntityNoIdSuper::getUpdateTime, SysOtherAppDO::getName,
                SysOtherAppDO::getType, SysOtherAppDO::getSubscribeReplyContent, SysOtherAppDO::getQrCode)
            .orderByDesc(BaseEntity::getUpdateTime).page(dto.page(true));

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseOtherAppDO infoById(NotNullId notNullId) {

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

    /**
     * 通过主键id，获取第三方应用名
     */
    @Override
    public String getNameById(NotNullId notNullId) {

        BaseOtherAppDO baseOtherAppDO =
            lambdaQuery().eq(TempEntity::getId, notNullId.getId()).select(BaseOtherAppDO::getName).one();

        if (baseOtherAppDO == null) {
            return "";
        }

        return baseOtherAppDO.getName();

    }

}