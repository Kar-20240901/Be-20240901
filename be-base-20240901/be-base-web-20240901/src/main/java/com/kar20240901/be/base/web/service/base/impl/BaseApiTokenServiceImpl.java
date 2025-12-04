package com.kar20240901.be.base.web.service.base.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
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
import com.kar20240901.be.base.web.util.base.MyThreadUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PreDestroy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BaseApiTokenServiceImpl extends ServiceImpl<BaseApiTokenMapper, BaseApiTokenDO>
    implements BaseApiTokenService {

    private static ConcurrentHashMap<Long, BaseApiTokenDO> API_TOKEN_DO_MAP = new ConcurrentHashMap<>();

    /**
     * 添加
     */
    public static void add(Set<Long> apiTokenIdSet) {

        if (CollUtil.isEmpty(apiTokenIdSet)) {
            return;
        }

        Date date = new Date();

        for (Long item : apiTokenIdSet) {

            BaseApiTokenDO baseApiTokenDO = new BaseApiTokenDO();

            baseApiTokenDO.setId(item);
            baseApiTokenDO.setLastUseTime(date);

            API_TOKEN_DO_MAP.put(item, baseApiTokenDO);

        }

    }

    /**
     * 定时任务，保存数据
     */
    @PreDestroy
    @Scheduled(fixedDelay = 5000)
    public void scheduledSava() {

        ConcurrentHashMap<Long, BaseApiTokenDO> tempApiTokenDoMap;

        synchronized (API_TOKEN_DO_MAP) {

            if (CollUtil.isEmpty(API_TOKEN_DO_MAP)) {
                return;
            }

            tempApiTokenDoMap = API_TOKEN_DO_MAP;
            API_TOKEN_DO_MAP = new ConcurrentHashMap<>();

        }

        // 目的：防止还有程序往：tempMap，里面添加数据，所以这里等待一会
        MyThreadUtil.schedule(() -> {

            updateBatchById(tempApiTokenDoMap.values());

        }, DateUtil.offsetMillisecond(new Date(), 1500));

    }

    /**
     * 新增/修改
     */
    @Override
    public String insertOrUpdate(BaseApiTokenInsertOrUpdateDTO dto) {

        Long userId = MyUserUtil.getCurrentUserId();

        if (MyUserUtil.getCurrentUserAdminFlag()) {

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
        baseApiTokenDO.setEnableFlag(BooleanUtil.isTrue(dto.getEnableFlag()));

        if (dto.getId() == null) {

            baseApiTokenDO.setToken(IdUtil.simpleUUID());
            baseApiTokenDO.setLastUseTime(DateUtil.parseDateTime("1970-01-01 00:00:00"));

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

        if (MyUserUtil.getCurrentUserAdminFlag()) {

            if (dto.getUserId() != null) {

                queryUserId = dto.getUserId();

            }

        } else {

            queryUserId = userId;

        }

        return lambdaQuery().like(StrUtil.isNotBlank(dto.getName()), BaseApiTokenDO::getName, dto.getName())
            .eq(queryUserId != null, BaseApiTokenDO::getUserId, queryUserId)
            .select(BaseApiTokenDO::getId, BaseApiTokenDO::getName, BaseApiTokenDO::getLastUseTime,
                BaseApiTokenDO::getEnableFlag).page(dto.createTimeDescDefaultOrderPage());

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseApiTokenDO infoById(NotNullId notNullId) {

        Long userId = MyUserUtil.getCurrentUserId();

        Long queryUserId = null;

        if (!MyUserUtil.getCurrentUserAdminFlag()) {

            queryUserId = userId;

        }

        return lambdaQuery().eq(BaseApiTokenDO::getId, notNullId.getId())
            .eq(queryUserId != null, BaseApiTokenDO::getUserId, queryUserId).one();

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

        if (!MyUserUtil.getCurrentUserAdminFlag()) {

            queryUserId = userId;

        }

        lambdaUpdate().in(BaseApiTokenDO::getId, idSet).eq(queryUserId != null, BaseApiTokenDO::getUserId, queryUserId)
            .remove();

        return TempBizCodeEnum.OK;

    }

}
