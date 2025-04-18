package com.kar20240901.be.base.web.model.domain.im;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@TableName(value = "base_im_apply_friend_extra")
@Data
@Schema(description = "表：好友申请扩展表，主表：好友申请表")
public class BaseImApplyFriendExtraDO {

    @TableId(type = IdType.INPUT)
    @Schema(description = "好友申请主键 id")
    private Long applyFriendId;

    @Schema(description = "用户主键 id")
    private Long userId;

    @Schema(description = "是否隐藏")
    private Integer hiddenFlag;

}