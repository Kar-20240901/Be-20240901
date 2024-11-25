package com.kar20240901.be.base.web.configuration.security;

import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.mapper.base.BaseUserInfoMapper;
import com.kar20240901.be.base.web.mapper.base.BaseUserMapper;
import com.kar20240901.be.base.web.model.configuration.base.IUserSignConfiguration;
import com.kar20240901.be.base.web.model.domain.base.BaseUserDeleteLogDO;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.base.TempUserDO;
import com.kar20240901.be.base.web.model.domain.base.TempUserInfoDO;
import com.kar20240901.be.base.web.service.base.BaseUserDeleteLogService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class BaseSecurityUserSignConfiguration implements IUserSignConfiguration {

    @Resource
    BaseUserDeleteLogService baseUserDeleteLogService;

    @Resource
    BaseUserMapper baseUserMapper;

    @Resource
    BaseUserInfoMapper baseUserInfoMapper;

    @Override
    public Object signUp(@NotNull Long userId) {

        return null;

    }

    @Override
    public void delete(Set<Long> userIdSet) {

        List<TempUserDO> tempUserDoList =
            ChainWrappers.lambdaQueryChain(baseUserMapper).in(TempEntity::getId, userIdSet).list();

        List<TempUserInfoDO> tempUserInfoDoList =
            ChainWrappers.lambdaQueryChain(baseUserInfoMapper).in(TempUserInfoDO::getId, userIdSet).list();

        Map<Long, TempUserInfoDO> tempUserInfoDoMap =
            tempUserInfoDoList.stream().collect(Collectors.toMap(TempUserInfoDO::getId, it -> it));

        List<BaseUserDeleteLogDO> baseUserDeleteLogDoList = new ArrayList<>(userIdSet.size());

        for (TempUserDO item : tempUserDoList) {

            BaseUserDeleteLogDO baseUserDeleteLogDO = new BaseUserDeleteLogDO();

            TempUserInfoDO tempUserInfoDO = tempUserInfoDoMap.get(item.getId());

            baseUserDeleteLogDO.setId(item.getId());
            baseUserDeleteLogDO.setPassword(item.getPassword());
            baseUserDeleteLogDO.setEmail(item.getEmail());
            baseUserDeleteLogDO.setUsername(item.getUsername());
            baseUserDeleteLogDO.setPhone(item.getPhone());
            baseUserDeleteLogDO.setWxOpenId(item.getWxOpenId());
            baseUserDeleteLogDO.setWxAppId(item.getWxAppId());
            baseUserDeleteLogDO.setUuid(tempUserInfoDO.getUuid());
            baseUserDeleteLogDO.setNickname(tempUserInfoDO.getNickname());
            baseUserDeleteLogDO.setBio(tempUserInfoDO.getBio());
            baseUserDeleteLogDO.setAvatarFileId(tempUserInfoDO.getAvatarFileId());

            baseUserDeleteLogDO.setUserCreateTime(item.getCreateTime());

            baseUserDeleteLogDoList.add(baseUserDeleteLogDO);

        }

        baseUserDeleteLogService.saveBatch(baseUserDeleteLogDoList);

    }

}
