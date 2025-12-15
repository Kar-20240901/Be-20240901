package com.kar20240901.be.base.web.model.bo.file;

import com.kar20240901.be.base.web.model.domain.file.BaseFileDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseFilePrivateDownloadBO {

    @Schema(description = "文件主键id")
    private Long fileId;

    @Schema(description = "偏移量，从 0开始，备注：end有值，begin不一定有值，表示：只请求最后的 length个字节")
    private Long pre;

    @Schema(description = "长度，从 0开始，备注：offset有值，length不一定有值，表示：从 offset到最后全部")
    private Long suf;

    @Schema(description = "文件对象")
    private BaseFileDO baseFileDO;

    @Schema(description = "是否返回缩略图，默认：true")
    private Boolean thumbnailFlag;

    @Schema(description = "缩略图宽度")
    private Integer thumbnailWidth;

    @Schema(description = "缩略图高度")
    private Integer thumbnailHeight;

    @Schema(description = "缩略图质量")
    private Double thumbnailQuality;

}
