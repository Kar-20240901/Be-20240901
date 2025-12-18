package com.kar20240901.be.base.web.model.dto.file;

import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseFilePrivateDownloadDTO extends NotNullId {

    @Schema(description = "是否返回缩略图，默认：true")
    private Boolean thumbnailFlag;

    @Schema(description = "缩略图宽度")
    private Integer thumbnailWidth;

    @Schema(description = "缩略图高度")
    private Integer thumbnailHeight;

    @Schema(description = "缩略图质量")
    private Double thumbnailQuality;

}
