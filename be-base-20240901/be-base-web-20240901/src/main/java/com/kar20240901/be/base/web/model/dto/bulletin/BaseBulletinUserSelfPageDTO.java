package com.kar20240901.be.base.web.model.dto.bulletin;

import com.kar20240901.be.base.web.model.dto.base.MyPageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseBulletinUserSelfPageDTO extends MyPageDTO {

    @Schema(description = "公告类型（字典值）")
    private Integer type;

    @Schema(description = "公告内容（富文本）")
    private String content;

    @Schema(description = "标题")
    private String title;

}
