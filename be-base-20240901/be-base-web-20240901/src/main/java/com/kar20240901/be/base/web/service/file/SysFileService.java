package com.kar20240901.be.base.web.service.file;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.file.SysFileDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.file.SysFilePageDTO;
import com.kar20240901.be.base.web.model.dto.file.SysFilePageSelfDTO;
import com.kar20240901.be.base.web.model.dto.file.SysFileUploadDTO;
import com.kar20240901.be.base.web.model.vo.base.LongObjectMapVO;
import javax.servlet.http.HttpServletResponse;

public interface SysFileService extends IService<SysFileDO> {

    Long upload(SysFileUploadDTO dto);

    void privateDownload(NotNullId notNullId, HttpServletResponse response);

    String removeByFileIdSet(NotEmptyIdSet notEmptyIdSet, boolean checkBelongFlag);

    LongObjectMapVO<String> getPublicUrl(NotEmptyIdSet notEmptyIdSet);

    Page<SysFileDO> myPage(SysFilePageDTO dto);

    Page<SysFileDO> myPageSelf(SysFilePageSelfDTO dto);

}
