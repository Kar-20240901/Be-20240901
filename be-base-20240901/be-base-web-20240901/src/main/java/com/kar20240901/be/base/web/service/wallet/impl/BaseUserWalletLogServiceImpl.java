package com.kar20240901.be.base.web.service.wallet.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletLogMapper;
import com.kar20240901.be.base.web.model.domain.wallet.BaseUserWalletLogDO;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserWalletLogPageDTO;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserWalletLogUserSelfPageDTO;
import com.kar20240901.be.base.web.service.wallet.BaseUserWalletLogService;
import com.kar20240901.be.base.web.util.base.MyThreadUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BaseUserWalletLogServiceImpl extends ServiceImpl<BaseUserWalletLogMapper, BaseUserWalletLogDO>
    implements BaseUserWalletLogService {

    private static CopyOnWriteArrayList<BaseUserWalletLogDO> BASE_USER_WALLET_LOG_DO_LIST =
        new CopyOnWriteArrayList<>();

    /**
     * 定时任务，保存数据
     */
    @PreDestroy
    @Scheduled(fixedDelay = 5000)
    public void scheduledSava() {

        CopyOnWriteArrayList<BaseUserWalletLogDO> tempBaseUserWalletLogDoList;

        synchronized (BASE_USER_WALLET_LOG_DO_LIST) {

            if (CollUtil.isEmpty(BASE_USER_WALLET_LOG_DO_LIST)) {
                return;
            }

            tempBaseUserWalletLogDoList = BASE_USER_WALLET_LOG_DO_LIST;
            BASE_USER_WALLET_LOG_DO_LIST = new CopyOnWriteArrayList<>();

        }

        // 目的：防止还有程序往：tempList，里面添加数据，所以这里等待一会
        MyThreadUtil.schedule(() -> {

            // 批量保存数据
            saveBatch(tempBaseUserWalletLogDoList);

        }, DateUtil.offsetMillisecond(new Date(), 1500));

    }

    /**
     * 添加：用户钱包操作日志
     */
    public static void add(BaseUserWalletLogDO baseUserWalletLogDO) {

        BASE_USER_WALLET_LOG_DO_LIST.add(baseUserWalletLogDO);

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseUserWalletLogDO> myPage(BaseUserWalletLogPageDTO dto) {

        return lambdaQuery().eq(dto.getUserId() != null, BaseUserWalletLogDO::getUserId, dto.getUserId()) //

            .eq(dto.getType() != null, BaseUserWalletLogDO::getType, dto.getType()) //

            .ne(BaseUserWalletLogDO::getWithdrawableMoneyChange, BigDecimal.ZERO) //

            .like(StrUtil.isNotBlank(dto.getName()), BaseUserWalletLogDO::getName, dto.getName()) //

            .le(dto.getCtEndTime() != null, BaseUserWalletLogDO::getCreateTime, dto.getCtEndTime()) //

            .ge(dto.getCtBeginTime() != null, BaseUserWalletLogDO::getCreateTime, dto.getCtBeginTime()) //

            .like(StrUtil.isNotBlank(dto.getRemark()), BaseUserWalletLogDO::getRemark, dto.getRemark()) //

            .page(dto.updateTimeDescDefaultOrderPage());

    }

    /**
     * 分页排序查询-用户自我
     */
    @Override
    public Page<BaseUserWalletLogDO> myPageUserSelf(BaseUserWalletLogUserSelfPageDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        BaseUserWalletLogPageDTO baseUserWalletLogPageDTO =
            BeanUtil.copyProperties(dto, BaseUserWalletLogPageDTO.class);

        baseUserWalletLogPageDTO.setUserId(currentUserId);

        // 执行
        return myPage(baseUserWalletLogPageDTO);

    }

}
