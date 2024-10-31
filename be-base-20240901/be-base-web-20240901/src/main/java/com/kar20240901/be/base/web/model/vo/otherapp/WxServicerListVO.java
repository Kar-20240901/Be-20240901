package com.kar20240901.be.base.web.model.vo.otherapp;

import cn.hutool.core.annotation.Alias;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WxServicerListVO extends WxBaseVO {

    @Alias(value = "servicer_list")
    @Schema(description = "客服账号的接待人员列表")
    private List<WxServicerListItemVO> servicerList;

}
