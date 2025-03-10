package com.kar20240901.be.base.web.model.dto.base;

import com.kar20240901.be.base.web.model.annotation.base.NotCheckBlankPattern;
import com.kar20240901.be.base.web.model.constant.base.TempRegexConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseUserInsertOrUpdateDTO extends TempInsertOrUpdateDTO {

    @NotCheckBlankPattern(regexp = TempRegexConstant.USER_NAME_REGEXP)
    @Schema(description = "用户名")
    private String username;

    @NotCheckBlankPattern(regexp = TempRegexConstant.EMAIL)
    @Schema(description = "邮箱")
    private String email;

    @NotCheckBlankPattern(regexp = TempRegexConstant.PHONE)
    @Schema(description = "手机号码")
    private String phone;

    @Schema(description = "微信 appId")
    private String wxAppId;

    @Schema(description = "微信 openId")
    private String wxOpenId;

    @Schema(description = "微信 unionId")
    private String wxUnionId;

    @Schema(description = "前端加密之后的密码")
    private String password;

    @Schema(description = "前端加密之后的原始密码")
    private String originPassword;

    @Pattern(regexp = TempRegexConstant.NICK_NAME_REGEXP)
    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "个人简介")
    private String bio;

    @Schema(description = "正常/冻结")
    private Boolean enableFlag;

    @Schema(description = "角色 idSet")
    private Set<Long> roleIdSet;

    @Schema(description = "权限 idSet")
    private Set<Long> authIdSet;

    @Schema(description = "部门 idSet")
    private Set<Long> deptIdSet;

    @Schema(description = "岗位 idSet")
    private Set<Long> postIdSet;

    @Schema(description = "是否允许登录：后台管理系统")
    private Boolean manageSignInFlag;

}
