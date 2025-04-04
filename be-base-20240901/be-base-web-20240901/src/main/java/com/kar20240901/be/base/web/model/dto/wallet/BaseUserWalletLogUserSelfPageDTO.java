package com.kar20240901.be.base.web.model.dto.wallet;

import com.kar20240901.be.base.web.model.dto.base.MyPageDTO;
import com.kar20240901.be.base.web.model.interfaces.wallet.IBaseUserWalletLogType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseUserWalletLogUserSelfPageDTO extends MyPageDTO {

    @Schema(description = "记录名")
    private String name;

    @Schema(description = "起始时间：创建时间")
    private Date ctBeginTime;

    @Schema(description = "结束时间：创建时间")
    private Date ctEndTime;

    @Schema(description = "备注")
    private String remark;

    /**
     * {@link IBaseUserWalletLogType}
     */
    @Schema(description = "记录类型：1开头 增加 2开头 减少")
    private Integer type;

}
