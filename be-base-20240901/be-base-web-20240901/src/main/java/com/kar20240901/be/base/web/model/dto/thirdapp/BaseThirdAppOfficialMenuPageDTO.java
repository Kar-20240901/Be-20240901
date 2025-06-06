package com.kar20240901.be.base.web.model.dto.thirdapp;

import com.kar20240901.be.base.web.model.dto.base.MyPageDTO;
import com.kar20240901.be.base.web.model.enums.thirdapp.BaseThirdAppOfficialMenuButtonTypeEnum;
import com.kar20240901.be.base.web.model.interfaces.thirdapp.IBaseThirdAppOfficialMenuType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseThirdAppOfficialMenuPageDTO extends MyPageDTO {

    @Schema(description = "所属的，第三方应用相关配置主键 id")
    private Long thirdAppId;

    @Schema(description = "菜单名，备注：一级菜单最多4个汉字，二级菜单最多8个汉字，多出来的部分将会以 ... 代替")
    private String name;

    /**
     * {@link IBaseThirdAppOfficialMenuType}
     */
    @Schema(description = "类型")
    private Integer type;

    @Schema(description = "按钮类型")
    private BaseThirdAppOfficialMenuButtonTypeEnum buttonType;

    @Schema(description = "如果是按钮，则表示按钮的 key，如果是链接，则表示是 url")
    private String value;

    @Schema(description = "回复的内容，备注：一般是点击按钮之后，回复的内容")
    private String replyContent;

    @Schema(description = "是否启用")
    private Boolean enableFlag;

    @Schema(description = "备注")
    private String remark;

}
