package com.kar20240901.be.base.web.model.vo.file;

import com.aliyun.oss.model.PartETag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BaseFileUploadChunkVO {

    @Schema(description = "阿里分片上传返回值")
    private PartETag partEtag;

}
