package com.kar20240901.be.base.web.model.domain.im;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@TableName(value = "base_im_block")
@Data
@Schema(description = "主表：拉黑表")
public class BaseImBlockDO {

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键 id")
    private Long id;

    @Schema(description = "创建人id")
    private Long createId;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "目标用户主键 id")
    private Long userId;

    @Schema(description = "拉黑该用户的来源 id，大多数情况下等于 createId，群聊的时候为：groupId")
    private Long sourceId;

    @Schema(description = "拉黑该用户来源的类型：101 好友 201 群组")
    private Integer sourceType;

}