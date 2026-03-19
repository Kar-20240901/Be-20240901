package com.kar20240901.be.base.web.model.domain.im;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@TableName(value = "base_im_search_history")
@Data
@Schema(description = "主表：搜索历史表")
public class BaseImSearchHistoryDO {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(description = "归属者主键 id")
    private Long belongId;

    @Schema(description = "搜索的内容")
    private String searchHistory;

}