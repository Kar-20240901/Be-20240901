package com.kar20240901.be.base.web.model.domain.live;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@TableName(value = "base_live_room_replay")
@Data
@Schema(description = "子表：实时房间回放表：实时房间表")
public class BaseLiveRoomReplayDO {

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键 id")
    private Long id;

    @Schema(description = "实时房间主键 id")
    private Long roomId;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "关联的文件主键 id")
    private Long fileId;

    @Schema(description = "时间，单位：毫秒")
    private Integer ms;

    @Schema(description = "归属者用户主键 id")
    private Long belongId;

}