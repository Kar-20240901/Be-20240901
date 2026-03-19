package com.kar20240901.be.base.web.service.im;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionContentDO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSessionContentInsertTxtDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSessionContentUpdateTargetInputFlagDTO;

public interface BaseImSessionContentService extends IService<BaseImSessionContentDO> {

    Long insertTxt(BaseImSessionContentInsertTxtDTO dto);

    String updateTargetInputFlag(BaseImSessionContentUpdateTargetInputFlagDTO dto);

}
