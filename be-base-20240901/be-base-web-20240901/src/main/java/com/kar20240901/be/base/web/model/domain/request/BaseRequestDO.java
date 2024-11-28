package com.kar20240901.be.base.web.model.domain.request;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240901.be.base.web.model.enums.base.BaseRequestCategoryEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@TableName(value = "base_request")
@Data
@Schema(description = "主表：请求表")
public class BaseRequestDO {

    @TableId(type = IdType.INPUT)
    @Schema(description = "主键id")
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人id")
    private Long createId;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "请求的 uri")
    private String uri;

    @Schema(description = "耗时（毫秒）")
    private Long costMs;

    @Schema(description = "接口名（备用）")
    private String name;

    @Schema(description = "请求类别")
    private BaseRequestCategoryEnum category;

    @Schema(description = "ip")
    private String ip;

    @Schema(description = "Ip2RegionUtil.getRegion() 获取到的 ip所处区域")
    private String region;

    @Schema(description = "请求是否成功")
    private Boolean successFlag;

    @Schema(description = "请求类型")
    private String type;

    @TableField(value = "COUNT(1)", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    @Schema(description = "总数量")
    private Long count;

    @TableField(value = "(SUM(cost_ms) DIV COUNT(1))", insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.NEVER)
    @Schema(description = "平均耗时（毫秒）")
    private Integer avgMs;

}
