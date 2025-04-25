package com.kar20240901.be.base.web.model.dto.im;

import com.kar20240901.be.base.web.model.dto.base.MyPageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseImApplyGroupPageSelfDTO extends MyPageDTO {

    @Schema(description = "群组名")
    private String groupName;

    @Schema(description = "申请内容")
    private String applyContent;

    @Schema(description = "搜索关键字")
    private String searchKey;

}
