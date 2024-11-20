package com.kar20240901.be.base.web.service.base.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.exception.base.BaseBizCodeEnum;
import com.kar20240901.be.base.web.mapper.base.BaseMenuMapper;
import com.kar20240901.be.base.web.model.annotation.base.MyTransactional;
import com.kar20240901.be.base.web.model.domain.base.BaseMenuDO;
import com.kar20240901.be.base.web.model.domain.base.BaseRoleRefMenuDO;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoId;
import com.kar20240901.be.base.web.model.domain.base.TempEntityTree;
import com.kar20240901.be.base.web.model.dto.base.BaseMenuInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.base.BaseMenuPageDTO;
import com.kar20240901.be.base.web.model.dto.base.ChangeNumberDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.enums.base.TempRedisKeyEnum;
import com.kar20240901.be.base.web.model.vo.base.BaseMenuInfoByIdVO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.base.BaseMenuService;
import com.kar20240901.be.base.web.service.base.BaseRoleRefMenuService;
import com.kar20240901.be.base.web.util.base.CallBack;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import com.kar20240901.be.base.web.util.base.MyThreadUtil;
import com.kar20240901.be.base.web.util.base.MyTreeUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
public class BaseMenuServiceImpl extends ServiceImpl<BaseMenuMapper, BaseMenuDO> implements BaseMenuService {

    @Resource
    BaseRoleRefMenuService baseRoleRefMenuService;

    @Resource
    RedissonClient redissonClient;

    /**
     * 新增/修改
     */
    @Override
    @MyTransactional
    public String insertOrUpdate(BaseMenuInsertOrUpdateDTO dto) {

        if (dto.getId() != null && dto.getId().equals(dto.getPid())) {
            R.error(TempBizCodeEnum.PID_CANNOT_BE_EQUAL_TO_ID);
        }

        // path不能重复
        if (StrUtil.isNotBlank(dto.getPath())) {

            boolean exists = lambdaQuery().eq(BaseMenuDO::getPath, dto.getPath())
                .ne(dto.getId() != null, TempEntity::getId, dto.getId()).exists();

            if (exists) {
                R.error(BaseBizCodeEnum.MENU_PATH_IS_EXIST);
            }

        }

        // uuid不能重复
        if (StrUtil.isNotBlank(dto.getUuid())) {

            boolean exists = lambdaQuery().eq(BaseMenuDO::getUuid, dto.getUuid())
                .ne(dto.getId() != null, TempEntity::getId, dto.getId()).exists();

            if (exists) {
                R.error(BaseBizCodeEnum.UUID_IS_EXIST);
            }

        }

        if (dto.getId() != null) {

            deleteByIdSetSub(CollUtil.newHashSet(dto.getId())); // 先删除：子表数据

        }

        BaseMenuDO baseMenuDO = getDoByDto(dto);

        saveOrUpdate(baseMenuDO);

        insertOrUpdateSub(baseMenuDO, dto); // 新增 子表数据

        BaseRoleServiceImpl.deleteMenuCache(null); // 删除缓存

        return TempBizCodeEnum.OK;

    }

    /**
     * 通过 dto，获取 do
     */
    private BaseMenuDO getDoByDto(BaseMenuInsertOrUpdateDTO dto) {

        BaseMenuDO baseMenuDO = new BaseMenuDO();

        baseMenuDO.setName(dto.getName());
        baseMenuDO.setPath(MyEntityUtil.getNotNullStr(dto.getPath()));
        baseMenuDO.setIcon(MyEntityUtil.getNotNullStr(dto.getIcon()));
        baseMenuDO.setPid(MyEntityUtil.getNotNullParentId(dto.getPid()));
        baseMenuDO.setId(dto.getId());
        baseMenuDO.setEnableFlag(BooleanUtil.isTrue(dto.getEnableFlag()));

        baseMenuDO.setRouter(MyEntityUtil.getNotNullStr(dto.getRouter()));
        baseMenuDO.setRedirect(MyEntityUtil.getNotNullStr(dto.getRedirect()));
        baseMenuDO.setRemark(MyEntityUtil.getNotNullStr(dto.getRemark()));
        baseMenuDO.setOrderNo(MyEntityUtil.getNotNullOrderNo(dto.getOrderNo()));

        baseMenuDO.setShowFlag(BooleanUtil.isTrue(dto.getShowFlag()));
        baseMenuDO.setLinkFlag(BooleanUtil.isTrue(dto.getLinkFlag()));

        baseMenuDO.setUuid(MyEntityUtil.getNotNullStr(dto.getUuid(), IdUtil.simpleUUID()));

        return baseMenuDO;

    }

    /**
     * 新增/修改：新增 子表数据
     */
    private void insertOrUpdateSub(BaseMenuDO baseMenuDO, BaseMenuInsertOrUpdateDTO dto) {

        // 如果禁用了，则子表不进行新增操作
        if (BooleanUtil.isFalse(baseMenuDO.getEnableFlag())) {
            return;
        }

        // 新增：角色菜单，菜单用户 关联表数据
        if (CollUtil.isNotEmpty(dto.getRoleIdSet())) {

            List<BaseRoleRefMenuDO> insertList =
                dto.getRoleIdSet().stream().map(it -> new BaseRoleRefMenuDO(it, baseMenuDO.getId()))
                    .collect(Collectors.toList());

            baseRoleRefMenuService.saveBatch(insertList);

        }

    }

    /**
     * 删除子表数据
     */
    private void deleteByIdSetSub(Set<Long> idSet) {

        // 删除：角色菜单关联表
        baseRoleRefMenuService.lambdaUpdate().in(BaseRoleRefMenuDO::getMenuId, idSet).remove();

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseMenuDO> myPage(BaseMenuPageDTO dto) {

        return lambdaQuery().like(StrUtil.isNotBlank(dto.getName()), BaseMenuDO::getName, dto.getName())
            .like(StrUtil.isNotBlank(dto.getPath()), BaseMenuDO::getPath, dto.getPath())
            .like(StrUtil.isNotBlank(dto.getRedirect()), BaseMenuDO::getRedirect, dto.getRedirect())
            .eq(StrUtil.isNotBlank(dto.getRouter()), BaseMenuDO::getRouter, dto.getRouter())
            .eq(dto.getPid() != null, BaseMenuDO::getPid, dto.getPid())
            .eq(dto.getEnableFlag() != null, TempEntity::getEnableFlag, dto.getEnableFlag())
            .eq(dto.getLinkFlag() != null, BaseMenuDO::getLinkFlag, dto.getLinkFlag())
            .eq(dto.getShowFlag() != null, BaseMenuDO::getShowFlag, dto.getShowFlag())
            .select(true, getMyPageSelectList()).orderByDesc(TempEntityTree::getOrderNo).orderByAsc(TempEntity::getId)
            .page(dto.pageOrder());

    }

    private static ArrayList<SFunction<BaseMenuDO, ?>> getMyPageSelectList() {

        return CollUtil.newArrayList(TempEntity::getId, TempEntityTree::getPid, BaseMenuDO::getName,
            BaseMenuDO::getPath, BaseMenuDO::getRouter, BaseMenuDO::getShowFlag, TempEntityNoId::getEnableFlag,
            BaseMenuDO::getRedirect, BaseMenuDO::getUuid, TempEntityTree::getOrderNo, BaseMenuDO::getIcon);

    }

    /**
     * 查询：树结构
     */
    @SneakyThrows
    @Override
    public List<BaseMenuDO> tree(BaseMenuPageDTO dto) {

        CountDownLatch countDownLatch = ThreadUtil.newCountDownLatch(1);

        CallBack<List<BaseMenuDO>> allListCallBack = new CallBack<>();

        MyThreadUtil.execute(() -> {

            allListCallBack.setValue(baseMapper.getMenuListByUserId(null));

        }, countDownLatch);

        // 根据条件进行筛选，得到符合条件的数据，然后再逆向生成整棵树，并返回这个树结构
        dto.setPageSize(-1); // 不分页
        List<BaseMenuDO> baseMenuDoList = myPage(dto).getRecords();

        countDownLatch.await();

        if (baseMenuDoList.size() == 0) {
            return new ArrayList<>();
        }

        if (allListCallBack.getValue().size() == 0) {
            return new ArrayList<>();
        }

        return MyTreeUtil.getFullTreeByDeepNode(baseMenuDoList, allListCallBack.getValue());

    }

    /**
     * 下拉树形列表
     */
    @Override
    public List<BaseMenuDO> dictTreeList() {

        List<BaseMenuDO> baseMenuDoList = lambdaQuery().eq(TempEntityNoId::getEnableFlag, true)
            .select(TempEntity::getId, TempEntityTree::getPid, BaseMenuDO::getName)
            .orderByDesc(TempEntityTree::getOrderNo).orderByAsc(TempEntity::getId).list();

        return MyTreeUtil.listToTree(baseMenuDoList);

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseMenuInfoByIdVO infoById(NotNullId notNullId) {

        BaseMenuDO baseMenuDO = lambdaQuery().eq(TempEntity::getId, notNullId.getId()).one();

        if (baseMenuDO == null) {
            return null;
        }

        BaseMenuInfoByIdVO baseMenuInfoByIdVO = BeanUtil.copyProperties(baseMenuDO, BaseMenuInfoByIdVO.class);

        // 设置：角色 idSet
        List<BaseRoleRefMenuDO> baseRoleRefMenuDOList =
            baseRoleRefMenuService.lambdaQuery().eq(BaseRoleRefMenuDO::getMenuId, notNullId.getId())
                .select(BaseRoleRefMenuDO::getRoleId).list();

        baseMenuInfoByIdVO.setRoleIdSet(
            baseRoleRefMenuDOList.stream().map(BaseRoleRefMenuDO::getRoleId).collect(Collectors.toSet()));

        // 处理：父级 id
        MyEntityUtil.handleParentId(baseMenuInfoByIdVO);

        return baseMenuInfoByIdVO;

    }

    /**
     * 批量删除
     */
    @Override
    @MyTransactional
    public String deleteByIdSet(NotEmptyIdSet notEmptyIdSet) {

        Set<Long> idSet = notEmptyIdSet.getIdSet();

        if (CollUtil.isEmpty(idSet)) {
            return TempBizCodeEnum.OK;
        }

        // 如果存在下级，则无法删除
        boolean exists = lambdaQuery().in(BaseMenuDO::getPid, idSet).exists();

        if (exists) {
            R.error(TempBizCodeEnum.PLEASE_DELETE_THE_CHILD_NODE_FIRST);
        }

        // 删除子表数据
        deleteByIdSetSub(idSet);

        removeByIds(idSet);

        BaseRoleServiceImpl.deleteMenuCache(null); // 删除缓存

        return TempBizCodeEnum.OK;

    }

    /**
     * 获取：当前用户绑定的菜单
     */
    @Override
    public List<BaseMenuDO> userSelfMenuList() {

        Long userId = MyUserUtil.getCurrentUserId();

        RList<BaseMenuDO> rList = redissonClient.getList(TempRedisKeyEnum.PRE_USER_MENU.name() + ":" + userId);

        List<BaseMenuDO> baseMenuDoList = rList.readAll();

        if (CollUtil.isEmpty(baseMenuDoList)) {

            Long queryUserId = userId;

            if (MyUserUtil.getCurrentUserAdminFlag(queryUserId)) {

                queryUserId = null;

            }

            baseMenuDoList = baseMapper.getMenuListByUserId(queryUserId);

            if (CollUtil.isEmpty(baseMenuDoList)) {

                rList.add(null); // 备注：redis是支持 list和 set里存放 null元素的

            } else {

                rList.addAll(baseMenuDoList);

            }

        }

        return baseMenuDoList;

    }

    /**
     * 通过主键 idSet，加减排序号
     */
    @Override
    @MyTransactional
    public String addOrderNo(ChangeNumberDTO dto) {

        if (dto.getNumber() == 0) {
            return TempBizCodeEnum.OK;
        }

        List<BaseMenuDO> baseMenuDoList =
            lambdaQuery().in(TempEntity::getId, dto.getIdSet()).select(TempEntity::getId, TempEntityTree::getOrderNo)
                .list();

        for (BaseMenuDO item : baseMenuDoList) {
            item.setOrderNo((int)(item.getOrderNo() + dto.getNumber()));
        }

        updateBatchById(baseMenuDoList);

        return TempBizCodeEnum.OK;

    }

    /**
     * 通过主键 idSet，修改排序号
     */
    @Override
    public String updateOrderNo(ChangeNumberDTO dto) {

        if (dto.getNumber() == 0) {
            return TempBizCodeEnum.OK;
        }

        lambdaUpdate().in(TempEntity::getId, dto.getIdSet()).set(TempEntityTree::getOrderNo, dto.getNumber()).update();

        return TempBizCodeEnum.OK;

    }

}
