package com.kar20240901.be.base.web.util.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.kar20240901.be.base.web.model.domain.TempUserInfoDO;
import com.kar20240901.be.base.web.model.domain.kafka.TempKafkaUserInfoDO;
import com.kar20240901.be.base.web.model.enums.base.BaseRequestCategoryEnum;
import com.kar20240901.be.base.web.service.BaseUserInfoService;
import com.kar20240901.be.base.web.util.kafka.TempKafkaUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyUserInfoUtil {

    private static BaseUserInfoService baseUserInfoService;

    public MyUserInfoUtil(BaseUserInfoService baseUserInfoService) {

        MyUserInfoUtil.baseUserInfoService = baseUserInfoService;

    }

    /**
     * 更新用户信息
     */
    public static void updateUserInfo(Long userId, Date lastActiveTime, String lastIp) {

        if (userId == null) {
            return;
        }

        TempKafkaUserInfoDO tempKafkaUserInfoDO = new TempKafkaUserInfoDO();

        tempKafkaUserInfoDO.setId(userId);
        tempKafkaUserInfoDO.setLastActiveTime(lastActiveTime);
        tempKafkaUserInfoDO.setLastIp(lastIp);

        TempKafkaUtil.sendTempUpdateUserInfoTopic(tempKafkaUserInfoDO);

    }

    /**
     * 通过：用户主键 idSet，获取：用户资料集合
     */
    @NotNull
    public static List<TempUserInfoDO> getUserInfoDoList(Set<Long> userIdSet) {

        if (CollUtil.isEmpty(userIdSet)) {
            return new ArrayList<>();
        }

        return baseUserInfoService.lambdaQuery().in(TempUserInfoDO::getId, userIdSet).list();

    }

    /**
     * 通过：用户主键 idSet，获取：用户资料 map，key：用户主键 id，value：用户资料
     */
    @NotNull
    public static Map<Long, TempUserInfoDO> getUserInfoDoMap(Set<Long> userIdSet) {

        if (CollUtil.isEmpty(userIdSet)) {
            return MapUtil.newHashMap();
        }

        List<TempUserInfoDO> userInfoDoList = getUserInfoDoList(userIdSet);

        return userInfoDoList.stream().collect(Collectors.toMap(TempUserInfoDO::getId, it -> it));

    }

    // 微信用户：昵称前缀
    public static final String WX_SYS_USER_INFO_NICKNAME_PRE = "微信用户";

    /**
     * 获取：带有昵称的 用户对象
     */
    @NotNull
    public static TempUserInfoDO getWxTempUserInfoDO() {

        TempUserInfoDO tempUserInfoDO = new TempUserInfoDO();

        tempUserInfoDO.setNickname(NicknameUtil.getRandomNickname(WX_SYS_USER_INFO_NICKNAME_PRE));

        return tempUserInfoDO;

    }

    // 企业微信用户：昵称前缀
    public static final String WX_WORK_SYS_USER_INFO_NICKNAME_PRE = "企业微信用户";

    /**
     * 获取：带有昵称的 用户对象
     */
    @NotNull
    public static TempUserInfoDO getWxWorkTempUserInfoDO() {

        TempUserInfoDO tempUserInfoDO = new TempUserInfoDO();

        tempUserInfoDO.setSignUpType(BaseRequestCategoryEnum.WX_WORK);

        tempUserInfoDO.setNickname(NicknameUtil.getRandomNickname(WX_WORK_SYS_USER_INFO_NICKNAME_PRE));

        return tempUserInfoDO;

    }

}
