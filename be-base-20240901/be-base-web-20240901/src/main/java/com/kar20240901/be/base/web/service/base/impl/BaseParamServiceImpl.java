package com.kar20240901.be.base.web.service.base.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.exception.base.BaseBizCodeEnum;
import com.kar20240901.be.base.web.mapper.base.BaseParamMapper;
import com.kar20240901.be.base.web.model.domain.base.BaseParamDO;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoId;
import com.kar20240901.be.base.web.model.dto.base.BaseParamInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.base.BaseParamPageDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.enums.base.TempRedisKeyEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.base.BaseParamService;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import com.kar20240901.be.base.web.util.base.MyParamUtil;
import com.kar20240901.be.base.web.util.kafka.TempKafkaUtil;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
public class BaseParamServiceImpl extends ServiceImpl<BaseParamMapper, BaseParamDO> implements BaseParamService {

    private static RedissonClient redissonClient;

    @Resource
    public void setRedissonClient(RedissonClient redissonClient) {
        BaseParamServiceImpl.redissonClient = redissonClient;
    }

    /**
     * 新增/修改
     */
    @Override
    @DSTransactional
    public String insertOrUpdate(BaseParamInsertOrUpdateDTO dto) {

        // uuid不能重复
        if (StrUtil.isNotBlank(dto.getUuid())) {

            boolean exists = lambdaQuery().eq(BaseParamDO::getUuid, dto.getUuid())
                .ne(dto.getId() != null, TempEntity::getId, dto.getId()).exists();

            if (exists) {
                R.error(BaseBizCodeEnum.UUID_IS_EXIST);
            }

        }

        BaseParamDO baseParamDO = new BaseParamDO();

        baseParamDO.setName(dto.getName());
        baseParamDO.setValue(dto.getValue());
        baseParamDO.setEnableFlag(BooleanUtil.isTrue(dto.getEnableFlag()));
        baseParamDO.setId(dto.getId());
        baseParamDO.setUuid(MyEntityUtil.getNotNullStr(dto.getUuid(), IdUtil.simpleUUID()));
        baseParamDO.setRemark(MyEntityUtil.getNotNullStr(dto.getRemark()));

        saveOrUpdate(baseParamDO);

        deleteParamCache(CollUtil.newHashSet(baseParamDO.getUuid())); // 删除参数缓存

        return TempBizCodeEnum.OK;

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseParamDO> myPage(BaseParamPageDTO dto) {

        return lambdaQuery().like(StrUtil.isNotBlank(dto.getName()), BaseParamDO::getName, dto.getName())
            .like(StrUtil.isNotBlank(dto.getRemark()), TempEntityNoId::getRemark, dto.getRemark())
            .eq(dto.getEnableFlag() != null, TempEntityNoId::getEnableFlag, dto.getEnableFlag())
            .page(dto.updateTimeDescDefaultOrderPage());

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseParamDO infoById(NotNullId notNullId) {

        return lambdaQuery().eq(TempEntity::getId, notNullId.getId()).one();

    }

    /**
     * 批量删除
     */
    @Override
    @DSTransactional
    public String deleteByIdSet(NotEmptyIdSet notEmptyIdSet) {

        Set<Long> idSet = notEmptyIdSet.getIdSet();

        if (CollUtil.isEmpty(idSet)) {
            return TempBizCodeEnum.OK;
        }

        for (String item : MyParamUtil.SYSTEM_PARAM_NOT_DELETE_ID_SET) {

            if (idSet.contains(Convert.toLong(item))) {

                R.errorMsg("操作失败：id【{}】不允许删除", item);

            }

        }

        List<BaseParamDO> baseParamDoList =
            lambdaQuery().in(TempEntity::getId, idSet).select(BaseParamDO::getUuid).list();

        Set<String> uuidSet = baseParamDoList.stream().map(BaseParamDO::getUuid).collect(Collectors.toSet());

        removeByIds(idSet); // 根据 idSet删除

        deleteParamCache(uuidSet); // 删除参数缓存

        return TempBizCodeEnum.OK;

    }

    /**
     * 删除参数缓存
     */
    public static void deleteParamCache(@Nullable Set<String> uuidSet) {

        if (uuidSet == null) {

            TempKafkaUtil.sendDeleteCacheByPatternTopic(
                CollUtil.newArrayList(TempRedisKeyEnum.PRE_PARAM_UUID.name() + ":*"));

        } else {

            if (CollUtil.isEmpty(uuidSet)) {
                return;
            }

            List<String> redisKeyList = uuidSet.stream().map(it -> TempRedisKeyEnum.PRE_PARAM_UUID.name() + ":" + it)
                .collect(Collectors.toList());

            TempKafkaUtil.sendDeleteCacheTopic(redisKeyList);

        }

    }

}
