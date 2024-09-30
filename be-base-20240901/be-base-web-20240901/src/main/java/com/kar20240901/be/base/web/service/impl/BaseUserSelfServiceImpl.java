package com.kar20240901.be.base.web.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.BaseUserInfoMapper;
import com.kar20240901.be.base.web.mapper.BaseUserMapper;
import com.kar20240901.be.base.web.model.domain.BaseUserDO;
import com.kar20240901.be.base.web.model.domain.BaseUserInfoDO;
import com.kar20240901.be.base.web.model.domain.TempEntity;
import com.kar20240901.be.base.web.model.dto.BaseUserSelfUpdateInfoDTO;
import com.kar20240901.be.base.web.model.vo.BaseUserSelfInfoVO;
import com.kar20240901.be.base.web.properties.BaseSecurityProperties;
import com.kar20240901.be.base.web.service.BaseUserSelfService;
import com.kar20240901.be.base.web.util.MyEntityUtil;
import com.kar20240901.be.base.web.util.MyThreadUtil;
import com.kar20240901.be.base.web.util.MyUserUtil;
import com.kar20240901.be.base.web.util.NicknameUtil;
import java.util.concurrent.CountDownLatch;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class BaseUserSelfServiceImpl implements BaseUserSelfService {

    @Resource
    BaseSecurityProperties baseSecurityProperties;

    @Resource
    BaseUserInfoMapper baseUserInfoMapper;

    @Resource
    BaseUserMapper baseUserMapper;

    /**
     * 获取：当前用户，基本信息
     */
    @SneakyThrows
    @Override
    public BaseUserSelfInfoVO userSelfInfo() {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        BaseUserSelfInfoVO baseUserSelfInfoVO = new BaseUserSelfInfoVO();

        baseUserSelfInfoVO.setId(currentUserId);

        CountDownLatch countDownLatch = ThreadUtil.newCountDownLatch(2);

        MyThreadUtil.execute(() -> {

            BaseUserInfoDO baseUserInfoDO =
                ChainWrappers.lambdaQueryChain(baseUserInfoMapper).eq(BaseUserInfoDO::getId, currentUserId)
                    .select(BaseUserInfoDO::getAvatarFileId, BaseUserInfoDO::getNickname, BaseUserInfoDO::getBio).one();

            if (baseUserInfoDO != null) {

                baseUserSelfInfoVO.setAvatarFileId(baseUserInfoDO.getAvatarFileId());
                baseUserSelfInfoVO.setNickname(baseUserInfoDO.getNickname());
                baseUserSelfInfoVO.setBio(baseUserInfoDO.getBio());

            }

        }, countDownLatch);

        MyThreadUtil.execute(() -> {

            BaseUserDO baseUserDO = ChainWrappers.lambdaQueryChain(baseUserMapper).eq(TempEntity::getId, currentUserId)
                .select(BaseUserDO::getEmail, BaseUserDO::getPassword, BaseUserDO::getUsername, BaseUserDO::getPhone,
                    BaseUserDO::getWxOpenId, BaseUserDO::getCreateTime, BaseUserDO::getWxAppId).one();

            if (baseUserDO != null) {

                // 备注：要和 userMyPage接口保持一致
                baseUserSelfInfoVO.setEmail(DesensitizedUtil.email(baseUserDO.getEmail())); // 脱敏
                baseUserSelfInfoVO.setUsername(DesensitizedUtil.chineseName(baseUserDO.getUsername())); // 脱敏
                baseUserSelfInfoVO.setPhone(DesensitizedUtil.mobilePhone(baseUserDO.getPhone())); // 脱敏
                // 脱敏：只显示前 3位，后 4位
                baseUserSelfInfoVO.setWxOpenId(
                    StrUtil.hide(baseUserDO.getWxOpenId(), 3, baseUserDO.getWxOpenId().length() - 4));
                // 脱敏：只显示前 3位，后 4位
                baseUserSelfInfoVO.setWxAppId(
                    StrUtil.hide(baseUserDO.getWxAppId(), 3, baseUserDO.getWxAppId().length() - 4));

                baseUserSelfInfoVO.setPasswordFlag(StrUtil.isNotBlank(baseUserDO.getPassword()));
                baseUserSelfInfoVO.setCreateTime(baseUserDO.getCreateTime());

            }

        }, countDownLatch);

        countDownLatch.await();

        return baseUserSelfInfoVO;

    }

    /**
     * 当前用户：基本信息：修改
     */
    @Override
    public String userSelfUpdateInfo(BaseUserSelfUpdateInfoDTO dto) {

        Long currentUserIdNotAdmin = MyUserUtil.getCurrentUserIdNotAdmin();

        BaseUserInfoDO baseUserInfoDO = new BaseUserInfoDO();

        baseUserInfoDO.setId(currentUserIdNotAdmin);
        baseUserInfoDO.setNickname(MyEntityUtil.getNotNullStr(dto.getNickname(), NicknameUtil.getRandomNickname()));
        baseUserInfoDO.setBio(MyEntityUtil.getNotNullAndTrimStr(dto.getBio()));

        baseUserInfoMapper.updateById(baseUserInfoDO);

        return TempBizCodeEnum.OK;

    }

    /**
     * 当前用户：重置头像
     */
    @Override
    public String userSelfResetAvatar() {

        Long currentUserIdNotAdmin = MyUserUtil.getCurrentUserIdNotAdmin();

        ChainWrappers.lambdaUpdateChain(baseUserInfoMapper).eq(BaseUserInfoDO::getId, currentUserIdNotAdmin)
            .set(BaseUserInfoDO::getAvatarFileId, -1).update();

        return TempBizCodeEnum.OK;

    }

}
