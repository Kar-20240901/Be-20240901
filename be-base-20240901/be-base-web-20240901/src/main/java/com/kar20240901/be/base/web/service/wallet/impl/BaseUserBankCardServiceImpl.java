package com.kar20240901.be.base.web.service.wallet.impl;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.wallet.BaseUserBankCardMapper;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.domain.wallet.BaseUserBankCardDO;
import com.kar20240901.be.base.web.model.dto.base.NotNullLong;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserBankCardInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserBankCardInsertOrUpdateUserSelfDTO;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserBankCardPageDTO;
import com.kar20240901.be.base.web.model.enums.wallet.BaseOpenBankNameEnum;
import com.kar20240901.be.base.web.model.vo.base.DictStringVO;
import com.kar20240901.be.base.web.service.wallet.BaseUserBankCardService;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class BaseUserBankCardServiceImpl extends ServiceImpl<BaseUserBankCardMapper, BaseUserBankCardDO>
    implements BaseUserBankCardService {

    /**
     * 新增/修改
     */
    @Override
    public String insertOrUpdate(BaseUserBankCardInsertOrUpdateDTO dto) {

        Long userId = dto.getId();

        // 执行
        return doInsertOrUpdate(dto, userId);

    }

    /**
     * 新增/修改-用户
     */
    @Override
    public String insertOrUpdateUserSelf(BaseUserBankCardInsertOrUpdateUserSelfDTO dto) {

        // 执行
        return doInsertOrUpdate(dto, null);

    }

    /**
     * 执行：新增/修改
     */
    @Override
    public String doInsertOrUpdate(BaseUserBankCardInsertOrUpdateUserSelfDTO dto, @Nullable Long userId) {

        Long id;

        if (userId == null) {

            id = MyUserUtil.getCurrentUserId();

        } else {

            id = userId;

        }

        boolean exists = lambdaQuery().eq(BaseUserBankCardDO::getId, id).exists();

        BaseUserBankCardDO baseUserBankCardDO = new BaseUserBankCardDO();

        baseUserBankCardDO.setId(id);
        baseUserBankCardDO.setBankCardNo(dto.getBankCardNo());
        baseUserBankCardDO.setOpenBankName(dto.getOpenBankName());
        baseUserBankCardDO.setBranchBankName(dto.getBranchBankName());

        if (exists) {

            if (!dto.getPayeeName().contains(TempConstant.ASTERISK)) {
                baseUserBankCardDO.setPayeeName(dto.getPayeeName()); // 防止脱敏数据，存入数据库
            }

        } else {

            baseUserBankCardDO.setPayeeName(dto.getPayeeName());

        }

        baseUserBankCardDO.setEnableFlag(true);
        baseUserBankCardDO.setRemark("");

        if (exists) { // 如果存在：则是修改

            updateById(baseUserBankCardDO);

        } else { // 如果不存在：则是新增

            save(baseUserBankCardDO);

        }

        return TempBizCodeEnum.OK;

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseUserBankCardDO> myPage(BaseUserBankCardPageDTO dto) {

        return doMyPage(dto);

    }

    /**
     * 执行：分页排序查询
     */
    @Override
    @NotNull
    public Page<BaseUserBankCardDO> doMyPage(BaseUserBankCardPageDTO dto) {

        Page<BaseUserBankCardDO> page = lambdaQuery() //
            .eq(dto.getId() != null, BaseUserBankCardDO::getId, dto.getId()) //
            .like(StrUtil.isNotBlank(dto.getBankCardNo()), BaseUserBankCardDO::getBankCardNo, dto.getBankCardNo()) //
            .eq(dto.getOpenBankName() != null, BaseUserBankCardDO::getOpenBankName, dto.getOpenBankName()) //
            .like(StrUtil.isNotBlank(dto.getBranchBankName()), BaseUserBankCardDO::getBranchBankName,
                dto.getBranchBankName()) //
            .like(StrUtil.isNotBlank(dto.getPayeeName()), BaseUserBankCardDO::getPayeeName, dto.getPayeeName()) //
            .page(dto.updateTimeDescDefaultOrderPage());

        for (BaseUserBankCardDO item : page.getRecords()) {

            // 脱敏：BaseUserBankCardDO
            desensitizedSysUserBankCardDO(item);

        }

        return page;

    }

    /**
     * 脱敏：BaseUserBankCardDO
     */
    public static void desensitizedSysUserBankCardDO(BaseUserBankCardDO baseUserBankCardDO) {

        if (baseUserBankCardDO == null) {
            return;
        }

        // 备注：需要和：提现记录的脱敏一致
        baseUserBankCardDO.setBankCardNo(
            StrUtil.cleanBlank(DesensitizedUtil.bankCard(baseUserBankCardDO.getBankCardNo()))); // 脱敏

        baseUserBankCardDO.setPayeeName(DesensitizedUtil.chineseName(baseUserBankCardDO.getPayeeName())); // 脱敏

    }

    /**
     * 下拉列表-开户行名称
     */
    @Override
    public Page<DictStringVO> openBankNameDictList() {

        return new Page<DictStringVO>().setTotal(BaseOpenBankNameEnum.DICT_VO_LIST.size())
            .setRecords(BaseOpenBankNameEnum.DICT_VO_LIST);

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseUserBankCardDO infoById(NotNullLong notNullLong) {

        return lambdaQuery().eq(BaseUserBankCardDO::getId, notNullLong.getValue()).one();

    }

    /**
     * 通过主键id，查看详情-用户
     */
    @Override
    public BaseUserBankCardDO infoByIdUserSelf() {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        BaseUserBankCardDO baseUserBankCardDO = lambdaQuery().eq(BaseUserBankCardDO::getId, currentUserId).one();

        // 脱敏：BaseUserBankCardDO
        desensitizedSysUserBankCardDO(baseUserBankCardDO);

        return baseUserBankCardDO;

    }

}
