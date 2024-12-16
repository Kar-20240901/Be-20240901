package com.kar20240901.be.base.web.model.domain.pay;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.enums.pay.BasePayTradeStatusEnum;
import com.kar20240901.be.base.web.model.interfaces.pay.IBasePayRefStatus;
import com.kar20240901.be.base.web.model.interfaces.pay.IBasePayRefType;
import com.kar20240901.be.base.web.model.interfaces.pay.IBasePayType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_pay")
@Data
@Schema(description = "主表：支付表")
public class BasePayDO extends TempEntity {

    @TableId(type = IdType.INPUT)
    @Schema(description = "主键 id")
    private Long id;

    /**
     * {@link IBasePayType}
     */
    @Schema(description = "支付方式")
    private Integer payType;

    @Schema(description = "支付配置主键 id")
    private Long basePayConfigurationId;

    @Schema(description = "冗余字段：支付配置的租户主键 id")
    private Long basePayConfigurationTenantId;

    @Schema(description = "支付平台，应用 id")
    private String payAppId;

    @Schema(description = "用户主键 id")
    private Long userId;

    @Schema(description = "支付名称")
    private String subject;

    @Schema(description = "商品描述")
    private String body;

    @Schema(description = "支付原始的钱")
    private BigDecimal originalPrice;

    @Schema(description = "冗余字段：实际支付的钱")
    private BigDecimal payPrice;

    @Schema(description = "实际支付的钱的单位，例如：人民币 CNY")
    private String payCurrency;

    @Schema(description = "支付过期时间")
    private Date expireTime;

    @Schema(description = "用户 openId")
    private String openId;

    @Schema(description = "支付状态")
    private BasePayTradeStatusEnum status;

    @Schema(description = "支付平台，支付号")
    private String tradeNo;

    @Schema(description = "支付返回的参数")
    private String payReturnValue;

    /**
     * {@link IBasePayRefType}
     */
    @Schema(description = "关联的类型，建议：修改")
    private Integer refType;

    @Schema(description = "关联的 id，建议：修改")
    private Long refId;

    @Schema(description = "关联的数据，建议：修改")
    private String refData;

    /**
     * {@link IBasePayRefStatus}
     */
    @Schema(description = "关联的状态，建议：修改")
    private Integer refStatus;

    @Schema(description = "app包名，必须是创建登录 api项目时，创建 android客户端 id使用包名，例如：谷歌支付")
    private String packageName;

    @Schema(description = "对应购买商品的商品 id，例如：谷歌支付")
    private String productId;

    @Schema(description = "购买成功后 Purchase对象的 getPurchaseToken()，例如：谷歌支付")
    private String token;

}
