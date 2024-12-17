package com.kar20240901.be.base.web.service.wallet;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.wallet.BaseUserBankCardDO;
import com.kar20240901.be.base.web.model.dto.base.NotNullLong;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserBankCardInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserBankCardInsertOrUpdateUserSelfDTO;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserBankCardPageDTO;
import com.kar20240901.be.base.web.model.vo.base.DictStringVO;
import org.jetbrains.annotations.Nullable;

public interface BaseUserBankCardService extends IService<BaseUserBankCardDO> {

    String insertOrUpdate(BaseUserBankCardInsertOrUpdateDTO dto);

    String insertOrUpdateUserSelf(BaseUserBankCardInsertOrUpdateUserSelfDTO dto);

    Page<BaseUserBankCardDO> myPage(BaseUserBankCardPageDTO dto);

    Page<DictStringVO> openBankNameDictList();

    BaseUserBankCardDO infoById(NotNullLong notNullLong);

    BaseUserBankCardDO infoByIdUserSelf();

    // ================================ 分割线

    String doInsertOrUpdate(BaseUserBankCardInsertOrUpdateUserSelfDTO dto, @Nullable Long userId);

    Page<BaseUserBankCardDO> doMyPage(BaseUserBankCardPageDTO dto);

}
