package com.kar20240901.be.base.web.service.file.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.file.BaseFileTransferMapper;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoIdSuper;
import com.kar20240901.be.base.web.model.domain.file.BaseFileTransferDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.file.BaseFileTransferPageDTO;
import com.kar20240901.be.base.web.service.file.BaseFileTransferService;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class BaseFileTransferServiceImpl extends ServiceImpl<BaseFileTransferMapper, BaseFileTransferDO>
    implements BaseFileTransferService {

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseFileTransferDO> myPage(BaseFileTransferPageDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        boolean adminFlag = MyUserUtil.getCurrentUserAdminFlag(currentUserId);

        return lambdaQuery() //
            .like(StrUtil.isNotBlank(dto.getShowFileName()), BaseFileTransferDO::getShowFileName,
                dto.getShowFileName()) //
            .eq(dto.getType() != null, BaseFileTransferDO::getType, dto.getType()) //
            .eq(!adminFlag, BaseFileTransferDO::getUserId, currentUserId)
            .select(TempEntity::getId, TempEntityNoIdSuper::getCreateTime, BaseFileTransferDO::getShowFileName,
                BaseFileTransferDO::getFileId, BaseFileTransferDO::getStatus, BaseFileTransferDO::getType)
            .page(dto.updateTimeDescDefaultOrderPage());

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseFileTransferDO infoById(NotNullId notNullId) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        boolean adminFlag = MyUserUtil.getCurrentUserAdminFlag(currentUserId);

        return lambdaQuery().eq(TempEntity::getId, notNullId.getId())
            .eq(!adminFlag, BaseFileTransferDO::getUserId, currentUserId).one();

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

        Long currentUserId = MyUserUtil.getCurrentUserId();

        boolean adminFlag = MyUserUtil.getCurrentUserAdminFlag(currentUserId);

        // 根据 idSet删除
        lambdaUpdate().eq(!adminFlag, BaseFileTransferDO::getUserId, currentUserId).in(BaseFileTransferDO::getId, idSet)
            .remove();

        return TempBizCodeEnum.OK;

    }

}
