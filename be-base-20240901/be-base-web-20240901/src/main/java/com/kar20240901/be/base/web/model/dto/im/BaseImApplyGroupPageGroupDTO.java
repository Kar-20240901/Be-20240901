package com.kar20240901.be.base.web.model.dto.im;

import com.kar20240901.be.base.web.model.dto.base.MyPageDTO;
import com.kar20240901.be.base.web.model.enums.im.BaseImApplyStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseImApplyGroupPageGroupDTO extends MyPageDTO {

    @Schema(description = "群组主键 id")
    private Long groupId;

    @Schema(description = "搜索关键字")
    private String searchKey;

    @Schema(description = "后端查询用", hidden = true)
    private BaseImApplyStatusEnum statusTemp;

    @Schema(description = "后端查询用", hidden = true)
    private Long currentUserId;

    @Schema(description = "后端查询用", hidden = true)
    private Integer baseImTypeEnum;

}
