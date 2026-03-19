package com.kar20240901.be.base.web.model.vo.im;

import com.kar20240901.be.base.web.model.domain.im.BaseImGroupDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseImGroupInfoByIdVO extends BaseImGroupDO {

    @Schema(description = "是否是群主")
    private Boolean belongFlag;

    @Schema(description = "是否是管理员")
    private Boolean manageFlag;

    @Schema(description = "群组头像地址")
    private String avatarUrl;

}
