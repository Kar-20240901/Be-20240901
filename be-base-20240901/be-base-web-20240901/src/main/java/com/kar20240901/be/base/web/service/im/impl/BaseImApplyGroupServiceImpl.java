package com.kar20240901.be.base.web.service.im.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.im.BaseImApplyGroupMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImGroupMapper;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.domain.im.BaseImApplyGroupDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImGroupDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyFriendSearchApplyGroupDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupSendDTO;
import com.kar20240901.be.base.web.model.vo.im.BaseImApplyFriendSearchApplyGroupVO;
import com.kar20240901.be.base.web.service.file.BaseFileService;
import com.kar20240901.be.base.web.service.im.BaseImApplyGroupService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BaseImApplyGroupServiceImpl extends ServiceImpl<BaseImApplyGroupMapper, BaseImApplyGroupDO>
    implements BaseImApplyGroupService {

    @Resource
    BaseImGroupMapper baseImGroupMapper;

    @Resource
    BaseFileService baseFileService;

    /**
     * 搜索要添加的群组
     */
    @Override
    public Page<BaseImApplyFriendSearchApplyGroupVO> searchApplyGroup(BaseImApplyFriendSearchApplyGroupDTO dto) {

        String name = dto.getName();

        Long groupId = dto.getGroupId();

        Page<BaseImApplyFriendSearchApplyGroupVO> resPage = new Page<>();

        if (StrUtil.isBlank(name) && groupId == null) {
            return resPage;
        }

        Page<BaseImGroupDO> page =
            ChainWrappers.lambdaQueryChain(baseImGroupMapper).eq(groupId != null, BaseImGroupDO::getId, groupId)
                .like(StrUtil.isNotBlank(name), BaseImGroupDO::getName, name)
                .select(BaseImGroupDO::getId, BaseImGroupDO::getName, BaseImGroupDO::getAvatarFileId)
                .page(dto.createTimeDescDefaultOrderPage(true));

        Set<Long> avatarFileIdSet =
            page.getRecords().stream().map(BaseImGroupDO::getAvatarFileId).filter(it -> it != TempConstant.NEGATIVE_ONE)
                .collect(Collectors.toSet());

        Map<Long, String> publicUrlMap = baseFileService.getPublicUrl(new NotEmptyIdSet(avatarFileIdSet)).getMap();

        List<BaseImApplyFriendSearchApplyGroupVO> list = new ArrayList<>();

        for (BaseImGroupDO item : page.getRecords()) {

            BaseImApplyFriendSearchApplyGroupVO baseImApplyFriendSearchApplyGroupVO =
                new BaseImApplyFriendSearchApplyGroupVO();

            baseImApplyFriendSearchApplyGroupVO.setGroupId(item.getId());

            baseImApplyFriendSearchApplyGroupVO.setName(item.getName());

            String avatarUrl = publicUrlMap.get(item.getAvatarFileId());

            baseImApplyFriendSearchApplyGroupVO.setAvatarUrl(avatarUrl);

        }

        resPage.setTotal(page.getTotal());

        resPage.setRecords(list);

        return resPage;

    }

    /**
     * 新增/修改
     */
    @Override
    public String insertOrUpdate(BaseImApplyGroupInsertOrUpdateDTO dto) {

        return TempBizCodeEnum.OK;

    }

    /**
     * 发送入群申请
     */
    @Override
    public String send(BaseImApplyGroupSendDTO dto) {

        return TempBizCodeEnum.OK;

    }

}
