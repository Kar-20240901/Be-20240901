package com.kar20240901.be.base.web.model.dto.im;

import com.kar20240901.be.base.web.model.dto.base.MyPageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseImFriendPageDTO extends MyPageDTO {

    @Schema(description = "好友用户主键 id")
    private Long imFriendId;

    @Schema(description = "搜索关键字")
    private String searchKey;

    @Schema(description = "是否向后查询，默认：false 根据 id，往前查询 true 根据 id，往后查询", hidden = true)
    private Boolean backwardFlag;

    @Schema(description = "是否查询拉黑情况")
    private Boolean queryBlockFlag;

    @Schema(description = "是否是好友管理查询")
    private Boolean manageQueryFlag;

}
