package com.kar20240901.be.base.web.service.wallet.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.wallet.BaseUserBankCardMapper;
import com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletWithdrawLogMapper;
import com.kar20240901.be.base.web.model.annotation.base.MyTransactional;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoId;
import com.kar20240901.be.base.web.model.domain.wallet.BaseUserBankCardDO;
import com.kar20240901.be.base.web.model.domain.wallet.BaseUserWalletDO;
import com.kar20240901.be.base.web.model.domain.wallet.BaseUserWalletWithdrawLogDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.base.NotNullIdAndStringValue;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserWalletWithdrawLogInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserWalletWithdrawLogInsertOrUpdateUserSelfDTO;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserWalletWithdrawLogPageDTO;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserWalletWithdrawLogPageUserSelfDTO;
import com.kar20240901.be.base.web.model.enums.base.BaseRedisKeyEnum;
import com.kar20240901.be.base.web.model.enums.wallet.BaseUserWalletLogTypeEnum;
import com.kar20240901.be.base.web.model.enums.wallet.BaseUserWalletWithdrawStatusEnum;
import com.kar20240901.be.base.web.model.vo.base.DictIntegerVO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.wallet.BaseUserWalletService;
import com.kar20240901.be.base.web.service.wallet.BaseUserWalletWithdrawLogService;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.base.RedissonUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class BaseUserWalletWithdrawLogServiceImpl
    extends ServiceImpl<BaseUserWalletWithdrawLogMapper, BaseUserWalletWithdrawLogDO>
    implements BaseUserWalletWithdrawLogService {

    @Resource
    BaseUserWalletService baseUserWalletService;

    @Resource
    BaseUserBankCardMapper baseUserBankCardMapper;

    /**
     * 下拉列表-提现状态
     */
    @Override
    public Page<DictIntegerVO> withdrawStatusDictList() {

        List<DictIntegerVO> dictVOList = new ArrayList<>();

        for (BaseUserWalletWithdrawStatusEnum item : BaseUserWalletWithdrawStatusEnum.values()) {

            dictVOList.add(new DictIntegerVO(item.getCode(), item.getName()));

        }

        return new Page<DictIntegerVO>().setTotal(dictVOList.size()).setRecords(dictVOList);

    }

    /**
     * 新增/修改
     */
    @Override
    @MyTransactional
    public String insertOrUpdate(BaseUserWalletWithdrawLogInsertOrUpdateDTO dto) {

        Long userId = dto.getUserId();

        // 执行
        return doInsertOrUpdate(dto, userId);

    }

    /**
     * 取消
     */
    @Override
    @MyTransactional
    public String cancel(NotNullId notNullId) {

        // 执行
        return doCancel(notNullId, false);

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseUserWalletWithdrawLogDO> myPage(BaseUserWalletWithdrawLogPageDTO dto) {

        Page<BaseUserWalletWithdrawLogDO> page =
            lambdaQuery().eq(dto.getUserId() != null, BaseUserWalletWithdrawLogDO::getUserId, dto.getUserId()) //
                .eq(dto.getId() != null, BaseUserWalletWithdrawLogDO::getId, dto.getId()) //
                .like(StrUtil.isNotBlank(dto.getBankCardNo()), BaseUserWalletWithdrawLogDO::getBankCardNo,
                    dto.getBankCardNo()) //
                .like(StrUtil.isNotBlank(dto.getOpenBankName()), BaseUserWalletWithdrawLogDO::getOpenBankName,
                    dto.getOpenBankName()) //
                .like(StrUtil.isNotBlank(dto.getBranchBankName()), BaseUserWalletWithdrawLogDO::getOpenBankName,
                    dto.getBranchBankName()) //
                .like(StrUtil.isNotBlank(dto.getPayeeName()), BaseUserWalletWithdrawLogDO::getPayeeName,
                    dto.getPayeeName()) //
                .like(StrUtil.isNotBlank(dto.getRejectReason()), BaseUserWalletWithdrawLogDO::getRejectReason,
                    dto.getRejectReason()) //
                .eq(dto.getWithdrawStatus() != null, BaseUserWalletWithdrawLogDO::getWithdrawStatus,
                    dto.getWithdrawStatus()) //
                .le(dto.getCtEndTime() != null, BaseUserWalletWithdrawLogDO::getCreateTime, dto.getCtEndTime()) //
                .ge(dto.getCtBeginTime() != null, BaseUserWalletWithdrawLogDO::getCreateTime, dto.getCtBeginTime()) //
                .le(dto.getEndWithdrawMoney() != null, BaseUserWalletWithdrawLogDO::getWithdrawMoney,
                    dto.getEndWithdrawMoney()) //
                .ge(dto.getBeginWithdrawMoney() != null, BaseUserWalletWithdrawLogDO::getWithdrawMoney,
                    dto.getBeginWithdrawMoney()) //
                .page(dto.updateTimeDescDefaultOrderPage());

        for (BaseUserWalletWithdrawLogDO item : page.getRecords()) {

            // 脱敏：BaseUserWalletWithdrawLogDO
            desensitizedSysUserWalletWithdrawLogDO(item);

        }

        return page;

    }

    /**
     * 脱敏：BaseUserWalletWithdrawLogDO
     */
    private void desensitizedSysUserWalletWithdrawLogDO(BaseUserWalletWithdrawLogDO baseUserWalletWithdrawLogDO) {

        if (baseUserWalletWithdrawLogDO == null) {
            return;
        }

        // 备注：需要和：银行卡的脱敏一致
        baseUserWalletWithdrawLogDO.setBankCardNo(
            StrUtil.cleanBlank(DesensitizedUtil.bankCard(baseUserWalletWithdrawLogDO.getBankCardNo()))); // 脱敏

        baseUserWalletWithdrawLogDO.setPayeeName(
            DesensitizedUtil.chineseName(baseUserWalletWithdrawLogDO.getPayeeName())); // 脱敏

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseUserWalletWithdrawLogDO infoById(NotNullId notNullId) {

        return lambdaQuery().eq(BaseUserWalletWithdrawLogDO::getId, notNullId.getId()).one();

    }

    /**
     * 分页排序查询-用户
     */
    @Override
    public Page<BaseUserWalletWithdrawLogDO> myPageUserSelf(BaseUserWalletWithdrawLogPageUserSelfDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        BaseUserWalletWithdrawLogPageDTO baseUserWalletWithdrawLogPageDTO =
            BeanUtil.copyProperties(dto, BaseUserWalletWithdrawLogPageDTO.class);

        baseUserWalletWithdrawLogPageDTO.setUserId(currentUserId);

        // 执行
        return myPage(baseUserWalletWithdrawLogPageDTO);

    }

    /**
     * 新增/修改-用户
     */
    @Override
    @MyTransactional
    public String insertOrUpdateUserSelf(BaseUserWalletWithdrawLogInsertOrUpdateUserSelfDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        // 执行
        return doInsertOrUpdate(dto, currentUserId);

    }

    /**
     * 执行：新增/修改-用户
     *
     * @param id 用户 id
     */
    @NotNull
    private String doInsertOrUpdate(BaseUserWalletWithdrawLogInsertOrUpdateUserSelfDTO dto, Long id) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        BaseUserWalletWithdrawLogDO baseUserWalletWithdrawLogDO = new BaseUserWalletWithdrawLogDO();

        baseUserWalletWithdrawLogDO.setUserId(id);

        baseUserWalletWithdrawLogDO.setWithdrawMoney(dto.getWithdrawMoney());

        // 查询：用户银行卡信息
        BaseUserBankCardDO baseUserBankCardDO =
            ChainWrappers.lambdaQueryChain(baseUserBankCardMapper).eq(BaseUserBankCardDO::getId, id).one();

        if (baseUserBankCardDO == null) {
            R.errorMsg("操作失败：请先绑定银行卡");
        }

        baseUserWalletWithdrawLogDO.setOpenBankName(baseUserBankCardDO.getOpenBankName());
        baseUserWalletWithdrawLogDO.setPayeeName(baseUserBankCardDO.getPayeeName());
        baseUserWalletWithdrawLogDO.setBankCardNo(baseUserBankCardDO.getBankCardNo());
        baseUserWalletWithdrawLogDO.setBranchBankName(baseUserBankCardDO.getBranchBankName());

        baseUserWalletWithdrawLogDO.setWithdrawStatus(BaseUserWalletWithdrawStatusEnum.COMMIT);

        baseUserWalletWithdrawLogDO.setRejectReason("");
        baseUserWalletWithdrawLogDO.setEnableFlag(true);
        baseUserWalletWithdrawLogDO.setRemark("");

        saveOrUpdate(baseUserWalletWithdrawLogDO); // 先操作数据库，原因：如果后面报错了，则会回滚该更新

        // 检查和增加：用户钱包的可提现余额
        baseUserWalletService.doAddWithdrawableMoney(currentUserId, new Date(), CollUtil.newHashSet(id),
            baseUserWalletWithdrawLogDO.getWithdrawMoney().negate(), BaseUserWalletLogTypeEnum.REDUCE_WITHDRAW, true,
            true, null, null, true, null);

        return TempBizCodeEnum.OK;

    }

    /**
     * 取消-用户
     */
    @Override
    @MyTransactional
    public String cancelUserSelf(NotNullId notNullId) {

        // 执行
        return doCancel(notNullId, true);

    }

    /**
     * 执行：取消
     */
    private String doCancel(NotNullId notNullId, boolean userSelfFlag) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        return RedissonUtil.doLock(BaseRedisKeyEnum.PRE_USER_WALLET_WITHDRAW_LOG.name() + ":" + notNullId.getId(),
            () -> {

                BaseUserWalletWithdrawLogDO baseUserWalletWithdrawLogDO =
                    lambdaQuery().eq(TempEntity::getId, notNullId.getId())
                        .eq(userSelfFlag, BaseUserWalletWithdrawLogDO::getUserId, currentUserId)
                        .select(TempEntity::getId, BaseUserWalletWithdrawLogDO::getWithdrawMoney,
                            BaseUserWalletWithdrawLogDO::getWithdrawStatus, BaseUserWalletWithdrawLogDO::getUserId)
                        .one();

                if (baseUserWalletWithdrawLogDO == null) {
                    R.error(TempBizCodeEnum.ILLEGAL_REQUEST, notNullId.getId());
                }

                // 只有待受理状态的提现记录，才可以取消
                if (!BaseUserWalletWithdrawStatusEnum.COMMIT.equals(baseUserWalletWithdrawLogDO.getWithdrawStatus())) {
                    R.error("操作失败：只能取消待受理状态的提现记录", notNullId.getId());
                }

                baseUserWalletWithdrawLogDO.setWithdrawStatus(BaseUserWalletWithdrawStatusEnum.CANCEL);

                updateById(baseUserWalletWithdrawLogDO); // 先更新提现记录状态，原因：如果后面报错了，则会回滚该更新

                // 检查和增加：用户钱包的可提现余额
                baseUserWalletService.doAddWithdrawableMoney(currentUserId, new Date(),
                    CollUtil.newHashSet(baseUserWalletWithdrawLogDO.getUserId()),
                    baseUserWalletWithdrawLogDO.getWithdrawMoney(), BaseUserWalletLogTypeEnum.REDUCE_WITHDRAW, true,
                    true, null, null, true, null);

                return TempBizCodeEnum.OK;

            });

    }

    /**
     * 受理-用户的提现记录
     */
    @Override
    @MyTransactional
    public String accept(NotEmptyIdSet notEmptyIdSet) {

        return RedissonUtil.doMultiLock(BaseRedisKeyEnum.PRE_USER_WALLET_WITHDRAW_LOG.name() + ":",
            notEmptyIdSet.getIdSet(), () -> {

                List<BaseUserWalletWithdrawLogDO> baseUserWalletWithdrawLogDOList =
                    lambdaQuery().in(TempEntity::getId, notEmptyIdSet.getIdSet())
                        .eq(BaseUserWalletWithdrawLogDO::getWithdrawStatus, BaseUserWalletWithdrawStatusEnum.COMMIT)
                        .list();

                if (CollUtil.isEmpty(baseUserWalletWithdrawLogDOList)) {
                    return TempBizCodeEnum.OK;
                }

                Map<Long, List<BaseUserWalletWithdrawLogDO>> groupMap = baseUserWalletWithdrawLogDOList.stream()
                    .collect(Collectors.groupingBy(BaseUserWalletWithdrawLogDO::getUserId));

                // 检查：用户钱包是否被冻结
                String resStr =
                    checkUserWallet(baseUserWalletWithdrawLogDOList, groupMap, notEmptyIdSet.getIdSet().size() == 1);

                if (StrUtil.isNotBlank(resStr)) {
                    return resStr;
                }

                if (CollUtil.isEmpty(baseUserWalletWithdrawLogDOList)) {
                    return TempBizCodeEnum.OK;
                }

                for (BaseUserWalletWithdrawLogDO item : baseUserWalletWithdrawLogDOList) {
                    item.setWithdrawStatus(BaseUserWalletWithdrawStatusEnum.ACCEPT);
                }

                updateBatchById(baseUserWalletWithdrawLogDOList);

                return TempBizCodeEnum.OK;

            });

    }

    /**
     * 检查：用户钱包是否被冻结
     */
    @Nullable
    private String checkUserWallet(List<BaseUserWalletWithdrawLogDO> baseUserWalletWithdrawLogDOList,
        Map<Long, List<BaseUserWalletWithdrawLogDO>> groupMap, boolean errorFlag) {

        return RedissonUtil.doMultiLock(BaseRedisKeyEnum.PRE_USER_WALLET.name() + ":", groupMap.keySet(), () -> {

            // 只要：没有被冻结的钱包
            List<BaseUserWalletDO> baseUserWalletDOList =
                baseUserWalletService.lambdaQuery().in(BaseUserWalletDO::getId, groupMap.keySet())
                    .eq(TempEntityNoId::getEnableFlag, true).select(BaseUserWalletDO::getId).list();

            if (CollUtil.isEmpty(baseUserWalletDOList)) {

                if (errorFlag) {
                    R.error("操作失败：钱包已被冻结，请联系管理员", baseUserWalletWithdrawLogDOList.get(0).getUserId());
                }

                return TempBizCodeEnum.OK;

            }

            baseUserWalletWithdrawLogDOList.clear(); // 先移除原始数据

            for (BaseUserWalletDO item : baseUserWalletDOList) {

                baseUserWalletWithdrawLogDOList.addAll(groupMap.get(item.getId())); // 再添加数据

            }

            return null;

        });

    }

    /**
     * 成功-用户的提现记录
     */
    @Override
    @MyTransactional
    public String success(NotNullId notNullId) {

        Set<Long> idSet = CollUtil.newHashSet(notNullId.getId());

        return RedissonUtil.doLock(BaseRedisKeyEnum.PRE_USER_WALLET_WITHDRAW_LOG.name() + ":" + notNullId.getId(),
            () -> {

                BaseUserWalletWithdrawLogDO baseUserWalletWithdrawLogDO =
                    lambdaQuery().eq(TempEntity::getId, notNullId.getId())
                        .eq(BaseUserWalletWithdrawLogDO::getWithdrawStatus, BaseUserWalletWithdrawStatusEnum.ACCEPT)
                        .select(TempEntity::getId, BaseUserWalletWithdrawLogDO::getUserId).one();

                if (baseUserWalletWithdrawLogDO == null) {
                    R.error("操作失败：只能成功受理中状态的提现记录", notNullId.getId());
                }

                // 只要：没有被冻结的钱包
                boolean userWalletEnableFlag = baseUserWalletService.lambdaQuery()
                    .eq(BaseUserWalletDO::getId, baseUserWalletWithdrawLogDO.getUserId())
                    .eq(TempEntityNoId::getEnableFlag, true).exists();

                if (!userWalletEnableFlag) {
                    R.error("操作失败：钱包已被冻结，请联系管理员", baseUserWalletWithdrawLogDO.getUserId());
                }

                baseUserWalletWithdrawLogDO.setWithdrawStatus(BaseUserWalletWithdrawStatusEnum.SUCCESS);

                updateById(baseUserWalletWithdrawLogDO); // 先更新提现记录状态，原因：如果后面报错了，则会回滚该更新

                return TempBizCodeEnum.OK;

            });

    }

    /**
     * 拒绝-用户的提现记录
     */
    @Override
    @MyTransactional
    public String reject(NotNullIdAndStringValue notNullIdAndStringValue) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        Set<Long> idSet = CollUtil.newHashSet(notNullIdAndStringValue.getId());

        return RedissonUtil.doLock(
            BaseRedisKeyEnum.PRE_USER_WALLET_WITHDRAW_LOG.name() + ":" + notNullIdAndStringValue.getId(), () -> {

                BaseUserWalletWithdrawLogDO baseUserWalletWithdrawLogDO =
                    lambdaQuery().eq(TempEntity::getId, notNullIdAndStringValue.getId())
                        .eq(BaseUserWalletWithdrawLogDO::getWithdrawStatus, BaseUserWalletWithdrawStatusEnum.ACCEPT)
                        .select(TempEntity::getId, BaseUserWalletWithdrawLogDO::getWithdrawMoney,
                            BaseUserWalletWithdrawLogDO::getUserId).one();

                if (baseUserWalletWithdrawLogDO == null) {
                    R.error("操作失败：只能拒绝受理中状态的提现记录", notNullIdAndStringValue.getId());
                }

                baseUserWalletWithdrawLogDO.setWithdrawStatus(BaseUserWalletWithdrawStatusEnum.REJECT);
                baseUserWalletWithdrawLogDO.setRejectReason(notNullIdAndStringValue.getValue());

                updateById(baseUserWalletWithdrawLogDO); // 先更新提现记录状态，原因：如果后面报错了，则会回滚该更新

                // 检查和增加：用户钱包的可提现余额
                baseUserWalletService.doAddWithdrawableMoney(currentUserId, new Date(),
                    CollUtil.newHashSet(baseUserWalletWithdrawLogDO.getUserId()),
                    baseUserWalletWithdrawLogDO.getWithdrawMoney(), BaseUserWalletLogTypeEnum.REDUCE_WITHDRAW, true,
                    true, null, null, true, null);

                return TempBizCodeEnum.OK;

            });

    }

}
