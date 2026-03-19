package com.kar20240901.be.base.web.model.interfaces.wallet;

public interface IBaseUserWalletLogType {

    int getCode(); // 建议从：10001和20001（包含）开始：1开头 增加 2开头 减少

    String getName();

}
