package com.kar20240901.be.base.web.model.vo.thirdapp;

import cn.hutool.core.annotation.Alias;
import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WxSyncMsgVO extends WxBaseVO {

    @Alias(value = "next_cursor")
    @Schema(description = "下次调用带上该值，则从当前的位置继续往后拉，以实现增量拉取。")
    private String nextCursor;

    @Alias(value = "msg_list")
    @Schema(description = "消息列表")
    private List<JSONObject> msgList;

}
