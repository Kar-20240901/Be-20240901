package com.kar20240901.be.base.web.model.dto.bulletin;

import com.kar20240901.be.base.web.model.enums.bulletin.BaseBulletinStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseBulletinPageDTO extends BaseBulletinUserSelfPageDTO {

    @Schema(description = "公告状态：101 草稿 201 公示")
    private BaseBulletinStatusEnum status;

    @Schema(description = "是否是用户自我查询，默认：false")
    private Boolean userSelfFlag;

    @Schema(description = "是否启用")
    private Boolean enableFlag;

}
