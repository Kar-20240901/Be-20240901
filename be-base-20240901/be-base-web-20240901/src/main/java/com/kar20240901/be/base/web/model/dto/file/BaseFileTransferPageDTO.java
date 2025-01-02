package com.kar20240901.be.base.web.model.dto.file;

import com.kar20240901.be.base.web.model.dto.base.MyPageDTO;
import com.kar20240901.be.base.web.model.enums.file.BaseFileTransferTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseFileTransferPageDTO extends MyPageDTO {

    @Schema(description = "类型：101 上传 201 下载")
    private BaseFileTransferTypeEnum type;

    @Schema(description = "冗余字段：展示用的文件名，默认为：原始文件名（包含文件类型）")
    private String showFileName;

}
