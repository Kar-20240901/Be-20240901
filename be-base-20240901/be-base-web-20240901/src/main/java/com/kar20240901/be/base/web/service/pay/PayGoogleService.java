package com.kar20240901.be.base.web.service.pay;

import com.kar20240901.be.base.web.model.dto.pay.BasePayGooglePayConsumeDTO;
import com.kar20240901.be.base.web.model.dto.pay.BasePayGooglePaySuccessDTO;

public interface PayGoogleService {

    boolean paySuccess(BasePayGooglePaySuccessDTO dto);

    boolean payConsume(BasePayGooglePayConsumeDTO dto);

}
