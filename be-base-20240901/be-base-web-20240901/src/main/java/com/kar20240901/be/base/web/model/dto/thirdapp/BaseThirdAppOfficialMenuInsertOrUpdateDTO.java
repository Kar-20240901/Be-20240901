package com.kar20240901.be.base.web.model.dto.thirdapp;

import com.kar20240901.be.base.web.model.dto.base.TempInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.enums.thirdapp.BaseThirdAppOfficialMenuButtonTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseThirdAppOfficialMenuInsertOrUpdateDTO extends TempInsertOrUpdateDTO {

    @NotNull
    @Schema(description = "所属的，第三方应用相关配置主键 id")
    private Long thirdAppId;

    @NotBlank
    @Schema(description = "菜单名，备注：一级菜单最多4个汉字，二级菜单最多8个汉字，多出来的部分将会以 ... 代替")
    private String name;

    @NotNull
    @Schema(description = "按钮类型")
    private BaseThirdAppOfficialMenuButtonTypeEnum buttonType;

    @NotBlank
    @Schema(description = "如果是按钮，则表示按钮的 key，如果是链接，则表示是 url，如果是小程序，则表示 appid")
    private String value;

    @Schema(description = "小程序的页面路径")
    private String pagePath;

    @Schema(description = "回复的内容，备注：一般是点击按钮之后，回复的内容")
    private String replyContent;

    @Schema(description = "排序号（值越大越前面，默认为 0）")
    private Integer orderNo;

    @Schema(description = "父节点id（顶级则为0）")
    private Long pid;

    @Schema(description = "是否启用")
    private Boolean enableFlag;

    @Schema(description = "备注")
    private String remark;

}
