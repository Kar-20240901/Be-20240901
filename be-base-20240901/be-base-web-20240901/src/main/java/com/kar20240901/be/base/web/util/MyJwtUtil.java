package com.kar20240901.be.base.web.util;

import cn.hutool.core.convert.NumberWithFormat;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.model.configuration.IJwtGetAuthSetConfiguration;
import com.kar20240901.be.base.web.model.constant.SecurityConstant;
import com.kar20240901.be.base.web.model.enums.BaseRequestCategoryEnum;
import com.kar20240901.be.base.web.model.enums.TempRedisKeyEnum;
import com.kar20240901.be.base.web.model.vo.R;
import com.kar20240901.be.base.web.properties.BaseSecurityProperties;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static BaseSecurityProperties baseSecurityProperties;

    @Resource
    public void setBaseSecurityProperties(BaseSecurityProperties baseSecurityProperties) {
        MyJwtUtil.baseSecurityProperties = baseSecurityProperties;
    }

    @Nullable
    private static IJwtGetAuthSetConfiguration iJwtGetAuthSetConfiguration;

    @Autowired(required = false)
    public void setiJwtGetAuthSetConfiguration(IJwtGetAuthSetConfiguration iJwtGetAuthSetConfiguration) {
        MyJwtUtil.iJwtGetAuthSetConfiguration = iJwtGetAuthSetConfiguration;
    }

    private static RedissonClient redissonClient;

    @Resource
    public void setRedissonClient(RedissonClient redissonClient) {
        MyJwtUtil.redissonClient = redissonClient;
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

        NumberWithFormat numberWithFormat = (NumberWithFormat)claimsJson.get(MyJwtUtil.PAYLOAD_MAP_USER_ID_KEY);

        if (numberWithFormat == null) {
            return null;
        }

        return numberWithFormat.longValue();

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
     * 从请求头里，获取：jwt字符串
     */
    @Nullable
    public static String getJwtStrByRequest(HttpServletRequest request) {

        String authorization = request.getHeader(SecurityConstant.AUTHORIZATION);

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
    public static Set<String> getAuthSetByUserId(Long userId, JWT jwt) {

        if (userId == null) {
            R.error(TempBizCodeEnum.ILLEGAL_REQUEST); // 直接抛出异常
        }

        // admin账号，自带所有权限
        if (MyUserUtil.getCurrentUserAdminFlag(userId)) {
            return new HashSet<>();
        }

        if (iJwtGetAuthSetConfiguration != null) {

            return iJwtGetAuthSetConfiguration.getAuthSet(userId, jwt);

        }

        Set<String> defaultAuthSet =
            redissonClient.<String>getSet(TempRedisKeyEnum.DEFAULT_USER_AUTH_CACHE.name()).readAll();

        Set<String> authSet =
            redissonClient.<String>getSet(TempRedisKeyEnum.PRE_USER_AUTH.name() + ":" + userId).readAll();

        authSet.addAll(defaultAuthSet);

        return authSet;

    }

}
