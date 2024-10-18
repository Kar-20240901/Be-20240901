package com.kar20240901.be.base.web.util.base;

import cn.hutool.core.convert.NumberWithFormat;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.func.VoidFunc0;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.base.TempUserInfoMapper;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.domain.TempUserDO;
import com.kar20240901.be.base.web.model.domain.TempUserInfoDO;
import com.kar20240901.be.base.web.model.enums.base.TempRedisKeyEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.redisson.api.RBatch;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MyUserUtil {

    private static RedissonClient redissonClient;

    @Resource
    public void setRedissonClient(RedissonClient redissonClient) {
        MyUserUtil.redissonClient = redissonClient;
    }

    private static TempUserInfoMapper tempUserInfoMapper;

    @Resource
    public void setTempUserInfoMapper(TempUserInfoMapper tempUserInfoMapper) {
        MyUserUtil.tempUserInfoMapper = tempUserInfoMapper;
    }

    /**
     * 获取当前 userId 这里只会返回实际的 userId，如果为 null，则会抛出异常
     */
    @NotNull
    public static Long getCurrentUserId() {

        Long userId = getCurrentUserIdWillNull();

        if (userId == null) {
            R.error(TempBizCodeEnum.NOT_LOGGED_IN_YET);
        }

        return userId;

    }

    /**
     * 获取当前 userId，如果是 admin账号，则会报错，只会返回 用户id，不会返回 null 因为 admin不支持一些操作，例如：修改密码，修改邮箱等
     */
    @NotNull
    public static Long getCurrentUserIdNotAdmin() {

        Long currentUserId = getCurrentUserId();

        if (MyUserUtil.getCurrentUserAdminFlag(currentUserId)) {
            R.error(TempBizCodeEnum.THE_ADMIN_ACCOUNT_DOES_NOT_SUPPORT_THIS_OPERATION);
        }

        return currentUserId;

    }

    /**
     * 这里只会返回实际的 userId 或者 -1，备注：-1表示没有 用户id，在大多数情况下，表示的是 系统 备注：尽量采用 {@link #getCurrentUserId} 方法
     */
    @NotNull
    public static Long getCurrentUserIdDefault() {

        Long userId = getCurrentUserIdWillNull();

        if (userId == null) {
            userId = TempConstant.SYS_ID;
        }

        return userId;

    }

    /**
     * 这里只会返回实际的 wxOpenId 或者 空字符串
     */
    @NotNull
    public static String getCurrentUserWxOpenIdDefault() {

        String currentUserWxOpenIdWillNull = getCurrentUserWxOpenIdWillNull();

        if (currentUserWxOpenIdWillNull == null) {
            currentUserWxOpenIdWillNull = "";
        }

        return currentUserWxOpenIdWillNull;

    }

    /**
     * 用户是否是系统管理员
     */
    public static boolean getCurrentUserAdminFlag() {

        return getCurrentUserAdminFlag(getCurrentUserIdDefault());

    }

    /**
     * 用户是否是系统管理员
     */
    public static boolean getCurrentUserAdminFlag(Long userId) {

        return TempConstant.ADMIN_ID.equals(userId);

    }

    /**
     * 获取当前用户的昵称 这里只会返回实际的昵称，如果为 null，则会抛出异常
     */
    @NotNull
    public static String getCurrentUserNickName() {

        Long userId = getCurrentUserId();

        TempUserInfoDO tempUserInfoDO =
            ChainWrappers.lambdaQueryChain(tempUserInfoMapper).eq(TempUserInfoDO::getId, userId)
                .select(TempUserInfoDO::getNickname).one();

        if (tempUserInfoDO == null) {
            return "";
        }

        return tempUserInfoDO.getNickname();

    }

    /**
     * 获取当前 wxAppId，注意：这里获取 wxAppId之后需要做 非空判断 这里只会返回实际的 wxAppId或者 null
     */
    @Nullable
    private static String getCurrentUserWxAppIdWillNull() {

        return MyJwtUtil.getPayloadMapWxAppIdValue(getSecurityContextHolderContextAuthenticationPrincipalJsonObject());

    }

    /**
     * 获取当前 wxOpenId，注意：这里获取 wxOpenId之后需要做 非空判断 这里只会返回实际的 wxOpenId或者 null
     */
    @Nullable
    private static String getCurrentUserWxOpenIdWillNull() {

        return MyJwtUtil.getPayloadMapWxOpenIdValue(getSecurityContextHolderContextAuthenticationPrincipalJsonObject());

    }

    /**
     * 获取当前 userId，注意：这里获取 userId之后需要做 非空判断 这里只会返回实际的 userId或者 null
     */
    @Nullable
    private static Long getCurrentUserIdWillNull() {

        return MyJwtUtil.getPayloadMapUserIdValue(getSecurityContextHolderContextAuthenticationPrincipalJsonObject());

    }

    /**
     * 获取：当前 security上下文里面存储的用户信息
     */
    @Nullable
    public static Authentication getSecurityContextHolderContextAuthentication() {

        return SecurityContextHolder.getContext().getAuthentication();

    }

    /**
     * 获取：当前 security上下文里面存储的用户信息
     */
    @Nullable
    public static JSONObject getSecurityContextHolderContextAuthenticationPrincipalJsonObject() {

        JSONObject result = null;

        if (SecurityContextHolder.getContext().getAuthentication() != null) {

            Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (principalObject instanceof JSONObject) {
                result = (JSONObject)principalObject;
            }

        }

        return result;

    }

    /**
     * 获取：当前 security上下文里面存储的用户信息，通过：key
     */
    @Nullable
    public static <T> T getSecurityContextHolderContextAuthenticationPrincipalJsonObjectValueByKey(String key) {

        JSONObject principalJson = getSecurityContextHolderContextAuthenticationPrincipalJsonObject();

        if (principalJson == null) {
            return null;
        }

        return (T)principalJson.get(key);

    }

    /**
     * 统一的：设置：用户冻结
     */
    public static void setDisable(Long userId) {

        redissonClient.getBucket(TempRedisKeyEnum.PRE_USER_DISABLE.name() + ":" + userId).set(DateUtil.now());

    }

    /**
     * 统一的：设置：用户冻结
     */
    public static void setDisable(Set<Long> userIdSet) {

        String now = DateUtil.now();

        RBatch rBatch = redissonClient.createBatch();

        for (Long item : userIdSet) {

            rBatch.<String>getBucket(TempRedisKeyEnum.PRE_USER_DISABLE.name() + ":" + item).setAsync(now);

        }

        rBatch.execute();

    }

    /**
     * 统一的：获取：用户冻结
     */
    public static boolean getDisable(Long userId) {

        return redissonClient.getBucket(TempRedisKeyEnum.PRE_USER_DISABLE.name() + ":" + userId).isExists();

    }

    /**
     * 统一的：删除：用户冻结
     */
    public static void removeDisable(Long userId) {

        redissonClient.getBucket(TempRedisKeyEnum.PRE_USER_DISABLE.name() + ":" + userId).delete();

    }

    /**
     * 统一的：删除：用户冻结
     */
    public static void removeDisable(Set<Long> userIdSet) {

        RKeys keys = redissonClient.getKeys();

        String[] redisKeyArr =
            userIdSet.stream().map(it -> TempRedisKeyEnum.PRE_USER_DISABLE.name() + ":" + it).toArray(String[]::new);

        keys.delete(redisKeyArr);

    }

    /**
     * 给 security设置用户信息，并执行方法
     */
    public static void securityContextHolderSetAuthenticationAndExecFun(VoidFunc0 voidFunc0, TempUserDO tempUserDO,
        boolean setAuthoritySetFlag) {

        // 执行
        securityContextHolderSetAuthenticationAndExecFun(voidFunc0, tempUserDO.getId(), tempUserDO.getWxAppId(),
            tempUserDO.getWxOpenId(), setAuthoritySetFlag);

    }

    /**
     * 给 security设置用户信息，并执行方法
     *
     * @param setAuthoritySetFlag 是否设置：权限
     */
    public static void securityContextHolderSetAuthenticationAndExecFun(VoidFunc0 voidFunc0, @Nullable Long userId,
        @Nullable String wxAppId, @Nullable String wxOpenId, boolean setAuthoritySetFlag) {

        JSONObject principalJson = JSONUtil.createObj();

        if (userId != null) {

            principalJson.set(MyJwtUtil.PAYLOAD_MAP_USER_ID_KEY, new NumberWithFormat(userId, null));

        }

        if (StrUtil.isNotBlank(wxAppId)) {

            principalJson.set(MyJwtUtil.PAYLOAD_MAP_WX_APP_ID_KEY, wxAppId);

        }

        if (StrUtil.isNotBlank(wxOpenId)) {

            principalJson.set(MyJwtUtil.PAYLOAD_MAP_WX_OPEN_ID_KEY, wxOpenId);

        }

        List<SimpleGrantedAuthority> authorityList = null;

        if (setAuthoritySetFlag) {

            Set<String> authSet = MyJwtUtil.getAuthSetByUserId(userId);

            authorityList = authSet.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        }

        // 把 principalJson 设置到：security的上下文里面
        SecurityContextHolder.getContext()
            .setAuthentication(new UsernamePasswordAuthenticationToken(principalJson, null, authorityList));

        MyTryUtil.tryCatchFinally(() -> {

            // 执行方法
            voidFunc0.call();

        }, () -> {

            SecurityContextHolder.clearContext(); // 清除：当前线程存储的值

        });

    }

}
