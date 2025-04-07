package com.kar20240901.be.base.web.service.bulletin.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper;
import com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinReadTimeRefUserMapper;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.bulletin.BaseBulletinDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.bulletin.BaseBulletinInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.bulletin.BaseBulletinPageDTO;
import com.kar20240901.be.base.web.model.dto.bulletin.BaseBulletinUserSelfPageDTO;
import com.kar20240901.be.base.web.model.enums.base.BaseRedisKeyEnum;
import com.kar20240901.be.base.web.model.enums.bulletin.BaseBulletinStatusEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.bulletin.BaseBulletinService;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.base.RedissonUtil;
import java.util.Date;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BaseBulletinServiceImpl extends ServiceImpl<BaseBulletinMapper, BaseBulletinDO>
    implements BaseBulletinService {

    @Resource
    BaseBulletinReadTimeRefUserMapper baseBulletinReadTimeRefUserMapper;

    /**
     * 新增/修改
     */
    @Override
    public String insertOrUpdate(BaseBulletinInsertOrUpdateDTO dto) {

        if (dto.getId() == null) {

            doInsertOrUpdate(dto);

        } else {

            RedissonUtil.doLock(BaseRedisKeyEnum.PRE_BULLETIN_ID + ":" + dto.getId(), () -> {

                doInsertOrUpdate(dto);

            });

        }

        return TempBizCodeEnum.OK;

    }

    /**
     * 执行：新增/修改
     */
    private void doInsertOrUpdate(BaseBulletinInsertOrUpdateDTO dto) {

        if (dto.getId() != null) {

            boolean exists = lambdaQuery().eq(BaseBulletinDO::getId, dto.getId())
                .eq(BaseBulletinDO::getStatus, BaseBulletinStatusEnum.DRAFT).exists();

            if (!exists) {
                R.errorMsg("操作失败：只能修改草稿状态的公告");
            }

        }

        BaseBulletinDO baseBulletinDO = new BaseBulletinDO();

        baseBulletinDO.setType(dto.getType());
        baseBulletinDO.setContent(dto.getContent());
        baseBulletinDO.setTitle(dto.getTitle());
        baseBulletinDO.setPublishTime(dto.getPublishTime());
        baseBulletinDO.setStatus(BaseBulletinStatusEnum.DRAFT);
        baseBulletinDO.setId(dto.getId());
        baseBulletinDO.setEnableFlag(dto.getEnableFlag());

        saveOrUpdate(baseBulletinDO);

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseBulletinDO> myPage(BaseBulletinPageDTO dto) {

        return lambdaQuery().like(StrUtil.isNotBlank(dto.getTitle()), BaseBulletinDO::getTitle, dto.getTitle())
            .like(StrUtil.isNotBlank(dto.getContent()), BaseBulletinDO::getContent, dto.getContent())
            .eq(dto.getType() != null, BaseBulletinDO::getType, dto.getType())
            .page(dto.updateTimeDescDefaultOrderPage());

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseBulletinDO infoById(NotNullId notNullId) {

        return lambdaQuery().eq(TempEntity::getId, notNullId.getId()).one();

    }

    /**
     * 批量删除
     */
    @Override
    public String deleteByIdSet(NotEmptyIdSet notEmptyIdSet) {

        RedissonUtil.doMultiLock(BaseRedisKeyEnum.PRE_BULLETIN_ID + ":", notEmptyIdSet.getIdSet(), () -> {

            lambdaUpdate().in(TempEntity::getId, notEmptyIdSet.getIdSet())
                .eq(BaseBulletinDO::getStatus, BaseBulletinStatusEnum.DRAFT).remove();

        });

        return TempBizCodeEnum.OK;

    }

    /**
     * 发布
     */
    @Override
    public String publish(NotEmptyIdSet notEmptyIdSet) {

        return TempBizCodeEnum.OK;

    }

    /**
     * 撤回
     */
    @Override
    public String revoke(NotEmptyIdSet notEmptyIdSet) {

        return TempBizCodeEnum.OK;

    }

    /**
     * 分页排序查询：当前用户可以查看的公告
     */
    @Override
    public Page<BaseBulletinDO> userSelfPage(BaseBulletinUserSelfPageDTO dto) {

        return null;

    }

    /**
     * 当前用户可以查看的公告总数
     */
    @Override
    public Long userSelfCount() {

        return 0L;

    }

    /**
     * 当前用户更新公告最近查看时间
     */
    @Override
    public String userSelfUpdateReadTime() {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        baseBulletinReadTimeRefUserMapper.insertOrUpdateReadTime(currentUserId, new Date());

        return TempBizCodeEnum.OK;

    }
}
