package com.kar20240901.be.base.web.model.interfaces.base;

import com.kar20240901.be.base.web.model.domain.base.TempUserDO;
import com.kar20240901.be.base.web.util.base.SeparatorUtil;
import com.kar20240901.be.base.web.util.base.VoidFunc3;
import org.redisson.api.RedissonClient;

public interface IBaseQrCodeSceneType {

    String getCode();

    int getExpireSecond(); // 二维码过期时间，单位：秒，小于等于 0，表示永久

    String SEPARATOR = SeparatorUtil.POUND_SIGN_SEPARATOR;

    default String getSceneStr() {

        return SEPARATOR + getCode();

    }

    VoidFunc3<String, RedissonClient, TempUserDO> getQrSceneValueConsumer(); // 处理：二维码扫码之后的值

    Boolean getAutoSignUpFlag(); // 是否自动注册

}
