package com.kar20240901.be.base.web.util.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.NumberWithFormat;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.base.BaseAuthMapper;
import com.kar20240901.be.base.web.mapper.base.BaseRoleMapper;
import com.kar20240901.be.base.web.mapper.base.BaseRoleRefAuthMapper;
import com.kar20240901.be.base.web.mapper.base.BaseRoleRefUserMapper;
import com.kar20240901.be.base.web.model.constant.base.SecurityConstant;
import com.kar20240901.be.base.web.model.enums.base.BaseRequestCategoryEnum;
import com.kar20240901.be.base.web.model.enums.base.TempRedisKeyEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.properties.base.BaseSecurityProperties;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyJwtUtil {

    // 系统里的 jwt密钥
    private static final String JWT_SECRET_SYS =
        "4382dde8cb54c0c68082ada1b1d1ce048195cd309jqk0e07d2ed3e1231b462a8b75fee46467b96f33dea66a11863f1ea4867aed76243dfe7e1efb89638d3da6570d1";

    // 系统里的 jwt刷新token密钥
    private static final String JWT_REFRESH_TOKEN_SECRET_SYS =
        "4382dde8cb54c0casd82ada1b1d1ce048195cd309jqk0e07d2ed3e1871b462a8b75ghj46467b96f33dea66a11863f1ea4867aed76243dfe7e1efb89638d3da6570d1";

    public static final String PAYLOAD_MAP_USER_ID_KEY = "userId";

    public static final String PAYLOAD_MAP_WX_APP_ID_KEY = "wxAppId";

    public static final String PAYLOAD_MAP_WX_OPEN_ID_KEY = "wxOpenId";

    public static final String PAYLOAD_MAP_ADMIN_FLAG_KEY = "adminFlag";
    // 管理员标识，判断逻辑：权限里面是否有：baseRoleKey:admin，或者是超级管理员

    private static BaseSecurityProperties baseSecurityProperties;

    @Resource
    public void setBaseSecurityProperties(BaseSecurityProperties baseSecurityProperties) {
        MyJwtUtil.baseSecurityProperties = baseSecurityProperties;
    }

    private static RedissonClient redissonClient;

    @Resource
    public void setRedissonClient(RedissonClient redissonClient) {
        MyJwtUtil.redissonClient = redissonClient;
    }

    private static BaseRoleMapper baseRoleMapper;

    @Resource
    public void setBaseRoleMapper(BaseRoleMapper baseRoleMapper) {
        MyJwtUtil.baseRoleMapper = baseRoleMapper;
    }

    private static BaseRoleRefAuthMapper baseRoleRefAuthMapper;

    @Resource
    public void setBaseRoleRefAuthMapper(BaseRoleRefAuthMapper baseRoleRefAuthMapper) {
        MyJwtUtil.baseRoleRefAuthMapper = baseRoleRefAuthMapper;
    }

    private static BaseRoleRefUserMapper baseRoleRefUserMapper;

    @Resource
    public void setBaseRoleRefUserMapper(BaseRoleRefUserMapper baseRoleRefUserMapper) {
        MyJwtUtil.baseRoleRefUserMapper = baseRoleRefUserMapper;
    }

    private static BaseAuthMapper baseAuthMapper;

    @Resource
    public void setBaseAuthMapper(BaseAuthMapper baseAuthMapper) {
        MyJwtUtil.baseAuthMapper = baseAuthMapper;
    }

    /**
     * 获取：jwt中的 是否是管理员
     */
    public static boolean getPayloadMapAdminFlagValue(@Nullable JSONObject claimsJson) {

        if (claimsJson == null) {
            return false;
        }

        return claimsJson.getBool(MyJwtUtil.PAYLOAD_MAP_ADMIN_FLAG_KEY, false);

    }

    /**
     * 获取：jwt中的 wxAppId值
     */
    @Nullable
    public static String getPayloadMapWxAppIdValue(@Nullable JSONObject claimsJson) {

        if (claimsJson == null) {
            return null;
        }

        return claimsJson.getStr(MyJwtUtil.PAYLOAD_MAP_WX_APP_ID_KEY);

    }

    /**
     * 获取：jwt中的 wxOpenId值
     */
    @Nullable
    public static String getPayloadMapWxOpenIdValue(@Nullable JSONObject claimsJson) {

        if (claimsJson == null) {
            return null;
        }

        return claimsJson.getStr(MyJwtUtil.PAYLOAD_MAP_WX_OPEN_ID_KEY);

    }

    /**
     * 获取：jwt中的 userId值
     */
    @Nullable
    public static Long getPayloadMapUserIdValue(@Nullable JSONObject claimsJson) {

        if (claimsJson == null) {
            return null;
        }

        Object userId = claimsJson.get(MyJwtUtil.PAYLOAD_MAP_USER_ID_KEY);

        if (userId == null) {
            return null;
        }

        if (userId instanceof NumberWithFormat) {

            return ((NumberWithFormat)userId).longValue();

        } else if (userId instanceof Long) {

            return (Long)userId;

        } else {

            return null;

        }

    }

    /**
     * 生成 redis中，jwt存储使用的 key
     */
    @NotNull
    public static String generateRedisJwt(String jwtStr, Long userId, BaseRequestCategoryEnum baseRequestCategoryEnum) {

        StrBuilder strBuilder = StrBuilder.create();

        strBuilder.append(TempRedisKeyEnum.PRE_JWT.name()).append(":").append(userId).append(":")
            .append(baseRequestCategoryEnum.getCode()).append(":").append(jwtStr);

        return strBuilder.toString();

    }

    /**
     * 生成 redis中，jwt刷新token存储使用的 key
     */
    @NotNull
    public static String generateRedisJwtRefreshToken(String jwtRefreshStr, Long userId) {

        StrBuilder strBuilder = StrBuilder.create();

        strBuilder.append(TempRedisKeyEnum.PRE_JWT_REFRESH_TOKEN.name()).append(":").append(userId).append(":")
            .append(jwtRefreshStr);

        return strBuilder.toString();

    }

    /**
     * 获取 jwt密钥：配置的私钥前缀 + JWT_SECRET_SYS
     */
    @NotNull
    public static String getJwtSecret() {

        return MyJwtUtil.baseSecurityProperties.getJwtSecretPre() + MyJwtUtil.JWT_SECRET_SYS;

    }

    /**
     * 获取 jwt刷新token密钥：配置的私钥前缀 + JWT_SECRET_SYS
     */
    @NotNull
    public static String getJwtRefreshTokenSecret() {

        return MyJwtUtil.baseSecurityProperties.getJwtRefreshTokenSecretPre() + MyJwtUtil.JWT_REFRESH_TOKEN_SECRET_SYS;

    }

    /**
     * 从请求头或者url里，获取：apiToken字符串
     */
    @Nullable
    public static String getApiTokenByRequest(HttpServletRequest request) {

        String apiToken = request.getHeader(SecurityConstant.BE_API_TOKEN);

        if (apiToken == null) {
            apiToken = request.getParameter(SecurityConstant.BE_API_TOKEN);
        }

        if (StrUtil.isBlank(apiToken)) {
            return null;
        }

        return apiToken;

    }

    /**
     * 从请求头或者url里，获取：jwt字符串
     */
    @Nullable
    public static String getJwtStrByRequest(HttpServletRequest request) {

        String authorization = request.getHeader(SecurityConstant.AUTHORIZATION);

        if (authorization == null) {
            authorization = request.getParameter(SecurityConstant.AUTHORIZATION);
        }

        if (authorization == null || authorization.startsWith(SecurityConstant.JWT_PREFIX) == false) {
            return null;
        }

        String jwtStr = getJwtStrByHeadAuthorization(authorization);

        if (StrUtil.isBlank(jwtStr)) {
            return null;
        }

        return jwtStr;

    }

    /**
     * 获取：jwtStr
     */
    public static String getJwtStrByHeadAuthorization(String authorization) {

        return authorization.replace(SecurityConstant.JWT_PREFIX, "");

    }

    /**
     * 通过 userId获取到权限的集合
     */
    @NotNull
    public static Set<String> getAuthSetByUserId(Long userId) {

        if (userId == null) {
            R.error(TempBizCodeEnum.ILLEGAL_REQUEST); // 直接抛出异常
        }

        // admin账号，自带所有权限
        if (MyUserUtil.getCurrentUserSuperAdminFlag(userId)) {
            return new HashSet<>();
        }

        RSet<String> rSet = redissonClient.getSet(TempRedisKeyEnum.PRE_USER_AUTH.name() + ":" + userId);

        Set<String> authSet = rSet.readAll();

        if (CollUtil.isEmpty(authSet)) {

            authSet = baseAuthMapper.getAuthSetByUserId(userId);

            if (CollUtil.isEmpty(authSet)) {

                rSet.add(null); // 备注：redis是支持 list和 set里存放 null元素的

            } else {

                rSet.addAll(authSet);

            }

        }

        return authSet;

    }

}
