package com.kar20240901.be.base.web.model.dto.request;

import com.kar20240901.be.base.web.model.dto.base.MyPageDTO;
import com.kar20240901.be.base.web.model.enums.base.BaseRequestCategoryEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseRequestPageDTO extends MyPageDTO {

    @Schema(description = "请求的uri")
    private String uri;

    @Schema(description = "耗时开始（毫秒）")
    private Long beginCostMs;

    @Schema(description = "耗时结束（毫秒）")
    private Long endCostMs;

    @Schema(description = "接口名（备用）")
    private String name;

    @Schema(description = "起始时间：创建时间")
    private Date ctBeginTime;

    @Schema(description = "结束时间：创建时间")
    private Date ctEndTime;

    @Schema(description = "创建人id")
    private Long createId;

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

}
