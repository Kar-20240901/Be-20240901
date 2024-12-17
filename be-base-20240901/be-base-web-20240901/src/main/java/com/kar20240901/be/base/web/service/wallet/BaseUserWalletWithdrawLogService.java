package com.kar20240901.be.base.web.service.wallet;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.wallet.BaseUserWalletWithdrawLogDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.base.NotNullIdAndStringValue;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserWalletWithdrawLogInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserWalletWithdrawLogInsertOrUpdateUserSelfDTO;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserWalletWithdrawLogPageDTO;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserWalletWithdrawLogPageUserSelfDTO;
import com.kar20240901.be.base.web.model.vo.base.DictIntegerVO;

public interface BaseUserWalletWithdrawLogService extends IService<BaseUserWalletWithdrawLogDO> {

    Page<DictIntegerVO> withdrawStatusDictList();

    String insertOrUpdate(BaseUserWalletWithdrawLogInsertOrUpdateDTO dto);

    String cancel(NotNullId notNullId);

    Page<BaseUserWalletWithdrawLogDO> myPage(BaseUserWalletWithdrawLogPageDTO dto);

    BaseUserWalletWithdrawLogDO infoById(NotNullId notNullId);

    Page<BaseUserWalletWithdrawLogDO> myPageUserSelf(BaseUserWalletWithdrawLogPageUserSelfDTO dto);

    String insertOrUpdateUserSelf(BaseUserWalletWithdrawLogInsertOrUpdateUserSelfDTO dto);

    String cancelUserSelf(NotNullId notNullId);

    String accept(NotEmptyIdSet notEmptyIdSet);

    String success(NotNullId notNullId);

    String reject(NotNullIdAndStringValue notNullIdAndStringValue);

}
