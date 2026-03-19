package com.kar20240901.be.base.web.model.dto.im;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class BaseImApplyGroupAgreeDTO {

    @NotEmpty
    @Schema(description = "群聊信息集合")
    private List<BaseImApplyGroupItemDTO> list;

}
