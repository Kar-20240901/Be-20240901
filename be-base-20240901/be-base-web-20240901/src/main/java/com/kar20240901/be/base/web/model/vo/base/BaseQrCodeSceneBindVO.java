package com.kar20240901.be.base.web.model.vo.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BaseQrCodeSceneBindVO {

    @Schema(description = "是否：已经扫码")
    private Boolean sceneFlag;

    @Schema(description = "错误信息")
    private String errorMsg;

}
