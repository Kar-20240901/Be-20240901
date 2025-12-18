package com.kar20240901.be.base.web.service.wallet.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.configuration.wallet.BaseUserWalletUserSignConfiguration;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoId;
import com.kar20240901.be.base.web.model.domain.pay.BasePayDO;
import com.kar20240901.be.base.web.model.domain.wallet.BaseUserWalletDO;
import com.kar20240901.be.base.web.model.domain.wallet.BaseUserWalletLogDO;
import com.kar20240901.be.base.web.model.dto.base.ChangeBigDecimalNumberIdSetDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullLong;
import com.kar20240901.be.base.web.model.dto.pay.PayDTO;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserWalletPageDTO;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserWalletRechargeUserSelfDTO;
import com.kar20240901.be.base.web.model.enums.base.BaseRedisKeyEnum;
import com.kar20240901.be.base.web.model.enums.pay.BasePayRefStatusEnum;
import com.kar20240901.be.base.web.model.enums.pay.BasePayRefTypeEnum;
import com.kar20240901.be.base.web.model.enums.wallet.BaseUserWalletLogTypeEnum;
import com.kar20240901.be.base.web.model.interfaces.wallet.IBaseUserWalletLogType;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.pay.BuyVO;
import com.kar20240901.be.base.web.service.wallet.BaseUserWalletService;
import com.kar20240901.be.base.web.util.base.IdGeneratorUtil;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.base.RedissonUtil;
import com.kar20240901.be.base.web.util.pay.BasePayUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class BaseUserWalletServiceImpl extends ServiceImpl<BaseUserWalletMapper, BaseUserWalletDO>
    implements BaseUserWalletService {

    @Resource
    BaseUserWalletUserSignConfiguration baseUserWalletUserSignConfiguration;

    /**
     * 批量冻结
     */
    @Override
    public String frozenByIdSet(NotEmptyIdSet notEmptyIdSet) {

        // 改变：钱包冻结状态
        return changeEnableFlag(notEmptyIdSet, false);

    }

    /**
     * 改变：钱包冻结状态
     */
    @Override
    public String changeEnableFlag(NotEmptyIdSet notEmptyIdSet, boolean enableFlag) {

        Set<Long> idSet = notEmptyIdSet.getIdSet();

        if (CollUtil.isEmpty(idSet)) {
            return TempBizCodeEnum.OK;
        }

        return RedissonUtil.doMultiLock(BaseRedisKeyEnum.PRE_USER_WALLET.name() + ":", idSet, () -> {

            lambdaUpdate().in(BaseUserWalletDO::getId, notEmptyIdSet.getIdSet())
                .set(TempEntityNoId::getEnableFlag, enableFlag).update();

            return TempBizCodeEnum.OK;

        });

    }

    /**
     * 批量解冻
     */
    @Override
    public String thawByIdSet(NotEmptyIdSet notEmptyIdSet) {

        // 改变：钱包冻结状态
        return changeEnableFlag(notEmptyIdSet, true);

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseUserWalletDO> myPage(BaseUserWalletPageDTO dto) {

        // 执行
        return doMyPage(dto);

    }

    /**
     * 执行：分页排序查询
     */
    @Override
    public Page<BaseUserWalletDO> doMyPage(BaseUserWalletPageDTO dto) {

        return lambdaQuery().eq(dto.getId() != null, BaseUserWalletDO::getId, dto.getId()) //
            .eq(dto.getEnableFlag() != null, TempEntityNoId::getEnableFlag, dto.getEnableFlag()) //
            .le(dto.getEndWithdrawableMoney() != null, BaseUserWalletDO::getWithdrawableMoney,
                dto.getEndWithdrawableMoney()) //
            .ge(dto.getBeginWithdrawableMoney() != null, BaseUserWalletDO::getWithdrawableMoney,
                dto.getBeginWithdrawableMoney()) //
            .le(dto.getUtEndTime() != null, BaseUserWalletDO::getUpdateTime, dto.getUtEndTime()) //
            .ge(dto.getUtBeginTime() != null, BaseUserWalletDO::getUpdateTime, dto.getUtBeginTime()) //
            .page(dto.updateTimeDescDefaultOrderPage());

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseUserWalletDO infoById(NotNullLong notNullLong) {

        return lambdaQuery().eq(BaseUserWalletDO::getId, notNullLong.getValue()).one();

    }

    /**
     * 通过主键id，查看详情-用户
     */
    @Override
    public BaseUserWalletDO infoByIdUserSelf() {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        BaseUserWalletDO baseUserWalletDO = lambdaQuery().eq(BaseUserWalletDO::getId, currentUserId).one();

        if (baseUserWalletDO == null) {

            baseUserWalletDO = (BaseUserWalletDO)baseUserWalletUserSignConfiguration.signUp(currentUserId);

        }

        return baseUserWalletDO;

    }

    /**
     * 通过主键 idSet，加减可提现的钱
     */
    @Override
    @DSTransactional
    public String addWithdrawableMoneyBackground(ChangeBigDecimalNumberIdSetDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        BigDecimal addNumber = dto.getNumber();

        BaseUserWalletLogTypeEnum baseUserWalletLogTypeEnum =
            addNumber.compareTo(BigDecimal.ZERO) > 0 ? BaseUserWalletLogTypeEnum.ADD_BACKGROUND
                : BaseUserWalletLogTypeEnum.REDUCE_BACKGROUND;

        // 执行
        return doAddWithdrawableMoney(currentUserId, new Date(), dto.getIdSet(), addNumber, baseUserWalletLogTypeEnum,
            false, false, null, null, false);

    }

    /**
     * 执行：通过主键 idSet，加减可提现的钱
     *
     * @param idSet 用户主键 idSet
     */
    @Override
    @NotNull
    @DSTransactional
    public String doAddWithdrawableMoney(Long currentUserId, Date date, Set<Long> idSet, BigDecimal addNumber,
        IBaseUserWalletLogType iBaseUserWalletLogType, boolean lowErrorFlag, boolean checkWalletEnableFlag,
        @Nullable Long refId, @Nullable String refData, boolean highErrorFlag) {

        if (addNumber.equals(BigDecimal.ZERO)) {
            return TempBizCodeEnum.OK;
        }

        // 日志集合
        List<BaseUserWalletLogDO> baseUserWalletLogDoList = new ArrayList<>();

        RedissonUtil.doMultiLock(BaseRedisKeyEnum.PRE_USER_WALLET.name() + ":", idSet, () -> {

            List<BaseUserWalletDO> baseUserWalletDOList = lambdaQuery().in(BaseUserWalletDO::getId, idSet)
                .select(BaseUserWalletDO::getId, BaseUserWalletDO::getWithdrawableMoney, TempEntityNoId::getEnableFlag)
                .list();

            // 处理：baseUserWalletDOList
            handleBaseUserWalletDOList(currentUserId, date, addNumber, iBaseUserWalletLogType, lowErrorFlag,
                checkWalletEnableFlag, baseUserWalletLogDoList, baseUserWalletDOList, refId, refData, highErrorFlag);

            // 操作数据库
            updateBatchById(baseUserWalletDOList);

        });

        for (BaseUserWalletLogDO item : baseUserWalletLogDoList) {

            BaseUserWalletLogServiceImpl.add(item); // 保存日志

        }

        return TempBizCodeEnum.OK;

    }

    /**
     * 充值-用户自我
     */
    @Override
    @DSTransactional
    public BuyVO rechargeUserSelf(BaseUserWalletRechargeUserSelfDTO dto) {

        if (dto.getValue().compareTo(BigDecimal.ZERO) <= 0) {
            R.errorMsg("操作失败：充值金额必须大于 0");
        }

        Long currentUserId = MyUserUtil.getCurrentUserId();

        // 获取：支付对象
        PayDTO payDTO = getPayDTO(dto, currentUserId);

        // 调用支付
        BasePayDO basePayDO = BasePayUtil.pay(payDTO, tempBasePayDO -> {

            tempBasePayDO.setRefType(BasePayRefTypeEnum.WALLET_RECHARGE_USER.getCode());
            tempBasePayDO.setRefId(currentUserId);

            tempBasePayDO.setRefStatus(BasePayRefStatusEnum.WAIT_PAY.getCode());

        });

        // 返回：调用支付之后，返回的参数
        return new BuyVO(basePayDO.getPayType(), basePayDO.getPayReturnValue(), basePayDO.getId().toString(),
            basePayDO.getBasePayConfigurationId());

    }

    /**
     * 获取：PayDTO对象
     */
    @NotNull
    private PayDTO getPayDTO(BaseUserWalletRechargeUserSelfDTO dto, Long userId) {

        PayDTO payDTO = new PayDTO();

        payDTO.setPayType(dto.getPayType());
        payDTO.setUserId(userId);

        payDTO.setTotalAmount(dto.getValue());
        payDTO.setSubject("钱包充值");
        payDTO.setExpireTime(DateUtil.offsetMinute(new Date(), 30));

        return payDTO;

    }

    /**
     * 处理：baseUserWalletDOList
     */
    private void handleBaseUserWalletDOList(Long currentUserId, Date date, BigDecimal addNumber,
        IBaseUserWalletLogType iBaseUserWalletLogType, boolean lowErrorFlag, boolean checkWalletEnableFlag,
        List<BaseUserWalletLogDO> baseUserWalletLogDoList, List<BaseUserWalletDO> baseUserWalletDOList,
        @Nullable Long refId, @Nullable String refData, boolean highErrorFlag) {

        for (BaseUserWalletDO item : baseUserWalletDOList) {

            if (checkWalletEnableFlag) {
                if (BooleanUtil.isFalse(item.getEnableFlag())) {
                    R.error("操作失败：钱包已被冻结，请联系管理员", item.getId());
                }
            }

            BigDecimal preWithdrawableMoney = item.getWithdrawableMoney();

            // 处理：需要增加的钱
            handleAddNumber(addNumber, item);

            item.setUpdateId(null);
            item.setUpdateTime(null);

            if (item.getWithdrawableMoney().compareTo(BigDecimal.ZERO) < 0) {

                if (lowErrorFlag) {

                    R.error("操作失败：可提现余额不足",
                        StrUtil.format("id：{}，withdrawableMoney：{}", item.getId(), item.getWithdrawableMoney()));

                } else {

                    item.setWithdrawableMoney(BigDecimal.ZERO);

                }

            } else if (item.getWithdrawableMoney().compareTo(MAX_MONEY) > 0) {

                if (highErrorFlag) {

                    R.error("操作失败：超过最大值",
                        StrUtil.format("id：{}，withdrawableMoney：{}", item.getId(), item.getWithdrawableMoney()));

                } else {

                    item.setWithdrawableMoney(MAX_MONEY);

                }

            }

            // 新增日志
            baseUserWalletLogDoList.add(
                addBaseUserWalletLogDO(currentUserId, date, iBaseUserWalletLogType, refId, refData, item,
                    preWithdrawableMoney));

        }

    }

    /**
     * 新增日志
     */
    public static BaseUserWalletLogDO addBaseUserWalletLogDO(Long currentUserId, Date date,
        IBaseUserWalletLogType iBaseUserWalletLogType, @Nullable Long refId, @Nullable String refData,
        BaseUserWalletDO item, BigDecimal preWithdrawableMoney) {

        BaseUserWalletLogDO baseUserWalletLogDO = new BaseUserWalletLogDO();

        baseUserWalletLogDO.setUserId(item.getId());
        baseUserWalletLogDO.setName(iBaseUserWalletLogType.getName());

        baseUserWalletLogDO.setType(iBaseUserWalletLogType.getCode());

        baseUserWalletLogDO.setRefId(MyEntityUtil.getNotNullLong(refId));

        baseUserWalletLogDO.setRefData(MyEntityUtil.getNotNullStr(refData));

        baseUserWalletLogDO.setWithdrawableMoneyPre(preWithdrawableMoney);
        baseUserWalletLogDO.setWithdrawableMoneySuf(item.getWithdrawableMoney());

        baseUserWalletLogDO.setId(IdGeneratorUtil.nextId());
        baseUserWalletLogDO.setEnableFlag(true);
        baseUserWalletLogDO.setRemark("");
        baseUserWalletLogDO.setCreateId(currentUserId);
        baseUserWalletLogDO.setCreateTime(date);
        baseUserWalletLogDO.setUpdateId(currentUserId);
        baseUserWalletLogDO.setUpdateTime(date);

        // 通用：处理：BaseUserWalletLogDO
        commonHandleBaseUserWalletLogDO(baseUserWalletLogDO);

        return baseUserWalletLogDO;

    }

    public static final BigDecimal MAX_MONEY = new BigDecimal("9999999999.999");

    /**
     * 处理：需要增加的钱
     */
    private void handleAddNumber(BigDecimal addNumber, BaseUserWalletDO item) {

        // 9999999999.999：九十九亿...
        item.setWithdrawableMoney(item.getWithdrawableMoney().add(addNumber));

    }

    /**
     * 通用：处理：BaseUserWalletLogDO
     */
    public static void commonHandleBaseUserWalletLogDO(BaseUserWalletLogDO baseUserWalletLogDO) {

        baseUserWalletLogDO.setWithdrawableMoneyChange(
            baseUserWalletLogDO.getWithdrawableMoneySuf().subtract(baseUserWalletLogDO.getWithdrawableMoneyPre()));

    }

}
