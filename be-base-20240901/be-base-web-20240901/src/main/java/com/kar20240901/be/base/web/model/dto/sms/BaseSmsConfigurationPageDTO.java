package com.kar20240901.be.base.web.model.dto.sms;

import com.kar20240901.be.base.web.model.dto.base.MyPageDTO;
import com.kar20240901.be.base.web.model.interfaces.sms.IBaseSmsType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseSmsConfigurationPageDTO extends MyPageDTO {

    @Schema(description = "短信名")
    private String name;

    /**
     * {@link IBaseSmsType}
     */
    @Schema(description = "短信类型：101 阿里 201 腾讯")
    private Integer type;

    @Schema(description = "是否是默认短信发送，备注：只会有一个默认短信发送")
    private Boolean defaultFlag;

    @Schema(description = "是否启用")
    private Boolean enableFlag;

    @Schema(description = "备注")
    private String remark;

}
