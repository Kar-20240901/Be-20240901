package com.kar20240901.be.base.web.service.im;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionContentRefUserDO;
import com.kar20240901.be.base.web.model.dto.base.ScrollListDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSessionContentRefUserPageDTO;
import com.kar20240901.be.base.web.model.vo.im.BaseImSessionContentRefUserPageVO;
import java.util.List;

public interface BaseImSessionContentRefUserService extends IService<BaseImSessionContentRefUserDO> {

    Page<BaseImSessionContentRefUserPageVO> myPage(BaseImSessionContentRefUserPageDTO dto);

    List<BaseImSessionContentRefUserPageVO> scroll(ScrollListDTO dto);

}
