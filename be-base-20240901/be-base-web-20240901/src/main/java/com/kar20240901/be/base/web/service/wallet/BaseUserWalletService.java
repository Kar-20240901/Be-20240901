package com.kar20240901.be.base.web.service.wallet;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.wallet.BaseUserWalletDO;
import com.kar20240901.be.base.web.model.dto.base.ChangeBigDecimalNumberIdSetDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullLong;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserWalletPageDTO;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserWalletRechargeUserSelfDTO;
import com.kar20240901.be.base.web.model.interfaces.wallet.IBaseUserWalletLogType;
import com.kar20240901.be.base.web.model.vo.pay.BuyVO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.jetbrains.annotations.Nullable;

public interface BaseUserWalletService extends IService<BaseUserWalletDO> {

    String frozenByIdSet(NotEmptyIdSet notEmptyIdSet);

    String thawByIdSet(NotEmptyIdSet notEmptyIdSet);

    Page<BaseUserWalletDO> myPage(BaseUserWalletPageDTO dto);

    BaseUserWalletDO infoById(NotNullLong notNullLong);

    BaseUserWalletDO infoByIdUserSelf();

    String addWithdrawableMoneyBackground(ChangeBigDecimalNumberIdSetDTO dto);

    String doAddWithdrawableMoney(Long currentUserId, Date date, Set<Long> idSet, BigDecimal addNumber,
        IBaseUserWalletLogType iBaseUserWalletLogType, boolean lowErrorFlag, boolean checkWalletEnableFlag,
        @Nullable Long refId, @Nullable String refData, boolean highErrorFlag);

    BuyVO rechargeUserSelf(BaseUserWalletRechargeUserSelfDTO dto);

    // ================================ 分割线

    String changeEnableFlag(NotEmptyIdSet notEmptyIdSet, boolean enableFlag);

    Page<BaseUserWalletDO> doMyPage(BaseUserWalletPageDTO dto);

}
