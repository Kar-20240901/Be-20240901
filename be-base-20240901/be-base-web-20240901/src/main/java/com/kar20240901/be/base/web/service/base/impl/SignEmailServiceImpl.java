package com.kar20240901.be.base.web.service.base.impl;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.exception.base.BaseBizCodeEnum;
import com.kar20240901.be.base.web.mapper.base.BaseUserMapper;
import com.kar20240901.be.base.web.model.domain.base.BaseUserConfigurationDO;
import com.kar20240901.be.base.web.model.domain.base.TempUserDO;
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
import com.kar20240901.be.base.web.model.enums.base.BaseQrCodeSceneTypeEnum;
import com.kar20240901.be.base.web.model.enums.base.BaseRedisKeyEnum;
import com.kar20240901.be.base.web.model.enums.base.EmailMessageEnum;
import com.kar20240901.be.base.web.model.vo.base.BaseQrCodeSceneBindVO;
import com.kar20240901.be.base.web.model.vo.base.GetQrCodeVO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.base.SignInVO;
import com.kar20240901.be.base.web.service.base.SignEmailService;
import com.kar20240901.be.base.web.util.base.BaseUserConfigurationUtil;
import com.kar20240901.be.base.web.util.base.MyEmailUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.base.RequestUtil;
import com.kar20240901.be.base.web.util.base.SignUtil;
import com.kar20240901.be.base.web.util.sms.BaseSmsHelper;
import com.kar20240901.be.base.web.util.sms.BaseSmsUtil;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SignEmailServiceImpl implements SignEmailService {

    private static final BaseRedisKeyEnum PRE_REDIS_KEY_ENUM = BaseRedisKeyEnum.PRE_EMAIL;

    @Resource
    BaseUserMapper baseUserMapper;

    /**
     * 注册-发送验证码
     */
    @Override
    public String signUpSendCode(EmailNotBlankDTO dto) {

        checkSignUpEnable(); // 检查：是否允许注册

        String key = PRE_REDIS_KEY_ENUM + ":" + dto.getEmail();

        return SignUtil.sendCode(key,
            ChainWrappers.lambdaQueryChain(baseUserMapper).eq(TempUserDO::getEmail, dto.getEmail()), false,
            BaseBizCodeEnum.EMAIL_HAS_BEEN_REGISTERED,
            (code) -> MyEmailUtil.send(dto.getEmail(), EmailMessageEnum.SIGN_UP, code));

    }

    /**
     * 检查：是否允许注册
     */
    private void checkSignUpEnable() {

        BaseUserConfigurationDO baseUserConfigurationDO = BaseUserConfigurationUtil.getBaseUserConfigurationDo();

        if (BooleanUtil.isFalse(baseUserConfigurationDO.getEmailSignUpEnable())) {
            R.errorMsg("操作失败：不允许邮箱注册，请联系管理员");
        }

    }

    /**
     * 注册
     */
    @Override
    public String signUp(SignEmailSignUpDTO dto) {

        checkSignUpEnable(); // 检查：是否允许注册

        return SignUtil.signUp(dto.getPassword(), dto.getOriginPassword(), dto.getCode(), PRE_REDIS_KEY_ENUM,
            dto.getEmail());

    }

    /**
     * 邮箱：账号密码登录
     */
    @Override
    public SignInVO signInPassword(SignEmailSignInPasswordDTO dto) {

        return SignUtil.signInPassword(
            ChainWrappers.lambdaQueryChain(baseUserMapper).eq(TempUserDO::getEmail, dto.getEmail()), dto.getPassword(),
            RequestUtil.getRequestCategoryEnum());

    }

    /**
     * 邮箱验证码登录-发送验证码
     */
    @Override
    public String signInSendCode(EmailNotBlankDTO dto) {

        String key = PRE_REDIS_KEY_ENUM + ":" + dto.getEmail();

        return SignUtil.sendCode(key,
            ChainWrappers.lambdaQueryChain(baseUserMapper).eq(TempUserDO::getEmail, dto.getEmail()), true,
            BaseBizCodeEnum.EMAIL_NOT_REGISTERED,
            (code) -> MyEmailUtil.send(dto.getEmail(), EmailMessageEnum.SIGN_IN_CODE, code));

    }

    /**
     * 邮箱验证码登录
     */
    @Override
    public SignInVO signInCode(SignEmailSignInCodeDTO dto) {

        return SignUtil.signInCode(
            ChainWrappers.lambdaQueryChain(baseUserMapper).eq(TempUserDO::getEmail, dto.getEmail()), dto.getCode(),
            PRE_REDIS_KEY_ENUM, dto.getEmail(), null, RequestUtil.getRequestCategoryEnum());

    }

    /**
     * 设置密码-发送验证码
     */
    @Override
    public String setPasswordSendCode() {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null, true); // 检查：是否可以进行操作

        return SignUtil.getAccountAndSendCode(PRE_REDIS_KEY_ENUM,
            (code, account) -> MyEmailUtil.send(account, EmailMessageEnum.SET_PASSWORD, code));

    }

    /**
     * 设置密码
     */
    @Override
    public String setPassword(SignEmailSetPasswordDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null, true); // 检查：是否可以进行操作

        return SignUtil.updatePassword(dto.getPassword(), dto.getOriginPassword(), PRE_REDIS_KEY_ENUM, dto.getCode(),
            null);

    }

    /**
     * 修改密码-发送验证码
     */
    @Override
    public String updatePasswordSendCode() {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null, true); // 检查：是否可以进行操作

        return SignUtil.getAccountAndSendCode(PRE_REDIS_KEY_ENUM,
            (code, account) -> MyEmailUtil.send(account, EmailMessageEnum.UPDATE_PASSWORD, code));

    }

    /**
     * 修改密码
     */
    @Override
    public String updatePassword(SignEmailUpdatePasswordDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null, true); // 检查：是否可以进行操作

        return SignUtil.updatePassword(dto.getNewPassword(), dto.getOriginNewPassword(), PRE_REDIS_KEY_ENUM,
            dto.getCode(), null);

    }

    /**
     * 设置用户名-发送验证码
     */
    @Override
    public String setUserNameSendCode(SignEmailSetUserNameSendCodeDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null, false); // 检查：是否可以进行操作

        String currentUserEmail = MyUserUtil.getCurrentUserEmail();

        String key = BaseRedisKeyEnum.PRE_USER_NAME + ":" + dto.getUsername();

        return SignUtil.sendCode(key,
            ChainWrappers.lambdaQueryChain(baseUserMapper).eq(TempUserDO::getUsername, dto.getUsername()), false,
            BaseBizCodeEnum.USER_NAME_EXIST_PLEASE_RE_ENTER,
            (code) -> MyEmailUtil.send(currentUserEmail, EmailMessageEnum.SET_USER_NAME, code));

    }

    /**
     * 设置用户名
     */
    @Override
    public String setUserName(SignEmailSetUserNameDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null, false); // 检查：是否可以进行操作

        return SignUtil.bindAccount(dto.getCode(), BaseRedisKeyEnum.PRE_USER_NAME, dto.getUsername(), null, null, null);

    }

    /**
     * 修改用户名-发送验证码
     */
    @Override
    public String updateUserNameSendCode(SignEmailUpdateUserNameSendCodeDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null, false); // 检查：是否可以进行操作

        String currentUserEmail = MyUserUtil.getCurrentUserEmail();

        String key = BaseRedisKeyEnum.PRE_USER_NAME + ":" + dto.getUsername();

        return SignUtil.sendCode(key,
            ChainWrappers.lambdaQueryChain(baseUserMapper).eq(TempUserDO::getUsername, dto.getUsername()), false,
            BaseBizCodeEnum.USER_NAME_EXIST_PLEASE_RE_ENTER,
            (code) -> MyEmailUtil.send(currentUserEmail, EmailMessageEnum.UPDATE_USER_NAME, code));

    }

    /**
     * 修改用户名
     */
    @Override
    public String updateUserName(SignEmailUpdateUserNameDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null, false); // 检查：是否可以进行操作

        return SignUtil.bindAccount(dto.getCode(), BaseRedisKeyEnum.PRE_USER_NAME, dto.getUsername(), null, null, null);

    }

    /**
     * 修改邮箱-发送新邮箱验证码
     */
    @Override
    public String updateEmailSendCodeNew(SignEmailUpdateEmailSendCodeNewDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null, true); // 检查：是否可以进行操作

        String key = PRE_REDIS_KEY_ENUM + ":" + dto.getEmail();

        return SignUtil.sendCode(key,
            ChainWrappers.lambdaQueryChain(baseUserMapper).eq(TempUserDO::getEmail, dto.getEmail()), false,
            BaseBizCodeEnum.EMAIL_HAS_BEEN_REGISTERED,
            (code) -> MyEmailUtil.send(dto.getEmail(), EmailMessageEnum.BIND_EMAIL, code));

    }

    /**
     * 修改邮箱-发送旧邮箱验证码
     */
    @Override
    public String updateEmailSendCodeOld() {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null, true); // 检查：是否可以进行操作

        String currentUserEmail = MyUserUtil.getCurrentUserEmail();

        String key = PRE_REDIS_KEY_ENUM + ":" + currentUserEmail;

        return SignUtil.sendCode(key, null, true, TempBizCodeEnum.RESULT_SYS_ERROR,
            (code) -> MyEmailUtil.send(currentUserEmail, EmailMessageEnum.UPDATE_EMAIL, code));

    }

    /**
     * 修改邮箱
     */
    @Override
    public String updateEmail(SignEmailUpdateEmailDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null, true); // 检查：是否可以进行操作

        return SignUtil.updateAccount(dto.getOldEmailCode(), dto.getNewEmailCode(), PRE_REDIS_KEY_ENUM,
            PRE_REDIS_KEY_ENUM, dto.getNewEmail(), null, null);

    }

    /**
     * 设置微信：发送验证码
     */
    @Override
    public String setWxSendCode() {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null, true); // 检查：是否可以进行操作

        String currentUserEmail = MyUserUtil.getCurrentUserEmail();

        String key = PRE_REDIS_KEY_ENUM + ":" + currentUserEmail;

        return SignUtil.sendCode(key, null, true, TempBizCodeEnum.RESULT_SYS_ERROR,
            (code) -> MyEmailUtil.send(currentUserEmail, EmailMessageEnum.BIND_WX, code));

    }

    /**
     * 设置微信：获取二维码地址
     */
    @Override
    public GetQrCodeVO setWxGetQrCodeUrl() {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null, true); // 检查：是否可以进行操作

        // 执行
        return SignUtil.getQrCodeUrlWx(true, BaseQrCodeSceneTypeEnum.WX_BIND);

    }

    /**
     * 设置微信：获取二维码是否已经被扫描
     */
    @Override
    public BaseQrCodeSceneBindVO getQrCodeSceneFlag(NotNullId notNullId) {

        // 执行
        return SignUtil.getBaseQrCodeSceneBindVoAndHandle(notNullId.getId(), false, null);

    }

    /**
     * 设置微信
     */
    @Override
    public BaseQrCodeSceneBindVO setWx(SignEmailSetWxDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null, true); // 检查：是否可以进行操作

        String currentUserEmail = MyUserUtil.getCurrentUserEmail();

        String codeKey = PRE_REDIS_KEY_ENUM + ":" + currentUserEmail;

        // 执行
        return SignUtil.setWx(dto.getQrCodeId(), dto.getEmailCode(), codeKey, null);

    }

    /**
     * 设置手机：发送邮箱验证码
     */
    @Override
    public String setPhoneSendCodeEmail() {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null, true); // 检查：是否可以进行操作

        String currentUserEmail = MyUserUtil.getCurrentUserEmail();

        String key = PRE_REDIS_KEY_ENUM + ":" + currentUserEmail;

        return SignUtil.sendCode(key, null, true, TempBizCodeEnum.RESULT_SYS_ERROR,
            (code) -> MyEmailUtil.send(currentUserEmail, EmailMessageEnum.BIND_PHONE, code));

    }

    /**
     * 设置手机：发送手机验证码
     */
    @Override
    public String setPhoneSendCodePhone(SignEmailSetPhoneSendCodePhoneDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null, true); // 检查：是否可以进行操作

        String key = BaseRedisKeyEnum.PRE_PHONE + ":" + dto.getPhone();

        return SignUtil.sendCode(key,
            ChainWrappers.lambdaQueryChain(baseUserMapper).eq(TempUserDO::getPhone, dto.getPhone()), false,
            BaseBizCodeEnum.PHONE_HAS_BEEN_REGISTERED,
            (code) -> BaseSmsUtil.sendSetPhone(BaseSmsHelper.getBaseSmsSendBO(code, dto.getPhone())));

    }

    /**
     * 设置手机
     */
    @Override
    public String setPhone(SignEmailSetPhoneDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null, true); // 检查：是否可以进行操作

        return SignUtil.updateAccount(dto.getEmailCode(), dto.getPhoneCode(), PRE_REDIS_KEY_ENUM,
            BaseRedisKeyEnum.PRE_PHONE, dto.getPhone(), null, null);

    }

    /**
     * 忘记密码-发送验证码
     */
    @Override
    public String forgetPasswordSendCode(EmailNotBlankDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, dto.getEmail(), null, true); // 检查：是否可以进行操作

        String key = PRE_REDIS_KEY_ENUM + ":" + dto.getEmail();

        return SignUtil.sendCode(key,
            ChainWrappers.lambdaQueryChain(baseUserMapper).eq(TempUserDO::getEmail, dto.getEmail()), true,
            BaseBizCodeEnum.EMAIL_NOT_REGISTERED,
            (code) -> MyEmailUtil.send(dto.getEmail(), EmailMessageEnum.FORGET_PASSWORD, code));

    }

    /**
     * 忘记密码
     */
    @Override
    public String forgetPassword(SignEmailForgetPasswordDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, dto.getEmail(), null, true); // 检查：是否可以进行操作

        return SignUtil.forgetPassword(dto.getNewPassword(), dto.getOriginNewPassword(), dto.getCode(),
            PRE_REDIS_KEY_ENUM, dto.getEmail(),
            ChainWrappers.lambdaQueryChain(baseUserMapper).eq(TempUserDO::getEmail, dto.getEmail()));

    }

    /**
     * 账号注销-发送验证码
     */
    @Override
    public String signDeleteSendCode() {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null, false); // 检查：是否可以进行操作

        return SignUtil.getAccountAndSendCode(PRE_REDIS_KEY_ENUM,
            (code, account) -> MyEmailUtil.send(account, EmailMessageEnum.SIGN_DELETE, code));

    }

    /**
     * 账号注销
     */
    @Override
    public String signDelete(NotBlankCodeDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null, false); // 检查：是否可以进行操作

        return SignUtil.signDelete(dto.getCode(), PRE_REDIS_KEY_ENUM, null, null);

    }

}
