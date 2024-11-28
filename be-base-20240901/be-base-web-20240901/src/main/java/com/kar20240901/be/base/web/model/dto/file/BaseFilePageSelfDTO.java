package com.kar20240901.be.base.web.model.dto.file;

import com.kar20240901.be.base.web.model.dto.base.MyPageDTO;
import com.kar20240901.be.base.web.model.interfaces.file.IBaseFileStorageType;
import com.kar20240901.be.base.web.model.interfaces.file.IBaseFileUploadType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseFilePageSelfDTO extends MyPageDTO {

    @Schema(description = "父节点id（顶级则为0）")
    private Long pid;

    @Schema(description = "文件原始名（包含文件类型）")
    private String originFileName;

    /**
     * {@link IBaseFileUploadType}
     */
    @Schema(description = "文件上传类型")
    private Integer uploadType;

    /**
     * {@link IBaseFileStorageType}
     */
    @Schema(description = "存放文件的服务器类型")
    private Integer storageType;

    @Schema(description = "是否公开访问")
    private Boolean publicFlag;

    @Schema(description = "是否启用")
    private Boolean enableFlag;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "关联的 id")
    private Long refId;

    @Schema(description = "展示用的文件名，默认为：原始文件名（包含文件类型）")
    private String showFileName;

    @Schema(description = "全局搜索")
    private Boolean globalFlag;

    @Schema(description = "返回上级")
    private Boolean backUpFlag;

}
