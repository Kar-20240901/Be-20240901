package com.kar20240901.be.base.web.model.domain.wallet;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.enums.wallet.BaseOpenBankNameEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_user_bank_card")
@Data
@Schema(description = "子表：用户银行卡表，主表：用户表")
public class BaseUserBankCardDO extends TempEntity {

    @TableId(type = IdType.INPUT)
    @Schema(description = "用户主键 id")
    private Long id;

    @Schema(description = "卡号")
    private String bankCardNo;

    @Schema(description = "开户行")
    private BaseOpenBankNameEnum openBankName;

    @Schema(description = "支行")
    private String branchBankName;

    @Schema(description = "收款人姓名")
    private String payeeName;

}
