package com.kar20240901.be.base.web.service.thirdapp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.thirdapp.BaseThirdAppMapper;
import com.kar20240901.be.base.web.model.annotation.base.MyTransactional;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoId;
import com.kar20240901.be.base.web.model.domain.thirdapp.BaseThirdAppDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.thirdapp.BaseThirdAppInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.thirdapp.BaseThirdAppPageDTO;
import com.kar20240901.be.base.web.model.enums.base.BaseRedisKeyEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.thirdapp.BaseThirdAppService;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import com.kar20240901.be.base.web.util.base.RedissonUtil;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class BaseThirdAppServiceImpl extends ServiceImpl<BaseThirdAppMapper, BaseThirdAppDO>
    implements BaseThirdAppService {

    /**
     * 新增/修改
     */
    @Override
    @MyTransactional
    public String insertOrUpdate(BaseThirdAppInsertOrUpdateDTO dto) {

        return RedissonUtil.doLock(
            BaseRedisKeyEnum.PRE_THIRD_APP_TYPE_AND_APP_ID.name() + ":" + dto.getType() + ":" + dto.getAppId(), () -> {

                // 同一个类型下，第三方 appId，不能重复
                boolean exists = lambdaQuery().eq(BaseThirdAppDO::getAppId, dto.getAppId())
                    .ne(dto.getId() != null, TempEntity::getId, dto.getId()).eq(BaseThirdAppDO::getType, dto.getType())
                    .exists();

                if (exists) {
                    R.errorMsg("操作失败：第三方 appId不能重复");
                }

                if (StrUtil.isNotBlank(dto.getOpenId())) {

                    // 同一个类型下，第三方 openId，不能重复
                    exists = lambdaQuery().eq(BaseThirdAppDO::getOpenId, dto.getOpenId())
                        .ne(dto.getId() != null, TempEntity::getId, dto.getId())
                        .eq(BaseThirdAppDO::getType, dto.getType()).exists();

                    if (exists) {
                        R.errorMsg("操作失败：第三方 openId不能重复");
                    }

                }

                BaseThirdAppDO baseThirdAppDO = new BaseThirdAppDO();

                baseThirdAppDO.setType(dto.getType());
                baseThirdAppDO.setName(dto.getName());
                baseThirdAppDO.setAppId(dto.getAppId());
                baseThirdAppDO.setSecret(dto.getSecret());

                baseThirdAppDO.setSubscribeReplyContent(MyEntityUtil.getNotNullStr(dto.getSubscribeReplyContent()));
                baseThirdAppDO.setTextReplyContent(MyEntityUtil.getNotNullStr(dto.getTextReplyContent()));
                baseThirdAppDO.setImageReplyContent(MyEntityUtil.getNotNullStr(dto.getImageReplyContent()));

                baseThirdAppDO.setQrCode(MyEntityUtil.getNotNullStr(dto.getQrCode()));
                baseThirdAppDO.setOpenId(MyEntityUtil.getNotNullStr(dto.getOpenId()));

                baseThirdAppDO.setId(dto.getId());
                baseThirdAppDO.setEnableFlag(BooleanUtil.isTrue(dto.getEnableFlag()));
                baseThirdAppDO.setRemark(MyEntityUtil.getNotNullStr(dto.getRemark()));

                saveOrUpdate(baseThirdAppDO);

                return TempBizCodeEnum.OK;

            });

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseThirdAppDO> myPage(BaseThirdAppPageDTO dto) {

        return lambdaQuery().like(StrUtil.isNotBlank(dto.getName()), BaseThirdAppDO::getName, dto.getName()) //
            .like(StrUtil.isNotBlank(dto.getAppId()), BaseThirdAppDO::getAppId, dto.getAppId()) //
            .like(StrUtil.isNotBlank(dto.getSubscribeReplyContent()), BaseThirdAppDO::getSubscribeReplyContent,
                dto.getSubscribeReplyContent()) //
            .like(StrUtil.isNotBlank(dto.getTextReplyContent()), BaseThirdAppDO::getTextReplyContent,
                dto.getTextReplyContent()) //
            .like(StrUtil.isNotBlank(dto.getImageReplyContent()), BaseThirdAppDO::getImageReplyContent,
                dto.getImageReplyContent()) //
            .like(StrUtil.isNotBlank(dto.getQrCode()), BaseThirdAppDO::getQrCode, dto.getQrCode()) //
            .like(StrUtil.isNotBlank(dto.getOpenId()), BaseThirdAppDO::getOpenId, dto.getOpenId()) //
            .like(StrUtil.isNotBlank(dto.getRemark()), TempEntity::getRemark, dto.getRemark()) //
            .eq(dto.getType() != null, BaseThirdAppDO::getType, dto.getType()) //
            .eq(dto.getEnableFlag() != null, TempEntity::getEnableFlag, dto.getEnableFlag()) //
            .select(TempEntity::getId, TempEntityNoId::getEnableFlag, TempEntityNoId::getCreateTime,
                TempEntityNoId::getUpdateTime, BaseThirdAppDO::getName, BaseThirdAppDO::getType,
                BaseThirdAppDO::getSubscribeReplyContent, BaseThirdAppDO::getQrCode)
            .page(dto.createTimeDescDefaultOrderPage());

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseThirdAppDO infoById(NotNullId notNullId) {

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

        BaseThirdAppDO baseThirdAppDO =
            lambdaQuery().eq(TempEntity::getId, notNullId.getId()).select(BaseThirdAppDO::getName).one();

        if (baseThirdAppDO == null) {
            return "";
        }

        return baseThirdAppDO.getName();

    }

}
