package com.kar20240901.be.base.web.model.bo.base;

import cn.hutool.json.JSONObject;
import com.kar20240901.be.base.web.model.interfaces.base.IBizCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BaseAuthorizationBO {

    @Schema(description = "错误信息")
    private IBizCode iBizCode;

    @Schema(description = "携带的额外信息")
    private JSONObject claimsJson;

    @Schema(description = "用户主键 id")
    private Long userId;

}
