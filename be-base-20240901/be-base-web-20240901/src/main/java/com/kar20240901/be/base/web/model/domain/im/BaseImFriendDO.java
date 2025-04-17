package com.kar20240901.be.base.web.model.domain.im;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@TableName(value = "base_im_friend")
@Data
@Schema(description = "主表：好友表")
public class BaseImFriendDO {

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键 id")
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "归属者主键 id")
    private Long belongId;

    @Schema(description = "好友主键 id")
    private Long friendId;

    @Schema(description = "会话主键 id")
    private Long sessionId;

}