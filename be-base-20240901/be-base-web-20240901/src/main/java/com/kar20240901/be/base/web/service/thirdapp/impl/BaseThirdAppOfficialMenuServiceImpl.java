package com.kar20240901.be.base.web.service.thirdapp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.thirdapp.BaseThirdAppMapper;
import com.kar20240901.be.base.web.mapper.thirdapp.BaseThirdAppOfficialMenuMapper;
import com.kar20240901.be.base.web.model.constant.log.LogTopicConstant;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoId;
import com.kar20240901.be.base.web.model.domain.base.TempEntityTree;
import com.kar20240901.be.base.web.model.domain.thirdapp.BaseThirdAppDO;
import com.kar20240901.be.base.web.model.domain.thirdapp.BaseThirdAppOfficialMenuDO;
import com.kar20240901.be.base.web.model.dto.base.ChangeNumberDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.thirdapp.BaseThirdAppOfficialMenuInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.thirdapp.BaseThirdAppOfficialMenuPageDTO;
import com.kar20240901.be.base.web.model.enums.thirdapp.BaseThirdAppOfficialMenuButtonTypeEnum;
import com.kar20240901.be.base.web.model.enums.thirdapp.BaseThirdAppOfficialMenuTypeEnum;
import com.kar20240901.be.base.web.model.enums.thirdapp.BaseThirdAppTypeEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.thirdapp.BaseThirdAppOfficialMenuService;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import com.kar20240901.be.base.web.util.base.MyTreeUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = LogTopicConstant.THIRD_APP_OFFICIAL_MENU)
public class BaseThirdAppOfficialMenuServiceImpl
    extends ServiceImpl<BaseThirdAppOfficialMenuMapper, BaseThirdAppOfficialMenuDO>
    implements BaseThirdAppOfficialMenuService {

    @Resource
    BaseThirdAppMapper baseThirdAppMapper;

    /**
     * 新增/修改
     */
    @Override
    public String insertOrUpdate(BaseThirdAppOfficialMenuInsertOrUpdateDTO dto) {

        if (BaseThirdAppOfficialMenuButtonTypeEnum.MINIPROGRAM.equals(dto.getButtonType())) {

            if (StrUtil.isBlank(dto.getPagePath())) {
                R.error("操作失败：小程序的页面路径不能为空", dto.getName());
            }

        } else {

            dto.setPagePath("");

        }

        Long thirdAppId = dto.getThirdAppId();

        // 第三方应用
        BaseThirdAppDO baseThirdAppDO =
            ChainWrappers.lambdaQueryChain(baseThirdAppMapper).eq(TempEntity::getId, thirdAppId)
                .select(BaseThirdAppDO::getType).one();

        if (baseThirdAppDO == null) {
            R.error(TempBizCodeEnum.ILLEGAL_REQUEST, thirdAppId);
        }

        BaseThirdAppOfficialMenuDO baseThirdAppOfficialMenuDO = new BaseThirdAppOfficialMenuDO();

        baseThirdAppOfficialMenuDO.setThirdAppId(dto.getThirdAppId());

        Integer type = baseThirdAppDO.getType();

        // 暂时：只能配置：微信公众号类型的第三方应用
        if (BaseThirdAppTypeEnum.WX_OFFICIAL.getCode() == type) {

            baseThirdAppOfficialMenuDO.setType(BaseThirdAppOfficialMenuTypeEnum.WX_OFFICIAL.getCode());

        } else {

            R.error("操作失败：暂不支持配置该类型的第三方应用", dto.getThirdAppId());

        }

        // 同一个类型和同一个第三方 appId下，并且是按钮类型时，value 不能重复
        boolean exists = lambdaQuery().eq(BaseThirdAppOfficialMenuDO::getThirdAppId, dto.getThirdAppId())
            .ne(dto.getId() != null, TempEntity::getId, dto.getId())
            .eq(BaseThirdAppOfficialMenuDO::getType, baseThirdAppOfficialMenuDO.getType())
            .eq(BaseThirdAppOfficialMenuDO::getButtonType, BaseThirdAppOfficialMenuButtonTypeEnum.CLICK)
            .eq(BaseThirdAppOfficialMenuDO::getValue, dto.getValue()).exists();

        if (exists) {
            R.errorMsg("操作失败：同一个公众号下，按钮类型的菜单，值不能重复");
        }

        baseThirdAppOfficialMenuDO.setName(dto.getName());
        baseThirdAppOfficialMenuDO.setButtonType(dto.getButtonType());

        baseThirdAppOfficialMenuDO.setValue(dto.getValue());

        baseThirdAppOfficialMenuDO.setPagePath(MyEntityUtil.getNotNullStr(dto.getPagePath()));

        baseThirdAppOfficialMenuDO.setReplyContent(MyEntityUtil.getNotNullStr(dto.getReplyContent()));

        baseThirdAppOfficialMenuDO.setOrderNo(MyEntityUtil.getNotNullOrderNo(dto.getOrderNo()));
        baseThirdAppOfficialMenuDO.setPid(MyEntityUtil.getNotNullPid(dto.getPid()));

        baseThirdAppOfficialMenuDO.setId(dto.getId());
        baseThirdAppOfficialMenuDO.setEnableFlag(BooleanUtil.isTrue(dto.getEnableFlag()));
        baseThirdAppOfficialMenuDO.setRemark(MyEntityUtil.getNotNullStr(dto.getRemark()));

        saveOrUpdate(baseThirdAppOfficialMenuDO);

        return TempBizCodeEnum.OK;

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseThirdAppOfficialMenuDO> myPage(BaseThirdAppOfficialMenuPageDTO dto) {

        return lambdaQuery().eq(dto.getThirdAppId() != null, BaseThirdAppOfficialMenuDO::getThirdAppId,
                dto.getThirdAppId())
            .like(StrUtil.isNotBlank(dto.getName()), BaseThirdAppOfficialMenuDO::getName, dto.getName())
            .like(StrUtil.isNotBlank(dto.getValue()), BaseThirdAppOfficialMenuDO::getValue, dto.getValue())
            .like(StrUtil.isNotBlank(dto.getReplyContent()), BaseThirdAppOfficialMenuDO::getReplyContent,
                dto.getReplyContent()) //
            .like(StrUtil.isNotBlank(dto.getRemark()), TempEntityNoId::getRemark, dto.getRemark())
            .eq(dto.getType() != null, BaseThirdAppOfficialMenuDO::getType, dto.getType())
            .eq(dto.getButtonType() != null, BaseThirdAppOfficialMenuDO::getButtonType, dto.getButtonType())
            .eq(dto.getEnableFlag() != null, TempEntityNoId::getEnableFlag, dto.getEnableFlag())
            .orderByDesc(TempEntityTree::getOrderNo).page(dto.createTimeDescDefaultOrderPage());

    }

    /**
     * 查询：树结构
     */
    @Override
    public List<BaseThirdAppOfficialMenuDO> tree(BaseThirdAppOfficialMenuPageDTO dto) {

        // 根据条件进行筛选，得到符合条件的数据，然后再逆向生成整棵树，并返回这个树结构
        dto.setPageSizeAll(); // 不分页

        List<BaseThirdAppOfficialMenuDO> baseThirdAppOfficialMenuDOList = myPage(dto).getRecords();

        if (baseThirdAppOfficialMenuDOList.size() == 0) {
            return new ArrayList<>();
        }

        List<BaseThirdAppOfficialMenuDO> allList = lambdaQuery().list();

        if (allList.size() == 0) {
            return new ArrayList<>();
        }

        return MyTreeUtil.getFullTreeByDeepNode(baseThirdAppOfficialMenuDOList, allList);

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseThirdAppOfficialMenuDO infoById(NotNullId notNullId) {

        BaseThirdAppOfficialMenuDO baseThirdAppOfficialMenuDO =
            lambdaQuery().eq(TempEntity::getId, notNullId.getId()).one();

        MyEntityUtil.handlePid(baseThirdAppOfficialMenuDO);

        return baseThirdAppOfficialMenuDO;

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
    @DSTransactional
    public String addOrderNo(ChangeNumberDTO dto) {

        if (dto.getNumber() == 0) {
            return TempBizCodeEnum.OK;
        }

        List<BaseThirdAppOfficialMenuDO> baseThirdAppOfficialMenuDOList =
            lambdaQuery().in(TempEntity::getId, dto.getIdSet()).select(TempEntity::getId, TempEntityTree::getOrderNo)
                .list();

        for (BaseThirdAppOfficialMenuDO item : baseThirdAppOfficialMenuDOList) {
            item.setOrderNo((int)(item.getOrderNo() + dto.getNumber()));
        }

        updateBatchById(baseThirdAppOfficialMenuDOList);

        return TempBizCodeEnum.OK;

    }

}
