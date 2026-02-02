package com.kar20240901.be.base.web.service.im;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.im.BaseImGroupDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.ScrollListDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupChangeBelongIdDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupPageDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupRemoveUserDTO;
import com.kar20240901.be.base.web.model.vo.im.BaseImGroupPageVO;
import java.util.List;

public interface BaseImGroupService extends IService<BaseImGroupDO> {

    String insertOrUpdate(BaseImGroupInsertOrUpdateDTO dto);

    Page<BaseImGroupPageVO> myPage(BaseImGroupPageDTO dto);

    List<BaseImGroupPageVO> scroll(ScrollListDTO dto);

    String removeUser(BaseImGroupRemoveUserDTO dto);

    String changeBelongId(BaseImGroupChangeBelongIdDTO dto);

    String deleteByIdSet(NotEmptyIdSet dto);

}
