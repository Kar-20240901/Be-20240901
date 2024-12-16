package com.kar20240901.be.base.web.model.interfaces.pay;

import com.kar20240901.be.base.web.model.dto.pay.BasePayConfigurationInsertOrUpdateDTO;
import java.util.function.Consumer;

public interface IBasePayType {

    int getCode(); // 建议从：10001（包含）开始

    Consumer<BasePayConfigurationInsertOrUpdateDTO> getCheckBasePayConfigurationInsertOrUpdateDtoConsumer();

}
