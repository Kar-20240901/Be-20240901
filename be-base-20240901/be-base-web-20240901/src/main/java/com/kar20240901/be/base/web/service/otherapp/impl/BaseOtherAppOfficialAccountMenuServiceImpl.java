package com.kar20240901.be.base.web.service.otherapp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.otherapp.BaseOtherAppMapper;
import com.kar20240901.be.base.web.mapper.otherapp.BaseOtherAppOfficialAccountMenuMapper;
import com.kar20240901.be.base.web.model.annotation.base.MyTransactional;
import com.kar20240901.be.base.web.model.constant.log.LogTopicConstant;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.base.TempEntityTree;
import com.kar20240901.be.base.web.model.domain.otherapp.BaseOtherAppDO;
import com.kar20240901.be.base.web.model.domain.otherapp.BaseOtherAppOfficialAccountMenuDO;
import com.kar20240901.be.base.web.model.dto.base.ChangeNumberDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.enums.otherapp.BaseOtherAppOfficialAccountMenuButtonTypeEnum;
import com.kar20240901.be.base.web.model.enums.otherapp.BaseOtherAppOfficialAccountMenuTypeEnum;
import com.kar20240901.be.base.web.model.enums.otherapp.BaseOtherAppTypeEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.otherapp.BaseOtherAppOfficialAccountMenuService;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import com.kar20240901.be.base.web.util.base.MyTreeUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = LogTopicConstant.OTHER_APP_OFFICIAL_ACCOUNT_MENU)
public class BaseOtherAppOfficialAccountMenuServiceImpl
    extends ServiceImpl<BaseOtherAppOfficialAccountMenuMapper, BaseOtherAppOfficialAccountMenuDO>
    implements BaseOtherAppOfficialAccountMenuService {

    @Resource
    BaseOtherAppMapper baseOtherAppMapper;

    /**
     * 新增/修改
     */
    @Override
    public String insertOrUpdate(BaseOtherAppOfficialAccountMenuInsertOrUpdateDTO dto) {

        if (BaseOtherAppOfficialAccountMenuButtonTypeEnum.MINIPROGRAM.equals(dto.getButtonType())) {

            if (StrUtil.isBlank(dto.getPagePath())) {
                R.error("操作失败：小程序的页面路径不能为空", dto.getName());
            }

        } else {

            dto.setPagePath("");

        }

        Long otherAppId = dto.getOtherAppId();

        // 第三方应用，必须是在自己租户下
        BaseOtherAppDO baseOtherAppDO =
            ChainWrappers.lambdaQueryChain(baseOtherAppMapper).eq(TempEntity::getId, otherAppId)
                .select(BaseOtherAppDO::getType).one();

        if (baseOtherAppDO == null) {
            R.error(TempBizCodeEnum.ILLEGAL_REQUEST, otherAppId);
        }

        BaseOtherAppOfficialAccountMenuDO baseOtherAppOfficialAccountMenuDO = new BaseOtherAppOfficialAccountMenuDO();

        baseOtherAppOfficialAccountMenuDO.setOtherAppId(dto.getOtherAppId());

        Integer type = baseOtherAppDO.getType();

        // 暂时：只能配置：微信公众号类型的第三方应用
        if (BaseOtherAppTypeEnum.WX_OFFICIAL_ACCOUNT.getCode() == type) {

            baseOtherAppOfficialAccountMenuDO.setType(
                BaseOtherAppOfficialAccountMenuTypeEnum.WX_OFFICIAL_ACCOUNT.getCode());

        } else {

            R.error("操作失败：暂不支持配置该类型的第三方应用", dto.getOtherAppId());

        }

        // 同一个类型和同一个第三方 appId下，并且是按钮类型时，value 不能重复
        boolean exists = lambdaQuery().eq(BaseOtherAppOfficialAccountMenuDO::getOtherAppId, dto.getOtherAppId())
            .ne(dto.getId() != null, TempEntity::getId, dto.getId())
            .eq(BaseOtherAppOfficialAccountMenuDO::getType, baseOtherAppOfficialAccountMenuDO.getType())
            .eq(BaseOtherAppOfficialAccountMenuDO::getButtonType, BaseOtherAppOfficialAccountMenuButtonTypeEnum.CLICK)
            .eq(BaseOtherAppOfficialAccountMenuDO::getValue, dto.getValue()).exists();

        if (exists) {
            R.errorMsg("操作失败：同一个公众号下，按钮类型的菜单，值不能重复");
        }

        baseOtherAppOfficialAccountMenuDO.setName(dto.getName());
        baseOtherAppOfficialAccountMenuDO.setButtonType(dto.getButtonType());

        baseOtherAppOfficialAccountMenuDO.setValue(dto.getValue());

        baseOtherAppOfficialAccountMenuDO.setPagePath(MyEntityUtil.getNotNullStr(dto.getPagePath()));

        baseOtherAppOfficialAccountMenuDO.setReplyContent(MyEntityUtil.getNotNullStr(dto.getReplyContent()));

        baseOtherAppOfficialAccountMenuDO.setOrderNo(MyEntityUtil.getNotNullOrderNo(dto.getOrderNo()));
        baseOtherAppOfficialAccountMenuDO.setPid(MyEntityUtil.getNotNullParentId(dto.getParentId()));

        baseOtherAppOfficialAccountMenuDO.setId(dto.getId());
        baseOtherAppOfficialAccountMenuDO.setEnableFlag(BooleanUtil.isTrue(dto.getEnableFlag()));
        baseOtherAppOfficialAccountMenuDO.setRemark(MyEntityUtil.getNotNullStr(dto.getRemark()));

        saveOrUpdate(baseOtherAppOfficialAccountMenuDO);

        return TempBizCodeEnum.OK;

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseOtherAppOfficialAccountMenuDO> myPage(BaseOtherAppOfficialAccountMenuPageDTO dto) {

        return lambdaQuery().eq(dto.getOtherAppId() != null, BaseOtherAppOfficialAccountMenuDO::getOtherAppId,
                dto.getOtherAppId())
            .like(StrUtil.isNotBlank(dto.getName()), BaseOtherAppOfficialAccountMenuDO::getName, dto.getName())
            .like(StrUtil.isNotBlank(dto.getValue()), BaseOtherAppOfficialAccountMenuDO::getValue, dto.getValue())
            .like(StrUtil.isNotBlank(dto.getReplyContent()), BaseOtherAppOfficialAccountMenuDO::getReplyContent,
                dto.getReplyContent()) //
            .like(StrUtil.isNotBlank(dto.getRemark()), BaseEntity::getRemark, dto.getRemark())
            .eq(dto.getType() != null, BaseOtherAppOfficialAccountMenuDO::getType, dto.getType())
            .eq(dto.getButtonType() != null, BaseOtherAppOfficialAccountMenuDO::getButtonType, dto.getButtonType())
            .eq(dto.getEnableFlag() != null, BaseEntity::getEnableFlag, dto.getEnableFlag())
            .orderByDesc(TempEntityTree::getOrderNo).page(dto.page(true));

    }

    /**
     * 查询：树结构
     */
    @Override
    public List<BaseOtherAppOfficialAccountMenuDO> tree(BaseOtherAppOfficialAccountMenuPageDTO dto) {

        // 根据条件进行筛选，得到符合条件的数据，然后再逆向生成整棵树，并返回这个树结构
        dto.setPageSize(-1); // 不分页
        List<BaseOtherAppOfficialAccountMenuDO> baseOtherAppOfficialAccountMenuDOList = myPage(dto).getRecords();

        if (baseOtherAppOfficialAccountMenuDOList.size() == 0) {
            return new ArrayList<>();
        }

        List<BaseOtherAppOfficialAccountMenuDO> allList = lambdaQuery().list();

        if (allList.size() == 0) {
            return new ArrayList<>();
        }

        return MyTreeUtil.getFullTreeByDeepNode(baseOtherAppOfficialAccountMenuDOList, allList);

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseOtherAppOfficialAccountMenuDO infoById(NotNullId notNullId) {

        BaseOtherAppOfficialAccountMenuDO baseOtherAppOfficialAccountMenuDO =
            lambdaQuery().eq(TempEntity::getId, notNullId.getId()).one();

        MyEntityUtil.handleParentId(baseOtherAppOfficialAccountMenuDO);

        return baseOtherAppOfficialAccountMenuDO;

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

        removeByIds(idSet); // 根据 idSet删除

        return TempBizCodeEnum.OK;

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

        List<BaseOtherAppOfficialAccountMenuDO> baseOtherAppOfficialAccountMenuDOList =
            lambdaQuery().in(TempEntity::getId, dto.getIdSet()).select(BaseEntity::getId, BaseEntityTree::getOrderNo)
                .list();

        for (BaseOtherAppOfficialAccountMenuDO item : baseOtherAppOfficialAccountMenuDOList) {
            item.setOrderNo((int)(item.getOrderNo() + dto.getNumber()));
        }

        updateBatchById(baseOtherAppOfficialAccountMenuDOList);

        return TempBizCodeEnum.OK;

    }

}
