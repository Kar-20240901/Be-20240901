package com.kar20240901.be.base.web.model.domain.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BaseRequestInfoDO {

    @TableId(type = IdType.INPUT)
    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "失败信息")
    private String errorMsg;

    @Schema(description = "请求的参数")
    private String requestParam;

    @Schema(description = "请求返回的值")
    private String responseValue;

}
