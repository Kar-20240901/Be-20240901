package com.kar20240901.be.base.web.model.domain.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.enums.base.BaseRequestCategoryEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_request")
@Data
@Schema(description = "主表：请求表")
public class BaseRequestDO extends TempEntity {

    @TableId(type = IdType.INPUT)
    @Schema(description = "主键id")
    private Long id;

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

    @TableField(exist = false, value = "COUNT(1) AS count")
    @Schema(description = "总数量")
    private Long count;

    @TableField(exist = false, value = "(SUM(a.cost_ms) DIV COUNT(1)) AS avgMs")
    @Schema(description = "平均耗时（毫秒）")
    private Integer avgMs;

}
