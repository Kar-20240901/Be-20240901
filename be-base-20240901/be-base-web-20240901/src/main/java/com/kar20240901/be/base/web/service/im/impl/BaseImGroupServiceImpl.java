package com.kar20240901.be.base.web.service.im.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.base.BaseUserInfoMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImGroupMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImGroupRefUserMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImSessionRefUserMapper;
import com.kar20240901.be.base.web.model.annotation.base.MyTransactional;
import com.kar20240901.be.base.web.model.domain.base.TempUserInfoDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImGroupDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImGroupRefUserDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionRefUserDO;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupChangeBelongIdDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupRemoveUserDTO;
import com.kar20240901.be.base.web.model.enums.im.BaseImTypeEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.im.BaseImGroupService;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.im.BaseImGroupUtil;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BaseImGroupServiceImpl extends ServiceImpl<BaseImGroupMapper, BaseImGroupDO>
    implements BaseImGroupService {

    @Resource
    BaseImGroupRefUserMapper baseImGroupRefUserMapper;

    @Resource
    BaseImSessionRefUserMapper baseImSessionRefUserMapper;

    @Resource
    BaseUserInfoMapper baseUserInfoMapper;

    /**
     * 新增/修改
     */
    @Override
    public String insertOrUpdate(BaseImGroupInsertOrUpdateDTO dto) {

        BaseImGroupDO baseImGroupDO = new BaseImGroupDO();

        Long currentUserId = MyUserUtil.getCurrentUserId();

        if (dto.getId() == null) {

            baseImGroupDO.setBelongId(currentUserId);

        } else {

            // 检查：是否有权限
            BaseImGroupUtil.checkGroupAuth(dto.getId());

        }

        baseImGroupDO.setId(dto.getId());

        baseImGroupDO.setName(dto.getName());

        baseImGroupDO.setId(dto.getId());

        saveOrUpdate(baseImGroupDO);

        return TempBizCodeEnum.OK;

    }

    /**
     * 踢出群员
     */
    @Override
    @MyTransactional
    public String removeUser(BaseImGroupRemoveUserDTO dto) {

        // 检查：是否有权限
        BaseImGroupUtil.checkGroupAuth(dto.getGroupId());

        ChainWrappers.lambdaUpdateChain(baseImGroupRefUserMapper).eq(BaseImGroupRefUserDO::getGroupId, dto.getGroupId())
            .eq(BaseImGroupRefUserDO::getUserId, dto.getUserId()).remove();

        ChainWrappers.lambdaUpdateChain(baseImSessionRefUserMapper)
            .eq(BaseImSessionRefUserDO::getTargetId, dto.getGroupId())
            .eq(BaseImSessionRefUserDO::getTargetType, BaseImTypeEnum.GROUP)
            .eq(BaseImSessionRefUserDO::getUserId, dto.getUserId()).remove();

        return TempBizCodeEnum.OK;

    }

    /**
     * 修改群主
     */
    @Override
    public String changeBelongId(BaseImGroupChangeBelongIdDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        if (dto.getNewBelongId().equals(currentUserId)) {
            R.errorMsg("操作失败：不能转移给自己");
        }

        // 检查：是否有权限
        BaseImGroupUtil.checkGroupAuth(dto.getGroupId());

        boolean exists = ChainWrappers.lambdaQueryChain(baseImGroupRefUserMapper)
            .eq(BaseImGroupRefUserDO::getGroupId, dto.getGroupId())
            .eq(BaseImGroupRefUserDO::getUserId, dto.getNewBelongId()).exists();

        if (!exists) {
            R.error("操作失败：目标用户不在群里，无法转移", dto.getNewBelongId());
        }

        boolean existsUser =
            ChainWrappers.lambdaQueryChain(baseUserInfoMapper).eq(TempUserInfoDO::getId, dto.getNewBelongId()).exists();

        if (!existsUser) {
            R.error("操作失败：用户已经注销，无法转移", dto.getNewBelongId());
        }

        lambdaUpdate().eq(BaseImGroupDO::getId, dto.getGroupId()).set(BaseImGroupDO::getBelongId, dto.getNewBelongId())
            .update();

        return TempBizCodeEnum.OK;

    }

    /**
     * 解散群组
     */
    @Override
    public String deleteById(NotNullId dto) {

        // 检查：是否有权限
        BaseImGroupUtil.checkGroupAuth(dto.getId());

        return TempBizCodeEnum.OK;

    }

}
