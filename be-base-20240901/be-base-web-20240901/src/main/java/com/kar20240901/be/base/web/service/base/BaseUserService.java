package com.kar20240901.be.base.web.service.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.base.TempUserDO;
import com.kar20240901.be.base.web.model.dto.base.BaseUserInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.base.BaseUserPageDTO;
import com.kar20240901.be.base.web.model.dto.base.BaseUserUpdatePasswordDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.vo.base.BaseUserPageVO;
import com.kar20240901.be.base.web.model.vo.base.DictVO;
import com.kar20240901.be.base.web.model.vo.base.TempUserInfoByIdVO;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface BaseUserService extends IService<TempUserDO> {

    Page<BaseUserPageVO> myPage(BaseUserPageDTO dto);

    Page<DictVO> dictList();

    String insertOrUpdate(BaseUserInsertOrUpdateDTO dto);

    TempUserInfoByIdVO infoById(NotNullId notNullId);

    Boolean manageSignInFlag();

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

    String resetAvatar(NotEmptyIdSet notEmptyIdSet);

    String updatePassword(BaseUserUpdatePasswordDTO dto);

    String thaw(NotEmptyIdSet notEmptyIdSet);

    String freeze(NotEmptyIdSet notEmptyIdSet);

    String signOutByIdSet(NotEmptyIdSet notEmptyIdSet);

    String signOutAll();

    String insertBatchByExcel(MultipartFile dto);

    void insertBatchByExcelDownloadTemplate(HttpServletResponse response);

}
