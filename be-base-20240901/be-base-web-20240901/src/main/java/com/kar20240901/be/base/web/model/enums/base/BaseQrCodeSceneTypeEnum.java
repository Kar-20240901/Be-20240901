package com.kar20240901.be.base.web.model.enums.base;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kar20240901.be.base.web.model.bo.base.BaseQrCodeSceneBindBO;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.domain.base.TempUserDO;
import com.kar20240901.be.base.web.model.interfaces.base.IBaseQrCodeSceneType;
import com.kar20240901.be.base.web.util.base.VoidFunc3;
import java.time.Duration;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

/**
 * 二维码，scene的类型
 */
@AllArgsConstructor
@Getter
public enum BaseQrCodeSceneTypeEnum implements IBaseQrCodeSceneType {

    // 微信绑定
    WX_BIND("WX_BIND", TempConstant.MINUTE_3_EXPIRE_TIME / 1000, (qrCodeSceneValue, redissonClient, tempUserDO) -> {

        RBucket<BaseQrCodeSceneBindBO> bucket =
            redissonClient.getBucket(BaseRedisKeyEnum.PRE_BASE_WX_QR_CODE_BIND.name() + qrCodeSceneValue);

        BaseQrCodeSceneBindBO baseQrCodeSceneBindBO = new BaseQrCodeSceneBindBO();

        baseQrCodeSceneBindBO.setUserId(tempUserDO.getId());

        bucket.set(baseQrCodeSceneBindBO, Duration.ofMillis(TempConstant.MINUTE_3_EXPIRE_TIME));

    }, false),

    ;

    @EnumValue
    @JsonValue
    private final String code; // 编码

    private final int expireSecond; // 二维码过期时间，单位：秒，小于等于 0，表示永久

    private final VoidFunc3<String, RedissonClient, TempUserDO> qrSceneValueConsumer; // 处理：二维码扫码之后的值

    private final Boolean autoSignUpFlag; // 是否自动注册

    // 自动注册的 map
    public static final Map<String, IBaseQrCodeSceneType> AUTO_SIGN_UP_MAP =
        MapUtil.newConcurrentHashMap(BaseQrCodeSceneTypeEnum.values().length);

    // 不自动注册的 map
    public static final Map<String, IBaseQrCodeSceneType> NOT_AUTO_SIGN_UP_MAP =
        MapUtil.newConcurrentHashMap(BaseQrCodeSceneTypeEnum.values().length);

    static {

        for (BaseQrCodeSceneTypeEnum item : BaseQrCodeSceneTypeEnum.values()) {

            if (BooleanUtil.isTrue(item.getAutoSignUpFlag())) {

                AUTO_SIGN_UP_MAP.put(item.getSceneStr(), item);

            } else {

                NOT_AUTO_SIGN_UP_MAP.put(item.getSceneStr(), item);

            }

        }

    }

}
