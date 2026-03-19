package com.kar20240901.be.base.web.model.bo.file;

import com.aliyun.oss.model.PartETag;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

@Data
public class BaseFileComposeBO {

    @Schema(description = "阿里云的上传标识集合")
    private List<PartETag> partEtagList;

    @Schema(description = "阿里云上传的id")
    private String uploadId;

    @Schema(description = "对象名集合")
    private List<String> objectNameList;

}
