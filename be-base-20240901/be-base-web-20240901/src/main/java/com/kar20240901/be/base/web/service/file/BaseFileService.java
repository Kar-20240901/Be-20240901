package com.kar20240901.be.base.web.service.file;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.file.BaseFileDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.file.BaseFileCopySelfDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileCreateFolderSelfSelfDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileMoveSelfDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFilePageDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFilePageSelfDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileUpdateSelfDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileUploadDTO;
import com.kar20240901.be.base.web.model.vo.base.LongObjectMapVO;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

public interface BaseFileService extends IService<BaseFileDO> {

    Long upload(BaseFileUploadDTO dto);

    void privateDownload(NotNullId notNullId, HttpServletResponse response);

    String removeByFileIdSet(NotEmptyIdSet notEmptyIdSet, boolean checkBelongFlag);

    LongObjectMapVO<String> getPublicUrl(NotEmptyIdSet notEmptyIdSet);

    Page<BaseFileDO> myPage(BaseFilePageDTO dto);

    Page<BaseFileDO> myPageSelf(BaseFilePageSelfDTO dto);

    List<BaseFileDO> tree(BaseFilePageDTO dto);

    List<BaseFileDO> treeSelf(BaseFilePageSelfDTO dto);

    String createFolderSelf(BaseFileCreateFolderSelfSelfDTO dto);

    String updateSelf(BaseFileUpdateSelfDTO dto);

    String moveSelf(BaseFileMoveSelfDTO dto);

    String copySelf(BaseFileCopySelfDTO dto);

}
