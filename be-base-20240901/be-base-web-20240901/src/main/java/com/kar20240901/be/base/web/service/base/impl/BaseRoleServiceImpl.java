package com.kar20240901.be.base.web.service.base.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.exception.base.BaseBizCodeEnum;
import com.kar20240901.be.base.web.mapper.base.BaseRoleMapper;
import com.kar20240901.be.base.web.model.annotation.base.MyTransactional;
import com.kar20240901.be.base.web.model.domain.base.BaseRoleDO;
import com.kar20240901.be.base.web.model.domain.base.BaseRoleRefAuthDO;
import com.kar20240901.be.base.web.model.domain.base.BaseRoleRefMenuDO;
import com.kar20240901.be.base.web.model.domain.base.BaseRoleRefUserDO;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoId;
import com.kar20240901.be.base.web.model.dto.base.BaseRoleInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.base.BaseRolePageDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.enums.base.TempRedisKeyEnum;
import com.kar20240901.be.base.web.model.vo.base.BaseRoleInfoByIdVO;
import com.kar20240901.be.base.web.model.vo.base.DictVO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.base.BaseRoleRefAuthService;
import com.kar20240901.be.base.web.service.base.BaseRoleRefMenuService;
import com.kar20240901.be.base.web.service.base.BaseRoleRefUserService;
import com.kar20240901.be.base.web.service.base.BaseRoleService;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import com.kar20240901.be.base.web.util.base.MyMapUtil;
import com.kar20240901.be.base.web.util.kafka.TempKafkaUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
public class BaseRoleServiceImpl extends ServiceImpl<BaseRoleMapper, BaseRoleDO> implements BaseRoleService {

    private static BaseRoleRefUserService baseRoleRefUserService;

    @Resource
    public void setBaseRoleRefUserService(BaseRoleRefUserService baseRoleRefUserService) {
        BaseRoleServiceImpl.baseRoleRefUserService = baseRoleRefUserService;
    }

    @Resource
    BaseRoleRefMenuService baseRoleRefMenuService;

    private static BaseRoleRefAuthService baseRoleRefAuthService;

    @Resource
    public void setBaseRoleRefAuthService(BaseRoleRefAuthService baseRoleRefAuthService) {
        BaseRoleServiceImpl.baseRoleRefAuthService = baseRoleRefAuthService;
    }

    private static RedissonClient redissonClient;

    @Resource
    public void setRedissonClient(RedissonClient redissonClient) {
        BaseRoleServiceImpl.redissonClient = redissonClient;
    }

    /**
     * 新增/修改
     */
    @Override
    @MyTransactional
    public String insertOrUpdate(BaseRoleInsertOrUpdateDTO dto) {

        // 角色名，不能重复
        boolean exists =
            lambdaQuery().eq(BaseRoleDO::getName, dto.getName()).ne(dto.getId() != null, TempEntity::getId, dto.getId())
                .exists();

        if (exists) {
            R.error(BaseBizCodeEnum.THE_SAME_ROLE_NAME_EXIST);
        }

        // uuid不能重复
        if (StrUtil.isNotBlank(dto.getUuid())) {

            exists = lambdaQuery().eq(BaseRoleDO::getUuid, dto.getUuid())
                .ne(dto.getId() != null, TempEntity::getId, dto.getId()).exists();

            if (exists) {
                R.error(BaseBizCodeEnum.UUID_IS_EXIST);
            }

        }

        // 如果是默认角色，则取消之前的默认角色
        if (BooleanUtil.isTrue(dto.getDefaultFlag())) {

            lambdaUpdate().set(BaseRoleDO::getDefaultFlag, false).eq(BaseRoleDO::getDefaultFlag, true)
                .ne(dto.getId() != null, TempEntity::getId, dto.getId()).update();

        }

        BaseRoleDO baseRoleDO = new BaseRoleDO();

        baseRoleDO.setName(dto.getName());
        baseRoleDO.setUuid(MyEntityUtil.getNotNullStr(dto.getUuid(), IdUtil.simpleUUID()));

        baseRoleDO.setDefaultFlag(BooleanUtil.isTrue(dto.getDefaultFlag()));
        baseRoleDO.setEnableFlag(BooleanUtil.isTrue(dto.getEnableFlag()));
        baseRoleDO.setRemark(MyEntityUtil.getNotNullStr(dto.getRemark()));
        baseRoleDO.setId(dto.getId());

        if (dto.getId() != null) {

            deleteByIdSetSub(CollUtil.newHashSet(dto.getId())); // 先删除子表数据

        }

        saveOrUpdate(baseRoleDO);

        insertOrUpdateSub(dto, baseRoleDO); // 新增 子表数据

        deleteAuthCache(null); // 删除权限缓存

        deleteMenuCache(null); // 删除菜单缓存

        return TempBizCodeEnum.OK;

    }

    /**
     * 删除权限缓存
     */
    public static void deleteAuthCache(@Nullable Set<Long> userIdSet) {

        if (userIdSet == null) {

            TempKafkaUtil.sendDeleteCacheTopic(TempRedisKeyEnum.PRE_USER_AUTH.name() + ":*");

        } else {

            if (CollUtil.isEmpty(userIdSet)) {
                return;
            }

            RKeys keys = redissonClient.getKeys();

            String[] redisKeyArr =
                userIdSet.stream().map(it -> TempRedisKeyEnum.PRE_USER_AUTH.name() + ":" + it).toArray(String[]::new);

            keys.delete(redisKeyArr);

        }

    }

    /**
     * 删除菜单缓存
     */
    public static void deleteMenuCache(@Nullable Set<Long> userIdSet) {

        if (userIdSet == null) {

            TempKafkaUtil.sendDeleteCacheTopic(TempRedisKeyEnum.PRE_USER_MENU.name() + ":*");

        } else {

            if (CollUtil.isEmpty(userIdSet)) {
                return;
            }

            RKeys keys = redissonClient.getKeys();

            String[] redisKeyArr =
                userIdSet.stream().map(it -> TempRedisKeyEnum.PRE_USER_MENU.name() + ":" + it).toArray(String[]::new);

            keys.delete(redisKeyArr);

        }

    }

    /**
     * 新增/修改：新增 子表数据
     */
    private void insertOrUpdateSub(BaseRoleInsertOrUpdateDTO dto, BaseRoleDO baseRoleDO) {

        // 如果禁用了，则子表不进行新增操作
        if (BooleanUtil.isFalse(baseRoleDO.getEnableFlag())) {
            return;
        }

        Long roleId = baseRoleDO.getId();

        if (CollUtil.isNotEmpty(dto.getMenuIdSet())) {

            List<BaseRoleRefMenuDO> insertList =
                new ArrayList<>(MyMapUtil.getInitialCapacity(dto.getMenuIdSet().size()));

            for (Long menuId : dto.getMenuIdSet()) {

                BaseRoleRefMenuDO baseRoleRefMenuDO = new BaseRoleRefMenuDO();

                baseRoleRefMenuDO.setRoleId(roleId);
                baseRoleRefMenuDO.setMenuId(menuId);

                insertList.add(baseRoleRefMenuDO);

            }

            baseRoleRefMenuService.saveBatch(insertList);

        }

        if (CollUtil.isNotEmpty(dto.getUserIdSet())) {

            List<BaseRoleRefUserDO> insertList =
                new ArrayList<>(MyMapUtil.getInitialCapacity(dto.getUserIdSet().size()));

            for (Long userId : dto.getUserIdSet()) {

                BaseRoleRefUserDO baseRoleRefUserDO = new BaseRoleRefUserDO();

                baseRoleRefUserDO.setRoleId(roleId);
                baseRoleRefUserDO.setUserId(userId);

                insertList.add(baseRoleRefUserDO);

            }

            baseRoleRefUserService.saveBatch(insertList);

        }

        if (CollUtil.isNotEmpty(dto.getAuthIdSet())) {

            List<BaseRoleRefAuthDO> insertList =
                new ArrayList<>(MyMapUtil.getInitialCapacity(dto.getAuthIdSet().size()));

            for (Long authId : dto.getAuthIdSet()) {

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

        // 删除：角色菜单关联表
        baseRoleRefMenuService.removeByIds(idSet);

        // 删除：角色用户关联表
        baseRoleRefUserService.removeByIds(idSet);

        // 删除：角色权限关联表
        baseRoleRefAuthService.removeByIds(idSet);

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseRoleDO> myPage(BaseRolePageDTO dto) {

        return lambdaQuery().like(StrUtil.isNotBlank(dto.getName()), BaseRoleDO::getName, dto.getName())
            .like(StrUtil.isNotBlank(dto.getRemark()), TempEntity::getRemark, dto.getRemark())
            .eq(dto.getEnableFlag() != null, TempEntity::getEnableFlag, dto.getEnableFlag())
            .eq(dto.getDefaultFlag() != null, BaseRoleDO::getDefaultFlag, dto.getDefaultFlag())
            .page(dto.updateTimeDescDefaultOrderPage());

    }

    /**
     * 下拉列表
     */
    @Override
    public Page<DictVO> dictList() {

        List<DictVO> dictVOList =
            lambdaQuery().eq(TempEntityNoId::getEnableFlag, true).select(TempEntity::getId, BaseRoleDO::getName).list()
                .stream().map(it -> new DictVO(it.getId(), it.getName())).collect(Collectors.toList());

        return new Page<DictVO>().setTotal(dictVOList.size()).setRecords(dictVOList);

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseRoleInfoByIdVO infoById(NotNullId notNullId) {

        BaseRoleDO baseRoleDO = lambdaQuery().eq(TempEntity::getId, notNullId.getId()).one();

        if (baseRoleDO == null) {
            return null;
        }

        BaseRoleInfoByIdVO baseRoleInfoByIdVO = BeanUtil.copyProperties(baseRoleDO, BaseRoleInfoByIdVO.class);

        // 完善子表的数据
        List<BaseRoleRefMenuDO> menuList =
            baseRoleRefMenuService.lambdaQuery().eq(BaseRoleRefMenuDO::getRoleId, baseRoleInfoByIdVO.getId())
                .select(BaseRoleRefMenuDO::getMenuId).list();

        List<BaseRoleRefUserDO> userList =
            baseRoleRefUserService.lambdaQuery().eq(BaseRoleRefUserDO::getRoleId, baseRoleInfoByIdVO.getId())
                .select(BaseRoleRefUserDO::getUserId).list();

        List<BaseRoleRefAuthDO> authList =
            baseRoleRefAuthService.lambdaQuery().eq(BaseRoleRefAuthDO::getRoleId, baseRoleInfoByIdVO.getId())
                .select(BaseRoleRefAuthDO::getAuthId).list();

        baseRoleInfoByIdVO.setMenuIdSet(
            menuList.stream().map(BaseRoleRefMenuDO::getMenuId).collect(Collectors.toSet()));
        baseRoleInfoByIdVO.setUserIdSet(
            userList.stream().map(BaseRoleRefUserDO::getUserId).collect(Collectors.toSet()));
        baseRoleInfoByIdVO.setAuthIdSet(
            authList.stream().map(BaseRoleRefAuthDO::getAuthId).collect(Collectors.toSet()));

        return baseRoleInfoByIdVO;

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

        Set<Long> userIdSet =
            baseRoleRefUserService.lambdaQuery().in(BaseRoleRefUserDO::getRoleId, notEmptyIdSet.getIdSet())
                .select(BaseRoleRefUserDO::getUserId).list().stream().map(BaseRoleRefUserDO::getUserId)
                .collect(Collectors.toSet());

        deleteByIdSetSub(notEmptyIdSet.getIdSet()); // 删除子表数据

        removeByIds(notEmptyIdSet.getIdSet());

        deleteAuthCache(null); // 删除权限缓存

        deleteMenuCache(null); // 删除菜单缓存

        return TempBizCodeEnum.OK;

    }

}
