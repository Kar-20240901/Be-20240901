package com.kar20240901.be.base.web.model.vo.thirdapp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class WxBaseVO {

    @Schema(description = "错误码：0 请求成功")
    private Integer errcode;

    @Schema(description = "错误信息")
    private String errmsg;

}
