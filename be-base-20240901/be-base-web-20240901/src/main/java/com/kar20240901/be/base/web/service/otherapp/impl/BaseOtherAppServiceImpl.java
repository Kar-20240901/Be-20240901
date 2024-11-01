package com.kar20240901.be.base.web.service.otherapp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.otherapp.BaseOtherAppMapper;
import com.kar20240901.be.base.web.model.annotation.base.MyTransactional;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoId;
import com.kar20240901.be.base.web.model.domain.otherapp.BaseOtherAppDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.otherapp.BaseOtherAppInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.otherapp.BaseOtherAppPageDTO;
import com.kar20240901.be.base.web.model.enums.base.BaseRedisKeyEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
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
                boolean exists = lambdaQuery().eq(BaseOtherAppDO::getAppId, dto.getAppId())
                    .ne(dto.getId() != null, TempEntity::getId, dto.getId()).eq(BaseOtherAppDO::getType, dto.getType())
                    .exists();

                if (exists) {
                    R.errorMsg("操作失败：第三方 appId不能重复");
                }

                if (StrUtil.isNotBlank(dto.getOpenId())) {

                    // 同一个类型下，第三方 openId，不能重复
                    exists = lambdaQuery().eq(BaseOtherAppDO::getOpenId, dto.getOpenId())
                        .ne(dto.getId() != null, TempEntity::getId, dto.getId())
                        .eq(BaseOtherAppDO::getType, dto.getType()).exists();

                    if (exists) {
                        R.errorMsg("操作失败：第三方 openId不能重复");
                    }

                }

                BaseOtherAppDO baseOtherAppDO = new BaseOtherAppDO();

                baseOtherAppDO.setType(dto.getType());
                baseOtherAppDO.setName(dto.getName());
                baseOtherAppDO.setAppId(dto.getAppId());
                baseOtherAppDO.setSecret(dto.getSecret());

                baseOtherAppDO.setSubscribeReplyContent(MyEntityUtil.getNotNullStr(dto.getSubscribeReplyContent()));
                baseOtherAppDO.setTextReplyContent(MyEntityUtil.getNotNullStr(dto.getTextReplyContent()));
                baseOtherAppDO.setImageReplyContent(MyEntityUtil.getNotNullStr(dto.getImageReplyContent()));

                baseOtherAppDO.setQrCode(MyEntityUtil.getNotNullStr(dto.getQrCode()));
                baseOtherAppDO.setOpenId(MyEntityUtil.getNotNullStr(dto.getOpenId()));

                baseOtherAppDO.setId(dto.getId());
                baseOtherAppDO.setEnableFlag(BooleanUtil.isTrue(dto.getEnableFlag()));
                baseOtherAppDO.setRemark(MyEntityUtil.getNotNullStr(dto.getRemark()));

                saveOrUpdate(baseOtherAppDO);

                return TempBizCodeEnum.OK;

            });

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseOtherAppDO> myPage(BaseOtherAppPageDTO dto) {

        return lambdaQuery().like(StrUtil.isNotBlank(dto.getName()), BaseOtherAppDO::getName, dto.getName()) //
            .like(StrUtil.isNotBlank(dto.getAppId()), BaseOtherAppDO::getAppId, dto.getAppId()) //
            .like(StrUtil.isNotBlank(dto.getSubscribeReplyContent()), BaseOtherAppDO::getSubscribeReplyContent,
                dto.getSubscribeReplyContent()) //
            .like(StrUtil.isNotBlank(dto.getTextReplyContent()), BaseOtherAppDO::getTextReplyContent,
                dto.getTextReplyContent()) //
            .like(StrUtil.isNotBlank(dto.getImageReplyContent()), BaseOtherAppDO::getImageReplyContent,
                dto.getImageReplyContent()) //
            .like(StrUtil.isNotBlank(dto.getQrCode()), BaseOtherAppDO::getQrCode, dto.getQrCode()) //
            .like(StrUtil.isNotBlank(dto.getOpenId()), BaseOtherAppDO::getOpenId, dto.getOpenId()) //
            .like(StrUtil.isNotBlank(dto.getRemark()), TempEntity::getRemark, dto.getRemark()) //
            .eq(dto.getType() != null, BaseOtherAppDO::getType, dto.getType()) //
            .eq(dto.getEnableFlag() != null, TempEntity::getEnableFlag, dto.getEnableFlag()) //
            .select(TempEntity::getId, TempEntityNoId::getEnableFlag, TempEntityNoId::getCreateTime,
                TempEntityNoId::getUpdateTime, BaseOtherAppDO::getName, BaseOtherAppDO::getType,
                BaseOtherAppDO::getSubscribeReplyContent, BaseOtherAppDO::getQrCode)
            .page(dto.createTimeDescDefaultOrderPage());

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
