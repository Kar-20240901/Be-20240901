package com.kar20240901.be.base.web.model.domain.sql;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@TableName(value = "base_sql_slow")
@Data
@Schema(description = "主表：慢sql日志表")
public class BaseSqlSlowDO {

    @Schema(description = "这里是自定义的主键 id")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人id")
    private Long createId;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "sqlId，如：com.kar20240901.be.base.web.mapper.base.BaseUserMapper.insert")
    private String name;

    @Schema(description = "sql语句类型：SELECT、DELETE、INSERT、UPDATE")
    private String type;

    @Schema(description = "耗时（毫秒）")
    private Long costMs;

    @Schema(description = "sql内容")
    private String sqlContent;

}