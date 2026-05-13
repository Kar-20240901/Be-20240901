package com.kar20240901.be.base.web.service.im;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionContentDO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSessionContentInsertTxtDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSessionContentUpdateTargetInputFlagDTO;
import com.kar20240901.be.base.web.model.vo.im.BaseImSessionContentRefUserPageVO;
import java.util.Date;

public interface BaseImSessionContentService extends IService<BaseImSessionContentDO> {

    BaseImSessionContentRefUserPageVO insertTxt(BaseImSessionContentInsertTxtDTO dto);

    void addApplyFriendFinishContent(Long sessionId, Long sourceUserId, Date date);

    void addInsertGroupFinishContent(Long sessionId, Long createUserId, Date date);

    void addApplyGroupFinishContent(Long sessionId, Long sourceUserId, Long targetUserId, Date date, String txt);

    String updateTargetInputFlag(BaseImSessionContentUpdateTargetInputFlagDTO dto);

}
