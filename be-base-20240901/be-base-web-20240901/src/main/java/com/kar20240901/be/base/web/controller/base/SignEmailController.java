package com.kar20240901.be.base.web.controller.base;

import com.kar20240901.be.base.web.model.constant.base.OperationDescriptionConstant;
import com.kar20240901.be.base.web.model.dto.base.EmailNotBlankDTO;
import com.kar20240901.be.base.web.model.dto.base.NotBlankCodeDTO;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.base.SignEmailForgetPasswordDTO;
import com.kar20240901.be.base.web.model.dto.base.SignEmailSetPasswordDTO;
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
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.base.SignInVO;
import com.kar20240901.be.base.web.service.base.SignEmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/sign/email")
@Tag(name = "基础-登录注册-邮箱")
public class SignEmailController {

    @Resource
    SignEmailService baseService;

    @PostMapping(value = "/signUp/sendCode")
    @Operation(summary = "注册-发送验证码")
    public R<String> signUpSendCode(@RequestBody @Valid EmailNotBlankDTO dto) {
        return R.okMsg(baseService.signUpSendCode(dto));
    }

    @PostMapping(value = "/signUp")
    @Operation(summary = "注册")
    public R<String> signUp(@RequestBody @Valid SignEmailSignUpDTO dto) {
        return R.okMsg(baseService.signUp(dto));
    }

    @PostMapping(value = "/signIn/password")
    @Operation(summary = "邮箱：账号密码登录", description = OperationDescriptionConstant.SIGN_IN)
    public R<SignInVO> signInPassword(@RequestBody @Valid SignEmailSignInPasswordDTO dto) {
        return R.okData(baseService.signInPassword(dto));
    }

    @PostMapping(value = "/signIn/sendCode")
    @Operation(summary = "邮箱验证码登录-发送验证码")
    public R<String> signInSendCode(@RequestBody @Valid EmailNotBlankDTO dto) {
        return R.okMsg(baseService.signInSendCode(dto));
    }

    @PostMapping(value = "/signIn/code")
    @Operation(summary = "邮箱验证码登录", description = OperationDescriptionConstant.SIGN_IN)
    public R<SignInVO> signInCode(@RequestBody @Valid SignEmailSignInCodeDTO dto) {
        return R.okData(baseService.signInCode(dto));
    }

    @PostMapping(value = "/setPassword/sendCode")
    @Operation(summary = "设置密码-发送验证码")
    public R<String> setPasswordSendCode() {
        return R.okMsg(baseService.setPasswordSendCode());
    }

    @PostMapping(value = "/setPassword")
    @Operation(summary = "设置密码")
    public R<String> setPassword(@RequestBody @Valid SignEmailSetPasswordDTO dto) {
        return R.okMsg(baseService.setPassword(dto));
    }

    @PostMapping(value = "/updatePassword/sendCode")
    @Operation(summary = "修改密码-发送验证码")
    public R<String> updatePasswordSendCode() {
        return R.okMsg(baseService.updatePasswordSendCode());
    }

    @PostMapping(value = "/updatePassword")
    @Operation(summary = "修改密码")
    public R<String> updatePassword(@RequestBody @Valid SignEmailUpdatePasswordDTO dto) {
        return R.okMsg(baseService.updatePassword(dto));
    }

    @PostMapping(value = "/setUserName/sendCode")
    @Operation(summary = "设置用户名-发送验证码")
    public R<String> setUserNameSendCode(@RequestBody @Valid SignEmailSetUserNameSendCodeDTO dto) {
        return R.okMsg(baseService.setUserNameSendCode(dto));
    }

    @PostMapping(value = "/setUserName")
    @Operation(summary = "设置用户名")
    public R<String> setUserName(@RequestBody @Valid SignEmailSetUserNameDTO dto) {
        return R.okMsg(baseService.setUserName(dto));
    }

    @PostMapping(value = "/updateUserName/sendCode")
    @Operation(summary = "修改用户名-发送验证码")
    public R<String> updateUserNameSendCode(@RequestBody @Valid SignEmailUpdateUserNameSendCodeDTO dto) {
        return R.okMsg(baseService.updateUserNameSendCode(dto));
    }

    @PostMapping(value = "/updateUserName")
    @Operation(summary = "修改用户名")
    public R<String> updateUserName(@RequestBody @Valid SignEmailUpdateUserNameDTO dto) {
        return R.okMsg(baseService.updateUserName(dto));
    }

    @PostMapping(value = "/updateEmail/sendCode/new")
    @Operation(summary = "修改邮箱-发送新邮箱验证码")
    public R<String> updateEmailSendCodeNew(@RequestBody @Valid SignEmailUpdateEmailSendCodeNewDTO dto) {
        return R.okMsg(baseService.updateEmailSendCodeNew(dto));
    }

    @PostMapping(value = "/updateEmail/sendCode/old")
    @Operation(summary = "修改邮箱-发送旧邮箱验证码")
    public R<String> updateEmailSendCodeOld() {
        return R.okMsg(baseService.updateEmailSendCodeOld());
    }

    @PostMapping(value = "/updateEmail")
    @Operation(summary = "修改邮箱")
    public R<String> updateEmail(@RequestBody @Valid SignEmailUpdateEmailDTO dto) {
        return R.okMsg(baseService.updateEmail(dto));
    }

    @PostMapping(value = "/setWx/sendCode")
    @Operation(summary = "设置微信：发送验证码")
    public R<String> setWxSendCode() {
        return R.okMsg(baseService.setWxSendCode());
    }

    @PostMapping(value = "/setWx/getQrCodeUrl")
    @Operation(summary = "设置微信：获取二维码地址")
    public R<GetQrCodeVO> setWxGetQrCodeUrl() {
        return R.okData(baseService.setWxGetQrCodeUrl());
    }

    @PostMapping(value = "/setWx/getQrCodeSceneFlag")
    @Operation(summary = "设置微信：获取二维码是否已经被扫描")
    public R<BaseQrCodeSceneBindVO> getQrCodeSceneFlag(@RequestBody @Valid NotNullId notNullId) {
        return R.okData(baseService.getQrCodeSceneFlag(notNullId));
    }

    @PostMapping(value = "/setWx")
    @Operation(summary = "设置微信")
    public R<BaseQrCodeSceneBindVO> setWx(@RequestBody @Valid SignEmailSetWxDTO dto) {
        return R.okData(baseService.setWx(dto));
    }

    @PostMapping(value = "/setPhone/sendCode/email")
    @Operation(summary = "设置手机：发送邮箱验证码")
    public R<String> setPhoneSendCodeEmail() {
        return R.okMsg(baseService.setPhoneSendCodeEmail());
    }

    @PostMapping(value = "/setPhone/sendCode/phone")
    @Operation(summary = "设置手机：发送手机验证码")
    public R<String> setPhoneSendCodePhone(@RequestBody @Valid SignEmailSetPhoneSendCodePhoneDTO dto) {
        return R.okMsg(baseService.setPhoneSendCodePhone(dto));
    }

    @PostMapping(value = "/setPhone")
    @Operation(summary = "设置手机")
    public R<String> setPhone(@RequestBody @Valid SignEmailSetPhoneDTO dto) {
        return R.okMsg(baseService.setPhone(dto));
    }

    @PostMapping(value = "/forgetPassword/sendCode")
    @Operation(summary = "忘记密码-发送验证码")
    public R<String> forgetPasswordSendCode(@RequestBody @Valid EmailNotBlankDTO dto) {
        return R.okMsg(baseService.forgetPasswordSendCode(dto));
    }

    @PostMapping(value = "/forgetPassword")
    @Operation(summary = "忘记密码")
    public R<String> forgetPassword(@RequestBody @Valid SignEmailForgetPasswordDTO dto) {
        return R.okMsg(baseService.forgetPassword(dto));
    }

    @PostMapping(value = "/signDelete/sendCode")
    @Operation(summary = "账号注销-发送验证码")
    public R<String> signDeleteSendCode() {
        return R.okMsg(baseService.signDeleteSendCode());
    }

    @PostMapping(value = "/signDelete")
    @Operation(summary = "账号注销")
    public R<String> signDelete(@RequestBody @Valid NotBlankCodeDTO dto) {
        return R.okMsg(baseService.signDelete(dto));
    }

}
