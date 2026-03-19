package com.kar20240901.be.base.web.model.dto.base;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "滚动列表查询")
@NoArgsConstructor
@AllArgsConstructor
public class ScrollListDTO {

    @Schema(description = "主键 id，如果为 null，则根据 backwardFlag，来查询最大 id或者最小 id，注意：不会查询该 id的数据")
    private Long id;

    @Schema(description = "本次查询的长度，默认：20")
    private Long pageSize;

    @Schema(description = "是否向后查询，默认：false 根据 id，往前查询 true 根据 id，往后查询")
    private Boolean backwardFlag;

    @Schema(description = "搜索内容")
    private String searchKey;

    @Schema(description = "关联其他主键 id")
    private Long refId;

    @Schema(description = "是否包含当前主键 id，默认：false")
    private Boolean containsCurrentIdFlag;

    @Schema(
        description = "是否多查询一些数据，backwardFlag 为 true时，往前多查询几条数据，为 false时，往后多查询几条数据，如果不足 pageSize，会补齐并且会额外多查询几条数据")
    private Boolean queryMoreFlag;

    @Schema(description = "关联的 id集合")
    private Set<Long> refIdSet;

    @Schema(description = "布尔值1")
    private Boolean boolean1;

}
