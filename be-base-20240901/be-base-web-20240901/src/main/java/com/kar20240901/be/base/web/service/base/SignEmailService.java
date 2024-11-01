package com.kar20240901.be.base.web.service.base;

import com.kar20240901.be.base.web.model.dto.base.EmailNotBlankDTO;
import com.kar20240901.be.base.web.model.dto.base.NotBlankCodeDTO;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.base.SignEmailForgetPasswordDTO;
import com.kar20240901.be.base.web.model.dto.base.SignEmailSetPhoneDTO;
import com.kar20240901.be.base.web.model.dto.base.SignEmailSetPhoneSendCodePhoneDTO;
import com.kar20240901.be.base.web.model.dto.base.SignEmailSetUserNameDTO;
import com.kar20240901.be.base.web.model.dto.base.SignEmailSetUserNameSendCodeDTO;
import com.kar20240901.be.base.web.model.dto.base.SignEmailSetWxDTO;
import com.kar20240901.be.base.web.model.dto.base.SignEmailSignInCodeDTO;
import com.kar20240901.be.base.web.model.dto.base.SignEmailSignInPasswordDTO;
import com.kar20240901.be.base.web.model.dto.base.SignEmailSignUpDTO;
import com.kar20240901.be.base.web.model.dto.base.SignEmailUpdateEmailDTO;
import com.kar20240901.be.base.web.model.dto.base.SignEmailUpdateEmailSendCodeNewDTO;
import com.kar20240901.be.base.web.model.dto.base.SignEmailUpdatePasswordDTO;
import com.kar20240901.be.base.web.model.dto.base.SignEmailUpdateUserNameDTO;
import com.kar20240901.be.base.web.model.dto.base.SignEmailUpdateUserNameSendCodeDTO;
import com.kar20240901.be.base.web.model.vo.base.BaseQrCodeSceneBindVO;
import com.kar20240901.be.base.web.model.vo.base.GetQrCodeVO;
import com.kar20240901.be.base.web.model.vo.base.SignInVO;

public interface SignEmailService {

    String signUpSendCode(EmailNotBlankDTO dto);

    String signUp(SignEmailSignUpDTO dto);

    SignInVO signInPassword(SignEmailSignInPasswordDTO dto);

    String signInSendCode(EmailNotBlankDTO dto);

    SignInVO signInCode(SignEmailSignInCodeDTO dto);

    String updatePasswordSendCode();

    String updatePassword(SignEmailUpdatePasswordDTO dto);

    String setUserNameSendCode(SignEmailSetUserNameSendCodeDTO dto);

    String setUserName(SignEmailSetUserNameDTO dto);

    String updateUserNameSendCode(SignEmailUpdateUserNameSendCodeDTO dto);

    String updateUserName(SignEmailUpdateUserNameDTO dto);

    String updateEmailSendCodeNew(SignEmailUpdateEmailSendCodeNewDTO dto);

    String updateEmailSendCodeOld();

    String updateEmail(SignEmailUpdateEmailDTO dto);

    String setWxSendCode();

    GetQrCodeVO setWxGetQrCodeUrl();

    BaseQrCodeSceneBindVO getQrCodeSceneFlag(NotNullId notNullId);

    BaseQrCodeSceneBindVO setWx(SignEmailSetWxDTO dto);

    String setPhoneSendCodeEmail();

    String setPhoneSendCodePhone(SignEmailSetPhoneSendCodePhoneDTO dto);

    String setPhone(SignEmailSetPhoneDTO dto);

    String forgetPasswordSendCode(EmailNotBlankDTO dto);

    String forgetPassword(SignEmailForgetPasswordDTO dto);

    String signDeleteSendCode();

    String signDelete(NotBlankCodeDTO dto);

}
