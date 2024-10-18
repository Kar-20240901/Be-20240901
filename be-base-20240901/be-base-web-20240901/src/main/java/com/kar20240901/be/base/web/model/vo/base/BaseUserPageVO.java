package com.kar20240901.be.base.web.model.vo.base;

import com.kar20240901.be.base.web.model.enums.base.BaseRequestCategoryEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import java.util.Set;
import lombok.Data;

@Data
public class BaseUserPageVO {

    @Schema(description = "主键 id")
    private Long id;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像 fileId（文件主键 id），备注：没有时则为 -1")
    private Long avatarFileId;

    @Schema(description = "邮箱，备注：会脱敏")
    private String email;

    @Schema(description = "用户名，会脱敏")
    private String username;

    @Schema(description = "手机号码，会脱敏")
    private String phone;

    @Schema(description = "微信 appId")
    private String wxAppId;

    @Schema(description = "微信 openId")
    private String wxOpenId;

    @Schema(description = "正常/冻结")
    private Boolean enableFlag;

    @Schema(description = "是否有密码")
    private Boolean passwordFlag;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "最近活跃时间")
    private Date lastActiveTime;

    @Schema(description = "ip")
    private String ip;

    @Schema(description = "Ip2RegionUtil.getRegion() 获取到的 ip所处区域")
    private String region;

    @Schema(description = "注册终端")
    private BaseRequestCategoryEnum signUpType;

    @Schema(description = "角色 idSet")
    private Set<Long> roleIdSet;

    @Schema(description = "部门 idSet")
    private Set<Long> deptIdSet;

    @Schema(description = "岗位 idSet")
    private Set<Long> postIdSet;

    @Schema(description = "后台登录")
    private Boolean manageSignInFlag;

}
