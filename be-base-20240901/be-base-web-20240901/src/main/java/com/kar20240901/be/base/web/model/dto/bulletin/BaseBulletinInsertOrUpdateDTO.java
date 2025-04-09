package com.kar20240901.be.base.web.model.dto.bulletin;

import com.kar20240901.be.base.web.model.dto.base.TempInsertOrUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseBulletinInsertOrUpdateDTO extends TempInsertOrUpdateDTO {

    @NotBlank
    @Schema(description = "公告内容（富文本）")
    private String content;

    @NotBlank
    @Schema(description = "标题")
    private String title;

    @Future // 限制必须是一个将来的日期
    @NotNull
    @Schema(description = "发布时间")
    private Date publishTime;

    @Schema(description = "是否启用")
    private Boolean enableFlag;

}
