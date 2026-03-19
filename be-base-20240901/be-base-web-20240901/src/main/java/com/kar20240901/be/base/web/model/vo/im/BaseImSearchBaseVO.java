package com.kar20240901.be.base.web.model.vo.im;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

@Data
public class BaseImSearchBaseVO {

    @Schema(description = "联系人")
    private List<BaseImSearchBaseFriendVO> friendList;

    @Schema(description = "群聊")
    private List<BaseImSearchBaseGroupVO> groupList;

    @Schema(description = "聊天记录")
    private List<BaseImSearchBaseContentVO> contentList;

}
