package com.kar20240901.be.base.web.model.domain.im;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@TableName(value = "base_live_room_user")
@Data
@Schema(description = "子表：实时房间用户表，主表：实时房间表")
public class BaseLiveRoomUserDO {

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键 id")
    private Long id;

    @Schema(description = "实时房间主键 id")
    private Long roomId;

    @Schema(description = "用户主键 id")
    private Long userId;

    @Schema(description = "套接字关联用户主键 id")
    private Long socketRefUser;

}