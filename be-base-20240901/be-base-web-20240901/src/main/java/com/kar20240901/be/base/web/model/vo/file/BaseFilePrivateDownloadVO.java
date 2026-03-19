package com.kar20240901.be.base.web.model.vo.file;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.InputStream;
import lombok.Data;

@Data
public class BaseFilePrivateDownloadVO {

    @Schema(description = "流")
    private InputStream inputStream;

    @Schema(description = "Content-Range的header值")
    private String contentRangeHeader;

    @Schema(description = "文件名")
    private String fileName;

}
