package com.kar20240901.be.base.web.model.vo.file;

import com.kar20240901.be.base.web.model.domain.file.BaseFileDO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

@Data
public class BaseFilePageSelfVO {

    @Schema(description = "数据")
    private List<BaseFileDO> records;

    @Schema(description = "父id组合集合，例如：[0,1,2]，备注：包含本级，并且包含顶级：0，并且和 pathList一一对应")
    private List<Long> pidList;

    @Schema(
        description = "路径字符串集合，例如：/根目录/测试1/测试1-1，备注：包含本级，并且包含顶级：根目录，并且和 pidList一一对应")
    private List<String> pathList;

    @Schema(description = "返回上一级的 pid")
    private Long backUpPid;

}
