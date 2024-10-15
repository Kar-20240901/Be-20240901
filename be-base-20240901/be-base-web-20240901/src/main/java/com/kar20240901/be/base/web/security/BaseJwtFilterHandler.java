package com.kar20240901.be.base.web.security;

import cn.hutool.jwt.JWT;
import com.kar20240901.be.base.web.model.domain.kafka.TempKafkaUserInfoDO;
import com.kar20240901.be.base.web.model.interfaces.IJwtFilterHandler;
import com.kar20240901.be.base.web.util.TempKafkaUtil;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class BaseJwtFilterHandler implements IJwtFilterHandler {

    @Override
    public void handleJwt(Long userId, String ip, JWT jwt) {

        TempKafkaUserInfoDO tempKafkaUserInfoDO = new TempKafkaUserInfoDO();

        tempKafkaUserInfoDO.setId(userId);
        tempKafkaUserInfoDO.setLastActiveTime(new Date());
        tempKafkaUserInfoDO.setLastIp(ip);

        TempKafkaUtil.sendTempUpdateUserInfoTopic(tempKafkaUserInfoDO);

    }

}
