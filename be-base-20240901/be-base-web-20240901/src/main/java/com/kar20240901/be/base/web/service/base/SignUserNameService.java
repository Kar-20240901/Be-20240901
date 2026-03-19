package com.kar20240901.be.base.web.service.base;

import com.kar20240901.be.base.web.model.dto.base.SignUserNameJwtRefreshTokenDTO;
import com.kar20240901.be.base.web.model.dto.base.SignUserNameSetEmailDTO;
import com.kar20240901.be.base.web.model.dto.base.SignUserNameSetEmailSendCodeDTO;
import com.kar20240901.be.base.web.model.dto.base.SignUserNameSignDeleteDTO;
import com.kar20240901.be.base.web.model.dto.base.SignUserNameSignInPasswordDTO;
import com.kar20240901.be.base.web.model.dto.base.SignUserNameSignUpDTO;
import com.kar20240901.be.base.web.model.dto.base.SignUserNameUpdatePasswordDTO;
import com.kar20240901.be.base.web.model.dto.base.SignUserNameUpdateUserNameDTO;
import com.kar20240901.be.base.web.model.vo.base.SignInVO;

public interface SignUserNameService {

    String signUp(SignUserNameSignUpDTO dto);

    SignInVO signInPassword(SignUserNameSignInPasswordDTO dto);

    String updatePassword(SignUserNameUpdatePasswordDTO dto);

    String updateUserName(SignUserNameUpdateUserNameDTO dto);

    String setEmailSendCode(SignUserNameSetEmailSendCodeDTO dto);

    String setEmail(SignUserNameSetEmailDTO dto);

    String signDelete(SignUserNameSignDeleteDTO dto);

    SignInVO jwtRefreshToken(SignUserNameJwtRefreshTokenDTO dto);

}
