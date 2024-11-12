package com.kar20240901.be.base.web.service.base.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.base.BaseApiTokenMapper;
import com.kar20240901.be.base.web.model.domain.base.BaseApiTokenDO;
import com.kar20240901.be.base.web.model.dto.base.BaseApiTokenInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.base.BaseApiTokenPageDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.base.BaseApiTokenService;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class BaseApiTokenServiceImpl extends ServiceImpl<BaseApiTokenMapper, BaseApiTokenDO>
    implements BaseApiTokenService {

    /**
     * 新增/修改
     */
    @Override
    public String insertOrUpdate(BaseApiTokenInsertOrUpdateDTO dto) {

        Long userId = MyUserUtil.getCurrentUserId();

        if (MyUserUtil.getCurrentUserAdminFlag(userId)) {

            if (dto.getUserId() == null) {

                dto.setUserId(userId);

            }

        } else {

            dto.setUserId(userId);

            if (dto.getId() != null) {

                boolean exists =
                    lambdaQuery().eq(BaseApiTokenDO::getId, dto.getId()).eq(BaseApiTokenDO::getUserId, userId).exists();

                if (!exists) {
                    R.error(TempBizCodeEnum.ILLEGAL_REQUEST);
                }

            }

        }

        BaseApiTokenDO baseApiTokenDO = new BaseApiTokenDO();

        baseApiTokenDO.setId(dto.getId());
        baseApiTokenDO.setUserId(userId);

        if (dto.getId() == null) {

            baseApiTokenDO.setToken(IdUtil.simpleUUID());

        }

        baseApiTokenDO.setName(dto.getName());

        saveOrUpdate(baseApiTokenDO);

        return baseApiTokenDO.getToken();

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseApiTokenDO> myPage(BaseApiTokenPageDTO dto) {

        Long userId = MyUserUtil.getCurrentUserId();

        Long queryUserId = null;

        if (MyUserUtil.getCurrentUserAdminFlag(userId)) {

            if (dto.getUserId() != null) {

                queryUserId = dto.getUserId();

            }

        } else {

            queryUserId = userId;

        }

        return lambdaQuery().like(StrUtil.isNotBlank(dto.getName()), BaseApiTokenDO::getName, dto.getName())
            .eq(queryUserId != null, BaseApiTokenDO::getUserId, queryUserId).page(dto.createTimeDescDefaultOrderPage());

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseApiTokenDO infoById(NotNullId notNullId) {

        Long userId = MyUserUtil.getCurrentUserId();

        Long queryUserId = null;

        if (!MyUserUtil.getCurrentUserAdminFlag(userId)) {

            queryUserId = userId;

        }

        return lambdaQuery().eq(BaseApiTokenDO::getId, notNullId.getId()).eq(BaseApiTokenDO::getUserId, queryUserId)
            .one();

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

        Long userId = MyUserUtil.getCurrentUserId();

        Long queryUserId = null;

        if (!MyUserUtil.getCurrentUserAdminFlag(userId)) {

            queryUserId = userId;

        }

        lambdaUpdate().in(BaseApiTokenDO::getId, idSet).eq(queryUserId != null, BaseApiTokenDO::getUserId, queryUserId)
            .remove();

        return TempBizCodeEnum.OK;

    }

}
