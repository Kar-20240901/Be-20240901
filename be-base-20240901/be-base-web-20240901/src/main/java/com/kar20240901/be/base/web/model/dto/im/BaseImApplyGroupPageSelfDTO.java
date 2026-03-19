package com.kar20240901.be.base.web.model.dto.im;

import com.kar20240901.be.base.web.model.dto.base.MyPageDTO;
import com.kar20240901.be.base.web.model.enums.im.BaseImApplyStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseImApplyGroupPageSelfDTO extends MyPageDTO {

    @Schema(description = "搜索关键字")
    private String searchKey;

    @Schema(description = "后端查询用", hidden = true)
    private BaseImApplyStatusEnum statusTemp;

}
