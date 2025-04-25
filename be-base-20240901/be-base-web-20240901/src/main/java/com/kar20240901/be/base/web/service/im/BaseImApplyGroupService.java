package com.kar20240901.be.base.web.service.im;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.im.BaseImApplyGroupDO;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyFriendSearchApplyGroupDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupPageGroupDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupPageSelfDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupRejectDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupSendDTO;
import com.kar20240901.be.base.web.model.vo.im.BaseImApplyFriendSearchApplyGroupVO;
import com.kar20240901.be.base.web.model.vo.im.BaseImApplyGroupPageGroupVO;
import com.kar20240901.be.base.web.model.vo.im.BaseImApplyGroupPageSelfVO;

public interface BaseImApplyGroupService extends IService<BaseImApplyGroupDO> {

    Page<BaseImApplyFriendSearchApplyGroupVO> searchApplyGroup(BaseImApplyFriendSearchApplyGroupDTO dto);

    String send(BaseImApplyGroupSendDTO dto);

    Page<BaseImApplyGroupPageSelfVO> myPageSelf(BaseImApplyGroupPageSelfDTO dto);

    Page<BaseImApplyGroupPageGroupVO> myPageGroup(BaseImApplyGroupPageGroupDTO dto);

    String agree(NotNullId dto);

    String reject(BaseImApplyGroupRejectDTO dto);

    String hidden(NotNullId dto);

}
