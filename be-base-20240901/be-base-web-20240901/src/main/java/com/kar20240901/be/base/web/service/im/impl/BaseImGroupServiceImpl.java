package com.kar20240901.be.base.web.service.im.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.base.BaseUserInfoMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImBlockMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImGroupMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImGroupRefUserMapper;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.domain.base.TempUserInfoDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImBlockDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImGroupDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImGroupRefUserDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupChangeBelongIdDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupPageDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupRemoveUserDTO;
import com.kar20240901.be.base.web.model.enums.im.BaseImTypeEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.im.BaseImGroupPageVO;
import com.kar20240901.be.base.web.service.file.BaseFileService;
import com.kar20240901.be.base.web.service.im.BaseImGroupRefUserService;
import com.kar20240901.be.base.web.service.im.BaseImGroupService;
import com.kar20240901.be.base.web.service.im.BaseImSessionRefUserService;
import com.kar20240901.be.base.web.service.im.BaseImSessionService;
import com.kar20240901.be.base.web.util.base.IdGeneratorUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.im.BaseImGroupUtil;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BaseImGroupServiceImpl extends ServiceImpl<BaseImGroupMapper, BaseImGroupDO>
    implements BaseImGroupService {

    @Resource
    BaseImGroupRefUserMapper baseImGroupRefUserMapper;

    @Resource
    BaseUserInfoMapper baseUserInfoMapper;

    @Resource
    BaseImBlockMapper baseImBlockMapper;

    @Resource
    BaseImSessionService baseImSessionService;

    @Resource
    BaseImSessionRefUserService baseImSessionRefUserService;

    @Resource
    BaseImGroupRefUserService baseImGroupRefUserService;

    @Resource
    BaseFileService baseFileService;

    /**
     * 新增/修改
     */
    @Override
    @DSTransactional
    public String insertOrUpdate(BaseImGroupInsertOrUpdateDTO dto) {

        BaseImGroupDO baseImGroupDO = new BaseImGroupDO();

        Long currentUserId = MyUserUtil.getCurrentUserId();

        baseImGroupDO.setName(dto.getName());

        if (dto.getId() == null) {

            Long groupId = IdGeneratorUtil.nextId();

            baseImGroupDO.setId(groupId);

            baseImGroupDO.setBelongId(currentUserId);

            Long sessionId = IdGeneratorUtil.nextId();

            baseImGroupDO.setAvatarFileId(TempConstant.NEGATIVE_ONE);

            baseImGroupDO.setSessionId(sessionId);

            save(baseImGroupDO);

            // 创建会话
            baseImSessionService.addSession(sessionId, TempConstant.NEGATIVE_ONE, BaseImTypeEnum.GROUP);

            // 创建会话关联用户
            baseImSessionRefUserService.addOrUpdateSessionRefUserForGroup(sessionId, groupId, currentUserId);

            // 添加群员
            baseImGroupRefUserService.addUser(sessionId, groupId, currentUserId);

        } else {

            // 检查：是否有权限
            BaseImGroupUtil.checkGroupAuth(dto.getId());

            baseImGroupDO.setId(dto.getId());

            updateById(baseImGroupDO);

        }

        return TempBizCodeEnum.OK;

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseImGroupPageVO> myPage(BaseImGroupPageDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        Page<BaseImGroupPageVO> page =
            baseMapper.myPage(dto.fieldDescDefaultOrderPage("a.create_time", true), dto, currentUserId);

        Set<Long> avatarFileIdSet =
            page.getRecords().stream().map(BaseImGroupPageVO::getAvatarFileId).collect(Collectors.toSet());

        Map<Long, String> publicUrlMap = baseFileService.getPublicUrl(new NotEmptyIdSet(avatarFileIdSet)).getMap();

        for (BaseImGroupPageVO item : page.getRecords()) {

            item.setAvatarUrl(publicUrlMap.get(item.getAvatarFileId()));

            item.setAvatarFileId(null);

        }

        return page;

    }

    /**
     * 踢出群员
     */
    @Override
    @DSTransactional
    public String removeUser(BaseImGroupRemoveUserDTO dto) {

        // 检查：是否有权限
        BaseImGroupUtil.checkGroupAuth(dto.getGroupId());

        ChainWrappers.lambdaUpdateChain(baseImGroupRefUserMapper).eq(BaseImGroupRefUserDO::getGroupId, dto.getGroupId())
            .eq(BaseImGroupRefUserDO::getUserId, dto.getUserId()).remove();

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
    @DSTransactional
    public String deleteById(NotNullId dto) {

        // 检查：是否有权限
        BaseImGroupUtil.checkGroupAuth(dto.getId());

        lambdaUpdate().eq(BaseImGroupDO::getId, dto.getId()).remove();

        ChainWrappers.lambdaUpdateChain(baseImGroupRefUserMapper).eq(BaseImGroupRefUserDO::getGroupId, dto.getId())
            .remove();

        ChainWrappers.lambdaUpdateChain(baseImBlockMapper).eq(BaseImBlockDO::getSourceId, dto.getId())
            .eq(BaseImBlockDO::getSourceType, BaseImTypeEnum.GROUP).remove();

        return TempBizCodeEnum.OK;

    }

}
