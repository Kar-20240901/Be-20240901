package com.kar20240901.be.base.web.service.base.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.exception.base.BaseBizCodeEnum;
import com.kar20240901.be.base.web.mapper.base.BaseDictMapper;
import com.kar20240901.be.base.web.model.domain.base.BaseDictDO;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoId;
import com.kar20240901.be.base.web.model.dto.base.BaseDictInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.base.BaseDictListByDictKeyDTO;
import com.kar20240901.be.base.web.model.dto.base.BaseDictPageDTO;
import com.kar20240901.be.base.web.model.dto.base.ChangeNumberDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.enums.base.BaseDictTypeEnum;
import com.kar20240901.be.base.web.model.vo.base.DictIntegerVO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.base.BaseDictService;
import com.kar20240901.be.base.web.util.base.MyDictUtil;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class BaseDictServiceImpl extends ServiceImpl<BaseDictMapper, BaseDictDO> implements BaseDictService {

    /**
     * 新增/修改 备注：这里修改了，租户管理那边也要一起修改
     */
    @Override
    @DSTransactional
    public String insertOrUpdate(BaseDictInsertOrUpdateDTO dto) {

        // uuid不能重复
        if (StrUtil.isNotBlank(dto.getUuid())) {

            boolean exists = lambdaQuery().eq(BaseDictDO::getUuid, dto.getUuid())
                .ne(dto.getId() != null, TempEntity::getId, dto.getId()).exists();

            if (exists) {
                R.error(BaseBizCodeEnum.UUID_IS_EXIST);
            }

        }

        if (BaseDictTypeEnum.DICT.equals(dto.getType())) {

            // 字典 key和 name不能重复
            boolean exists = lambdaQuery().eq(BaseDictDO::getType, BaseDictTypeEnum.DICT)
                .and(i -> i.eq(BaseDictDO::getDictKey, dto.getDictKey()).or().eq(BaseDictDO::getName, dto.getName()))
                .eq(TempEntityNoId::getEnableFlag, true).ne(dto.getId() != null, TempEntity::getId, dto.getId())
                .exists();

            if (exists) {
                R.error(BaseBizCodeEnum.SAME_KEY_OR_NAME_EXIST);
            }

            dto.setValue(-1); // 字典的value为 -1

        } else {

            if (dto.getValue() == null) {
                R.error(BaseBizCodeEnum.VALUE_CANNOT_BE_EMPTY);
            }

            // 字典项 value和 name不能重复
            boolean exists = lambdaQuery().eq(BaseDictDO::getType, BaseDictTypeEnum.DICT_ITEM)
                .eq(TempEntityNoId::getEnableFlag, true).eq(BaseDictDO::getDictKey, dto.getDictKey())
                .and(i -> i.eq(BaseDictDO::getValue, dto.getValue()).or().eq(BaseDictDO::getName, dto.getName()))
                .ne(dto.getId() != null, TempEntity::getId, dto.getId()).exists();

            if (exists) {
                R.error(BaseBizCodeEnum.SAME_VALUE_OR_NAME_EXIST);
            }

        }

        if (dto.getId() != null && BaseDictTypeEnum.DICT.equals(dto.getType())) {

            // 如果是修改，并且是字典，那么也需要修改 该字典的字典项的 dictKey
            BaseDictDO baseDictDO =
                lambdaQuery().eq(TempEntity::getId, dto.getId()).select(BaseDictDO::getDictKey).one();

            if (baseDictDO == null) {
                R.errorMsg("操作失败：字典不存在，请刷新重试");
            }

            if (!baseDictDO.getDictKey().equals(dto.getDictKey())) {

                lambdaUpdate().eq(BaseDictDO::getDictKey, baseDictDO.getDictKey())
                    .eq(BaseDictDO::getType, BaseDictTypeEnum.DICT_ITEM).set(BaseDictDO::getDictKey, dto.getDictKey())
                    .update();

            }

        }

        BaseDictDO baseDictDO = new BaseDictDO();

        baseDictDO.setDictKey(dto.getDictKey());
        baseDictDO.setName(dto.getName());
        baseDictDO.setType(dto.getType());
        baseDictDO.setValue(dto.getValue());
        baseDictDO.setOrderNo(MyEntityUtil.getNotNullOrderNo(dto.getOrderNo()));
        baseDictDO.setEnableFlag(BooleanUtil.isTrue(dto.getEnableFlag()));
        baseDictDO.setRemark(MyEntityUtil.getNotNullStr(dto.getRemark()));
        baseDictDO.setId(dto.getId());
        baseDictDO.setUuid(MyEntityUtil.getNotNullStr(dto.getUuid(), IdUtil.simpleUUID()));

        saveOrUpdate(baseDictDO);

        return TempBizCodeEnum.OK;

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseDictDO> myPage(BaseDictPageDTO dto) {

        return lambdaQuery().like(StrUtil.isNotBlank(dto.getName()), BaseDictDO::getName, dto.getName())
            .like(StrUtil.isNotBlank(dto.getRemark()), TempEntityNoId::getRemark, dto.getRemark())
            .like(StrUtil.isNotBlank(dto.getDictKey()), BaseDictDO::getDictKey, dto.getDictKey())
            .eq(dto.getType() != null, BaseDictDO::getType, dto.getType())
            .eq(dto.getValue() != null, BaseDictDO::getValue, dto.getValue())
            .eq(dto.getEnableFlag() != null, TempEntityNoId::getEnableFlag, dto.getEnableFlag())
            .orderByDesc(BaseDictDO::getOrderNo).orderByAsc(TempEntity::getId).page(dto.pageOrder());

    }

    /**
     * 通过：dictKey获取字典项集合，备注：会进行缓存
     */
    @Override
    public List<DictIntegerVO> listByDictKey(BaseDictListByDictKeyDTO dto) {

        return MyDictUtil.listByDictKey(dto.getDictKey());

    }

    /**
     * 查询：树结构
     */
    @Override
    public List<BaseDictDO> tree(BaseDictPageDTO dto) {

        dto.setPageSize(-1); // 不分页
        List<BaseDictDO> records = myPage(dto).getRecords();

        if (records.size() == 0) {
            return new ArrayList<>();
        }

        // 过滤出：为字典项的数据，目的：查询其所属字典，封装成树结构
        List<BaseDictDO> dictItemList =
            records.stream().filter(it -> BaseDictTypeEnum.DICT_ITEM.equals(it.getType())).collect(Collectors.toList());

        if (dictItemList.size() == 0) {

            // 如果没有字典项类型数据，则直接返回
            return records;

        }

        // 查询出：字典项所属，字典的信息
        List<BaseDictDO> allDictList =
            records.stream().filter(item -> BaseDictTypeEnum.DICT.equals(item.getType())).collect(Collectors.toList());

        Set<Long> dictIdSet = allDictList.stream().map(TempEntity::getId).collect(Collectors.toSet());

        Set<String> dictKeySet = dictItemList.stream().map(BaseDictDO::getDictKey).collect(Collectors.toSet());

        // 查询数据库：字典信息
        List<BaseDictDO> baseDictDoList = lambdaQuery().notIn(dictIdSet.size() != 0, TempEntity::getId, dictIdSet)
            .in(dictKeySet.size() != 0, BaseDictDO::getDictKey, dictKeySet)
            .eq(BaseDictDO::getType, BaseDictTypeEnum.DICT).list();

        // 拼接本次返回值所需的，所有字典
        allDictList.addAll(baseDictDoList);

        // 排序
        allDictList =
            allDictList.stream().sorted(Comparator.comparing(BaseDictDO::getOrderNo, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        Map<String, BaseDictDO> dictMap =
            allDictList.stream().collect(Collectors.toMap(BaseDictDO::getDictKey, it -> it));

        // 封装 children
        for (BaseDictDO item : dictItemList) {

            BaseDictDO baseDictDO = dictMap.get(item.getDictKey());

            List<BaseDictDO> children = baseDictDO.getChildren();

            if (children == null) {

                children = new ArrayList<>();
                baseDictDO.setChildren(children);

            }

            children.add(item); // 添加：字典项

        }

        return allDictList;

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseDictDO infoById(NotNullId notNullId) {

        return lambdaQuery().eq(TempEntity::getId, notNullId.getId()).one();

    }

    /**
     * 批量删除
     */
    @Override
    @DSTransactional
    public String deleteByIdSet(NotEmptyIdSet notEmptyIdSet, boolean checkDeleteFlag) {

        Set<Long> idSet = notEmptyIdSet.getIdSet();

        if (CollUtil.isEmpty(idSet)) {
            return TempBizCodeEnum.OK;
        }

        List<BaseDictDO> baseDictDoList =
            lambdaQuery().in(TempEntity::getId, idSet).eq(BaseDictDO::getType, BaseDictTypeEnum.DICT)
                .select(BaseDictDO::getDictKey).list();

        removeByIds(idSet); // 根据 idSet删除

        if (CollUtil.isEmpty(baseDictDoList)) {
            return TempBizCodeEnum.OK;
        }

        // 如果删除是字典项的父级，则把其下的字典项也跟着删除了
        Set<String> dictKeySet = baseDictDoList.stream().map(BaseDictDO::getDictKey).collect(Collectors.toSet());

        lambdaUpdate().in(BaseDictDO::getDictKey, dictKeySet).remove();

        return TempBizCodeEnum.OK;

    }

    /**
     * 通过主键 idSet，加减排序号
     */
    @Override
    @DSTransactional
    public String addOrderNo(ChangeNumberDTO dto) {

        if (dto.getNumber() == 0) {
            return TempBizCodeEnum.OK;
        }

        List<BaseDictDO> baseDictDoList =
            lambdaQuery().in(TempEntity::getId, dto.getIdSet()).select(TempEntity::getId, BaseDictDO::getOrderNo)
                .list();

        for (BaseDictDO item : baseDictDoList) {
            item.setOrderNo((int)(item.getOrderNo() + dto.getNumber()));
        }

        updateBatchById(baseDictDoList);

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

        lambdaUpdate().in(TempEntity::getId, dto.getIdSet()).set(BaseDictDO::getOrderNo, dto.getNumber()).update();

        return TempBizCodeEnum.OK;

    }

}
