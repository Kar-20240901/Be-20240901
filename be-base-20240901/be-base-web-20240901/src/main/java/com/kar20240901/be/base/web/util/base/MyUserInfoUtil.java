package com.kar20240901.be.base.web.util.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.domain.base.TempUserInfoDO;
import com.kar20240901.be.base.web.model.enums.base.BaseRequestCategoryEnum;
import com.kar20240901.be.base.web.service.base.BaseUserInfoService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyUserInfoUtil {

    private static BaseUserInfoService baseUserInfoService;

    public MyUserInfoUtil(BaseUserInfoService baseUserInfoService) {

        MyUserInfoUtil.baseUserInfoService = baseUserInfoService;

    }

    private static CopyOnWriteArrayList<TempUserInfoDO> USER_INFO_DO_LIST = new CopyOnWriteArrayList<>();

    /**
     * 添加 备注：如果为 null，则不会更新该字段
     */
    public static void add(Long id, @Nullable Date lastActiveTime, @Nullable String lastIp,
        @Nullable String lastRegion) {

        if (id == null) {
            return;
        }

        if (TempConstant.NEGATIVE_ONE_LONG.equals(id)) {
            return;
        }

        if (lastActiveTime == null && lastIp == null && lastRegion == null) {
            return;
        }

        TempUserInfoDO tempUserInfoDO = new TempUserInfoDO();

        tempUserInfoDO.setId(id);

        tempUserInfoDO.setLastActiveTime(lastActiveTime);
        tempUserInfoDO.setLastIp(lastIp);

        if (StrUtil.isBlank(lastIp)) {

            tempUserInfoDO.setLastRegion(Ip2RegionUtil.getRegion(lastIp));

        } else {

            tempUserInfoDO.setLastRegion(lastRegion);

        }

        // 添加
        add(tempUserInfoDO);

    }

    /**
     * 添加
     */
    public static void add(TempUserInfoDO tempUserInfoDO) {

        if (tempUserInfoDO.getId() == null) {
            return;
        }

        USER_INFO_DO_LIST.add(tempUserInfoDO);

    }

    /**
     * 定时任务，保存数据
     */
    @PreDestroy
    @Scheduled(fixedDelay = 5000)
    public void scheduledSava() {

        CopyOnWriteArrayList<TempUserInfoDO> tempUserInfoDoList;

        synchronized (USER_INFO_DO_LIST) {

            if (CollUtil.isEmpty(USER_INFO_DO_LIST)) {
                return;
            }

            tempUserInfoDoList = USER_INFO_DO_LIST;
            USER_INFO_DO_LIST = new CopyOnWriteArrayList<>();

        }

        // 目的：防止还有程序往：tempMap，里面添加数据，所以这里等待一会
        MyThreadUtil.schedule(() -> {

            // 批量更新数据
            baseUserInfoService.updateBatchById(tempUserInfoDoList);

        }, DateUtil.offsetMillisecond(new Date(), 1500));

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
    public static final String WX_BASE_USER_INFO_NICKNAME_PRE = "微信用户";

    /**
     * 获取：带有昵称的 用户对象
     */
    @NotNull
    public static TempUserInfoDO getWxTempUserInfoDO() {

        TempUserInfoDO tempUserInfoDO = new TempUserInfoDO();

        tempUserInfoDO.setNickname(NicknameUtil.getRandomNickname(WX_BASE_USER_INFO_NICKNAME_PRE));

        return tempUserInfoDO;

    }

    // 企业微信用户：昵称前缀
    public static final String WX_WORK_BASE_USER_INFO_NICKNAME_PRE = "企业微信用户";

    /**
     * 获取：带有昵称的 用户对象
     */
    @NotNull
    public static TempUserInfoDO getWxWorkTempUserInfoDO() {

        TempUserInfoDO tempUserInfoDO = new TempUserInfoDO();

        tempUserInfoDO.setSignUpType(BaseRequestCategoryEnum.WX_WORK);

        tempUserInfoDO.setNickname(NicknameUtil.getRandomNickname(WX_WORK_BASE_USER_INFO_NICKNAME_PRE));

        return tempUserInfoDO;

    }

}
