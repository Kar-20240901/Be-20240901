package com.kar20240901.be.base.web.model.domain.wallet;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.interfaces.wallet.IBaseUserWalletLogType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_user_wallet_log")
@Data
@Schema(description = "主表：用户钱包操作日志表")
public class BaseUserWalletLogDO extends TempEntity {

    @Schema(description = "用户主键 id")
    private Long userId;

    @Schema(description = "记录名")
    private String name;

    /**
     * {@link IBaseUserWalletLogType}
     */
    @Schema(description = "记录类型：1开头 增加 2开头 减少")
    private Integer type;

    @Schema(description = "关联的 id")
    private Long refId;

    @Schema(description = "关联的数据")
    private String refData;

    @Schema(description = "可提现的钱，前")
    private BigDecimal withdrawableMoneyPre;

    @Schema(description = "可提现的钱，变")
    private BigDecimal withdrawableMoneyChange;

    @Schema(description = "可提现的钱，后")
    private BigDecimal withdrawableMoneySuf;

    @Schema(description = "可提现的钱，预使用，前")
    private BigDecimal withdrawablePreUseMoneyPre;

    @Schema(description = "可提现的钱，预使用，变")
    private BigDecimal withdrawablePreUseMoneyChange;

    @Schema(description = "可提现的钱，预使用，后")
    private BigDecimal withdrawablePreUseMoneySuf;

}
