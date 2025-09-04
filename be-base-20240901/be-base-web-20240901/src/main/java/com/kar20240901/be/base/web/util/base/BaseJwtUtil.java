package com.kar20240901.be.base.web.util.base;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import com.kar20240901.be.base.web.model.constant.base.SecurityConstant;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.enums.base.BaseRedisKeyEnum;
import com.kar20240901.be.base.web.model.enums.base.BaseRequestCategoryEnum;
import com.kar20240901.be.base.web.model.vo.base.SignInVO;
import java.time.Duration;
import java.util.Date;
import java.util.Set;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BaseJwtUtil {

    // 是否是：管理员，备注：只要权限里面有该值，则表示是管理员，并且超级管理员只有一个，管理员可以有很多个
    private static final String ADMIN_FLAG = "baseRoleKey:admin";

    /**
     * 统一生成 jwt
     */
    @Nullable
    public static SignInVO generateJwt(Long userId, @Nullable Consumer<JSONObject> consumer,
        boolean generateRefreshTokenFlag, BaseRequestCategoryEnum baseRequestCategoryEnum,
        @Nullable String jwtRefreshToken) {

        if (userId == null) {
            return null;
        }

        RedissonUtil.batch(batch -> {

            // 移除密码错误次数相关
            batch.getBucket(BaseRedisKeyEnum.PRE_PASSWORD_ERROR_COUNT.name() + ":" + userId).deleteAsync();
            batch.getBucket(BaseRedisKeyEnum.PRE_TOO_MANY_PASSWORD_ERROR.name() + ":" + userId).deleteAsync();

        });

        // 生成 jwt
        return sign(userId, consumer, generateRefreshTokenFlag, baseRequestCategoryEnum, jwtRefreshToken);

    }

    /**
     * 生成 jwt
     */
    @NotNull
    private static SignInVO sign(Long userId, @Nullable Consumer<JSONObject> consumer, boolean generateRefreshTokenFlag,
        BaseRequestCategoryEnum baseRequestCategoryEnum, @Nullable String jwtRefreshTokenTemp) {

        // 备注：jwtRefreshToken请求，需要同步进行修改
        JSONObject payloadMap = JSONUtil.createObj();

        payloadMap.set(MyJwtUtil.PAYLOAD_MAP_USER_ID_KEY, userId);

        boolean adminFlag = false;

        if (MyUserUtil.getCurrentUserSuperAdminFlag(userId)) {

            adminFlag = true;

        } else {

            Set<String> authSet = MyJwtUtil.getAuthSetByUserId(userId);

            if (authSet.contains(ADMIN_FLAG)) {

                adminFlag = true;

            }

        }

        payloadMap.set(MyJwtUtil.PAYLOAD_MAP_ADMIN_FLAG_KEY, adminFlag);

        if (consumer != null) {
            consumer.accept(payloadMap);
        }

        long jwtExpireTime = TempConstant.JWT_EXPIRE_TIME;

        // 过期时间
        Date expireTs = new Date(System.currentTimeMillis() + jwtExpireTime);

        String jwt = JWT.create() //
            .setExpiresAt(expireTs) // 设置过期时间
            .addPayloads(payloadMap) // 增加JWT载荷信息
            .setKey(MyJwtUtil.getJwtSecret().getBytes()) // 设置密钥
            .sign();

        String jwtRefreshToken;

        if (generateRefreshTokenFlag) {

            jwtRefreshToken = JWT.create() //
                .addPayloads(payloadMap) // 增加JWT载荷信息
                .setKey(MyJwtUtil.getJwtRefreshTokenSecret().getBytes()) // 设置密钥
                .sign();

        } else {

            jwtRefreshToken = jwtRefreshTokenTemp;

        }

        RedissonUtil.batch(batch -> {

            if (generateRefreshTokenFlag) {

                batch.getBucket(MyJwtUtil.generateRedisJwtRefreshToken(jwtRefreshToken, userId))
                    .setAsync("jwtRefreshToken");

            }

            batch.getBucket(MyJwtUtil.generateRedisJwt(jwt, userId, baseRequestCategoryEnum))
                .setAsync("jwt", Duration.ofMillis(jwtExpireTime));

        });

        return new SignInVO(SecurityConstant.JWT_PREFIX + jwt, expireTs.getTime() - (10 * 60 * 1000), jwtRefreshToken);

    }

}
