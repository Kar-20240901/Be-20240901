package com.kar20240901.be.base.web.model.domain.bulletin;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@TableName(value = "base_bulletin_read_time_ref_user")
@Data
@Schema(description = "关联表：公告表，用户表")
public class BaseBulletinReadTimeRefUserDO {

    @TableId
    @Schema(description = "用户主键id")
    private Long userId;

    @Schema(description = "用户最近查看公告的时间，目的：统计公告数量时，根据这个时间和公告发布时间来计算")
    private Date bulletinReadTime;

}