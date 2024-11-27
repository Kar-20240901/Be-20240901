package com.kar20240901.be.base.web.service.base.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.exception.base.BaseBizCodeEnum;
import com.kar20240901.be.base.web.mapper.base.BaseAuthMapper;
import com.kar20240901.be.base.web.mapper.base.BaseRoleRefUserMapper;
import com.kar20240901.be.base.web.model.annotation.base.MyTransactional;
import com.kar20240901.be.base.web.model.domain.base.BaseAuthDO;
import com.kar20240901.be.base.web.model.domain.base.BaseRoleRefAuthDO;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.dto.base.BaseAuthInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.base.BaseAuthPageDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.vo.base.BaseAuthInfoByIdVO;
import com.kar20240901.be.base.web.model.vo.base.DictVO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.base.BaseAuthService;
import com.kar20240901.be.base.web.service.base.BaseRoleRefAuthService;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import com.kar20240901.be.base.web.util.base.MyMapUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BaseAuthServiceImpl extends ServiceImpl<BaseAuthMapper, BaseAuthDO> implements BaseAuthService {

    @Resource
    BaseRoleRefAuthService baseRoleRefAuthService;

    @Resource
    BaseRoleRefUserMapper baseRoleRefUserMapper;

    /**
     * 新增/修改
     */
    @Override
    @MyTransactional
    public String insertOrUpdate(BaseAuthInsertOrUpdateDTO dto) {

        // 权限名，不能重复
        boolean exists =
            lambdaQuery().eq(BaseAuthDO::getName, dto.getName()).ne(dto.getId() != null, TempEntity::getId, dto.getId())
                .exists();

        if (exists) {
            R.error(BaseBizCodeEnum.THE_SAME_AUTH_NAME_EXIST);
        }

        BaseAuthDO baseAuthDO = new BaseAuthDO();

        baseAuthDO.setName(dto.getName());
        baseAuthDO.setAuth(dto.getAuth());
        baseAuthDO.setEnableFlag(BooleanUtil.isTrue(dto.getEnableFlag()));
        baseAuthDO.setRemark(MyEntityUtil.getNotNullStr(dto.getRemark()));
        baseAuthDO.setId(dto.getId());

        if (dto.getId() != null) {

            deleteByIdSetSub(CollUtil.newHashSet(dto.getId())); // 先删除子表数据

        }

        saveOrUpdate(baseAuthDO);

        insertOrUpdateSub(dto, baseAuthDO); // 新增 子表数据

        BaseRoleServiceImpl.deleteAuthCache(null); // 删除缓存

        return TempBizCodeEnum.OK;

    }

    /**
     * 新增/修改：新增 子表数据
     */
    private void insertOrUpdateSub(BaseAuthInsertOrUpdateDTO dto, BaseAuthDO baseAuthDO) {

        Long authId = baseAuthDO.getId();

        if (CollUtil.isNotEmpty(dto.getRoleIdSet())) {

            List<BaseRoleRefAuthDO> insertList =
                new ArrayList<>(MyMapUtil.getInitialCapacity(dto.getRoleIdSet().size()));

            for (Long roleId : dto.getRoleIdSet()) {

                BaseRoleRefAuthDO baseRoleRefAuthDO = new BaseRoleRefAuthDO();

                baseRoleRefAuthDO.setRoleId(roleId);
                baseRoleRefAuthDO.setAuthId(authId);

                insertList.add(baseRoleRefAuthDO);

            }

            baseRoleRefAuthService.saveBatch(insertList);

        }

    }

    /**
     * 删除子表数据
     */
    private void deleteByIdSetSub(Set<Long> idSet) {

        // 删除：角色权限关联表
        baseRoleRefAuthService.lambdaUpdate().in(BaseRoleRefAuthDO::getAuthId, idSet).remove();

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseAuthDO> myPage(BaseAuthPageDTO dto) {

        return lambdaQuery().like(StrUtil.isNotBlank(dto.getName()), BaseAuthDO::getName, dto.getName())
            .like(StrUtil.isNotBlank(dto.getAuth()), BaseAuthDO::getAuth, dto.getAuth())
            .like(StrUtil.isNotBlank(dto.getRemark()), TempEntity::getRemark, dto.getRemark())
            .eq(dto.getEnableFlag() != null, TempEntity::getEnableFlag, dto.getEnableFlag())
            .page(dto.updateTimeDescDefaultOrderPage());

    }

    /**
     * 下拉列表
     */
    @Override
    public Page<DictVO> dictList() {

        List<DictVO> dictVOList =
            lambdaQuery().eq(TempEntity::getEnableFlag, true).select(TempEntity::getId, BaseAuthDO::getName).list()
                .stream().map(it -> new DictVO(it.getId(), it.getName())).collect(Collectors.toList());

        return new Page<DictVO>().setTotal(dictVOList.size()).setRecords(dictVOList);

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseAuthInfoByIdVO infoById(NotNullId notNullId) {

        BaseAuthDO baseAuthDO = lambdaQuery().eq(TempEntity::getId, notNullId.getId()).one();

        if (baseAuthDO == null) {
            return null;
        }

        BaseAuthInfoByIdVO baseAuthInfoByIdVO = BeanUtil.copyProperties(baseAuthDO, BaseAuthInfoByIdVO.class);

        // 完善子表的数据
        List<BaseRoleRefAuthDO> authList =
            baseRoleRefAuthService.lambdaQuery().eq(BaseRoleRefAuthDO::getAuthId, baseAuthInfoByIdVO.getId())
                .select(BaseRoleRefAuthDO::getRoleId).list();

        baseAuthInfoByIdVO.setRoleIdSet(
            authList.stream().map(BaseRoleRefAuthDO::getRoleId).collect(Collectors.toSet()));

        return baseAuthInfoByIdVO;

    }

    /**
     * 批量删除
     */
    @Override
    @MyTransactional
    public String deleteByIdSet(NotEmptyIdSet notEmptyIdSet) {

        if (CollUtil.isEmpty(notEmptyIdSet.getIdSet())) {
            return TempBizCodeEnum.OK;
        }

        deleteByIdSetSub(notEmptyIdSet.getIdSet()); // 删除子表数据

        removeByIds(notEmptyIdSet.getIdSet());

        BaseRoleServiceImpl.deleteAuthCache(null); // 删除缓存

        return TempBizCodeEnum.OK;

    }

}
