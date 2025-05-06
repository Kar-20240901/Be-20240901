package com.kar20240901.be.base.web.model.domain.im;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@TableName(value = "base_live_room")
@Data
@Schema(description = "主表：实时房间表")
public class BaseLiveRoomDO {

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "房间主键 id")
    private Long id;

    @Schema(description = "归属用户主键 id")
    private Long belongId;

    @Schema(description = "实时房间名")
    private String name;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Date createTime;

    @TableField(exist = false)
    @Schema(description = "归属用户昵称")
    private Date belongNickname;

}