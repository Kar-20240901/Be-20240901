package com.kar20240901.be.base.web.service.im.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.base.BaseUserInfoMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImSessionRefUserMapper;
import com.kar20240901.be.base.web.model.annotation.base.MyTransactional;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.domain.base.TempUserInfoDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionRefUserDO;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.im.BaseImSessionRefUserPageDTO;
import com.kar20240901.be.base.web.model.enums.im.BaseImTypeEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.im.BaseImSessionRefUserPageVO;
import com.kar20240901.be.base.web.service.im.BaseImSessionRefUserService;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BaseImSessionRefUserServiceImpl extends ServiceImpl<BaseImSessionRefUserMapper, BaseImSessionRefUserDO>
    implements BaseImSessionRefUserService {

    @Resource
    BaseUserInfoMapper baseUserInfoMapper;

    /**
     * 创建会话关联用户：好友
     */
    @Override
    @MyTransactional
    public void addSessionRefUserForFriend(Long sessionId, Long userId1, Long userId2) {

        Assert.notNull(sessionId);
        Assert.notNull(userId1);
        Assert.notNull(userId2);

        List<TempUserInfoDO> tempUserInfoDOList =
            ChainWrappers.lambdaQueryChain(baseUserInfoMapper).in(TempUserInfoDO::getId, userId1, userId2)
                .select(TempUserInfoDO::getId, TempUserInfoDO::getNickname, TempUserInfoDO::getAvatarFileId).list();

        if (tempUserInfoDOList.size() != 2) {
            R.error("操作失败：用户信息不存在，请重新申请", CollUtil.newArrayList(userId1, userId2));
        }

        Map<Long, TempUserInfoDO> userInfoMap =
            tempUserInfoDOList.stream().collect(Collectors.toMap(TempUserInfoDO::getId, it -> it));

        Date date = new Date();

        TempUserInfoDO tempUserInfoDo1 = userInfoMap.get(userId1);

        TempUserInfoDO tempUserInfoDo2 = userInfoMap.get(userId2);

        BaseImSessionRefUserDO baseImSessionRefUserDo1 = new BaseImSessionRefUserDO();

        baseImSessionRefUserDo1.setSessionId(sessionId);
        baseImSessionRefUserDo1.setUserId(userId1);
        baseImSessionRefUserDo1.setLastOpenTs(date.getTime());
        baseImSessionRefUserDo1.setShowFlag(true);
        baseImSessionRefUserDo1.setName(tempUserInfoDo2.getNickname());
        baseImSessionRefUserDo1.setLastReceiveTs(TempConstant.NEGATIVE_ONE);
        baseImSessionRefUserDo1.setAvatarFileId(tempUserInfoDo2.getAvatarFileId());
        baseImSessionRefUserDo1.setTargetId(userId2);
        baseImSessionRefUserDo1.setTargetType(BaseImTypeEnum.FRIEND.getCode());

        save(baseImSessionRefUserDo1);

        BaseImSessionRefUserDO baseImSessionRefUserDo2 = new BaseImSessionRefUserDO();

        baseImSessionRefUserDo2.setSessionId(sessionId);
        baseImSessionRefUserDo2.setUserId(userId2);
        baseImSessionRefUserDo2.setLastOpenTs(date.getTime());
        baseImSessionRefUserDo2.setShowFlag(true);
        baseImSessionRefUserDo2.setName(tempUserInfoDo1.getNickname());
        baseImSessionRefUserDo2.setLastReceiveTs(TempConstant.NEGATIVE_ONE);
        baseImSessionRefUserDo2.setAvatarFileId(tempUserInfoDo1.getAvatarFileId());
        baseImSessionRefUserDo2.setTargetId(userId2);
        baseImSessionRefUserDo2.setTargetType(BaseImTypeEnum.FRIEND.getCode());

        save(baseImSessionRefUserDo2);

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseImSessionRefUserPageVO> myPage(BaseImSessionRefUserPageDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        Page<BaseImSessionRefUserPageVO> resPage = baseMapper.myPage(dto.pageOrder(), dto);

        return null;

    }

    /**
     * 隐藏
     */
    @Override
    public String hidden(NotNullId dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        lambdaUpdate().eq(BaseImSessionRefUserDO::getUserId, currentUserId)
            .eq(BaseImSessionRefUserDO::getSessionId, dto.getId()).set(BaseImSessionRefUserDO::getShowFlag, false)
            .update();

        return TempBizCodeEnum.OK;

    }

}
