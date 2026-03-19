package com.kar20240901.be.base.web.service.wallet;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.wallet.BaseUserWalletLogDO;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserWalletLogPageDTO;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserWalletLogUserSelfPageDTO;

public interface BaseUserWalletLogService extends IService<BaseUserWalletLogDO> {

    Page<BaseUserWalletLogDO> myPage(BaseUserWalletLogPageDTO dto);

    Page<BaseUserWalletLogDO> myPageUserSelf(BaseUserWalletLogUserSelfPageDTO dto);

}
