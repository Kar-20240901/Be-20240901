package com.kar20240901.be.base.web.model.domain.live;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@TableName(value = "base_live_room_data")
@Data
@Schema(description = "子表：实时房间数据表：实时房间表")
public class BaseLiveRoomDataDO {

    @Schema(description = "主键 id")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "实时房间主键 id")
    private Long roomId;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "创建时间的时间戳，UTC+8")
    private Long createTs;

    @Schema(description = "数据")
    private byte[] data;

    @Schema(description = "创建者用户主键 id")
    private Long createId;

    @Schema(description = "媒体类型")
    private String mediaType;

    @Schema(description = "是否是第一个 blob")
    private Boolean firstBlobFlag;

}