package com.kar20240901.be.base.web.model.domain.bulletin;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.enums.bulletin.BaseBulletinStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_bulletin")
@Data
@Schema(description = "主表：公告表")
public class BaseBulletinDO extends TempEntity {

    @Schema(description = "公告内容（富文本）")
    private String content;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "发布时间")
    private Date publishTime;

    @Schema(description = "公告状态：101 草稿 201 公示")
    private BaseBulletinStatusEnum status;

}