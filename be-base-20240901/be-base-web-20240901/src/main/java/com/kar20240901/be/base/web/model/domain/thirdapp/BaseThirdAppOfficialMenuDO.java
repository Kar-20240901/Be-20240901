package com.kar20240901.be.base.web.model.domain.thirdapp;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240901.be.base.web.model.domain.base.TempEntityTree;
import com.kar20240901.be.base.web.model.enums.thirdapp.BaseThirdAppOfficialMenuButtonTypeEnum;
import com.kar20240901.be.base.web.model.interfaces.thirdapp.IBaseThirdAppOfficialMenuType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_third_app_official_menu")
@Data
@Schema(description = "子表：公众号菜单配置表，主表：三方应用配置表（base_third_app）")
public class BaseThirdAppOfficialMenuDO extends TempEntityTree<BaseThirdAppOfficialMenuDO> {

    @Schema(description = "所属的，三方应用配置主键 id")
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

    @Schema(description = "如果是按钮，则表示按钮的 key，如果是链接，则表示是 url，如果是小程序，则表示 appid")
    private String value;

    @Schema(description = "小程序的页面路径")
    private String pagePath;

    @Schema(description = "回复的内容，备注：一般是点击按钮之后，回复的内容")
    private String replyContent;

}
