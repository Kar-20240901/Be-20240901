package com.kar20240901.be.base.web.model.domain.wallet;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_user_wallet")
@Data
@Schema(description = "子表：用户钱包表，主表：用户表")
public class BaseUserWalletDO extends TempEntity {

    @TableId(type = IdType.INPUT)
    @Schema(description = "用户主键 id")
    private Long id;

    @Schema(description = "可提现的钱")
    private BigDecimal withdrawableMoney;

    @Schema(description = "可提现的钱：预使用，例如用于：用户提现时，需要扣除租户的可提现的钱时")
    private BigDecimal withdrawablePreUseMoney;

    @TableField(exist = false)
    @Schema(description = "总金额")
    private BigDecimal totalMoney;

    public BigDecimal getTotalMoney() {
        return withdrawableMoney;
    }

    @TableField(exist = false)
    @Schema(description = "实际可提现的钱")
    private BigDecimal withdrawableRealMoney;

    public BigDecimal getWithdrawableRealMoney() {
        return withdrawableMoney.subtract(withdrawablePreUseMoney);
    }

}
