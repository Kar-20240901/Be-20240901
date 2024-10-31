package com.kar20240901.be.base.web.service.base.impl;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.exception.base.BaseBizCodeEnum;
import com.kar20240901.be.base.web.mapper.base.TempUserMapper;
import com.kar20240901.be.base.web.model.domain.base.BaseUserConfigurationDO;
import com.kar20240901.be.base.web.model.domain.base.TempUserDO;
import com.kar20240901.be.base.web.model.dto.base.EmailNotBlankDTO;
import com.kar20240901.be.base.web.model.dto.base.NotBlankCodeDTO;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.base.SignEmailForgetPasswordDTO;
import com.kar20240901.be.base.web.model.dto.base.SignEmailSetPhoneDTO;
import com.kar20240901.be.base.web.model.dto.base.SignEmailSetPhoneSendCodePhoneDTO;
import com.kar20240901.be.base.web.model.dto.base.SignEmailSetUserNameDTO;
import com.kar20240901.be.base.web.model.dto.base.SignEmailSetUserNameSendCodeDTO;
import com.kar20240901.be.base.web.model.dto.base.SignEmailSetWxDTO;
import com.kar20240901.be.base.web.model.dto.base.SignEmailSignInPasswordDTO;
import com.kar20240901.be.base.web.model.dto.base.SignEmailSignUpDTO;
import com.kar20240901.be.base.web.model.dto.base.SignEmailUpdateEmailDTO;
import com.kar20240901.be.base.web.model.dto.base.SignEmailUpdateEmailSendCodeNewDTO;
import com.kar20240901.be.base.web.model.dto.base.SignEmailUpdatePasswordDTO;
import com.kar20240901.be.base.web.model.dto.base.SignEmailUpdateUserNameDTO;
import com.kar20240901.be.base.web.model.dto.base.SignEmailUpdateUserNameSendCodeDTO;
import com.kar20240901.be.base.web.model.enums.base.BaseRedisKeyEnum;
import com.kar20240901.be.base.web.model.enums.base.EmailMessageEnum;
import com.kar20240901.be.base.web.model.vo.base.GetQrCodeVO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.base.SignInVO;
import com.kar20240901.be.base.web.model.vo.base.SysQrCodeSceneBindVO;
import com.kar20240901.be.base.web.service.base.BaseUserConfigurationService;
import com.kar20240901.be.base.web.service.base.SignEmailService;
import com.kar20240901.be.base.web.util.base.MyEmailUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.base.RequestUtil;
import com.kar20240901.be.base.web.util.base.SignUtil;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SignEmailServiceImpl implements SignEmailService {

    private static final BaseRedisKeyEnum PRE_REDIS_KEY_ENUM = BaseRedisKeyEnum.PRE_EMAIL;

    @Resource
    TempUserMapper baseUserMapper;

    @Resource
    BaseUserConfigurationService baseUserConfigurationService;

    /**
     * 注册-发送验证码
     */
    @Override
    public String signUpSendCode(EmailNotBlankDTO dto) {

        checkSignUpEnable(); // 检查：是否允许注册

        String key = BaseRedisKeyEnum.PRE_EMAIL + dto.getEmail();

        return SignUtil.sendCode(key,
            ChainWrappers.lambdaQueryChain(baseUserMapper).eq(TempUserDO::getEmail, dto.getEmail()), false,
            BaseBizCodeEnum.EMAIL_HAS_BEEN_REGISTERED,
            (code) -> MyEmailUtil.send(dto.getEmail(), EmailMessageEnum.SIGN_UP, code));

    }

    /**
     * 检查：是否允许注册
     */
    private void checkSignUpEnable() {

        BaseUserConfigurationDO baseUserConfigurationDO = baseUserConfigurationService.getBaseUserConfigurationDo();

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

        return SignUtil.signUp(dto.getPassword(), dto.getOriginPassword(), dto.getCode(), BaseRedisKeyEnum.PRE_EMAIL,
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
     * 修改密码-发送验证码
     */
    @Override
    public String updatePasswordSendCode() {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null); // 检查：是否可以进行操作

        return SignUtil.getAccountAndSendCode(BaseRedisKeyEnum.PRE_EMAIL,
            (code, account) -> MyEmailUtil.send(account, EmailMessageEnum.UPDATE_PASSWORD, code));

    }

    /**
     * 修改密码
     */
    @Override
    public String updatePassword(SignEmailUpdatePasswordDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null); // 检查：是否可以进行操作

        return SignUtil.updatePassword(dto.getNewPassword(), dto.getOriginNewPassword(), BaseRedisKeyEnum.PRE_EMAIL,
            dto.getCode(), null);

    }

    /**
     * 设置用户名-发送验证码
     */
    @Override
    public String setUserNameSendCode(SignEmailSetUserNameSendCodeDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null); // 检查：是否可以进行操作

        String currentUserEmail = MyUserUtil.getCurrentUserEmail();

        String key = BaseRedisKeyEnum.PRE_USER_NAME + dto.getUsername();

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

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null); // 检查：是否可以进行操作

        return SignUtil.bindAccount(dto.getCode(), BaseRedisKeyEnum.PRE_USER_NAME, dto.getSignInName(), null, null,
            null);

    }

    /**
     * 修改用户名-发送验证码
     */
    @Override
    public String updateUserNameSendCode(SignEmailUpdateUserNameSendCodeDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null); // 检查：是否可以进行操作

        String currentUserEmailNotAdmin = MyUserUtil.getCurrentUserEmail();

        String key = BaseRedisKeyEnum.PRE_USER_NAME + dto.getUsername();

        return SignUtil.sendCode(key,
            ChainWrappers.lambdaQueryChain(baseUserMapper).eq(TempUserDO::getUsername, dto.getUsername()), false,
            BaseBizCodeEnum.USER_NAME_EXIST_PLEASE_RE_ENTER,
            (code) -> MyEmailUtil.send(currentUserEmailNotAdmin, EmailMessageEnum.UPDATE_USER_NAME, code));

    }

    /**
     * 修改用户名
     */
    @Override
    public String updateUserName(SignEmailUpdateUserNameDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null); // 检查：是否可以进行操作

        return SignUtil.bindAccount(dto.getCode(), BaseRedisKeyEnum.PRE_USER_NAME, dto.getUsername(), null, null, null);

    }

    /**
     * 修改邮箱-发送新邮箱验证码
     */
    @Override
    public String updateEmailSendCodeNew(SignEmailUpdateEmailSendCodeNewDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null); // 检查：是否可以进行操作

        String key = BaseRedisKeyEnum.PRE_EMAIL + dto.getEmail();

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

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null); // 检查：是否可以进行操作

        String currentUserEmailNotAdmin = MyUserUtil.getCurrentUserEmail();

        String key = BaseRedisKeyEnum.PRE_EMAIL + currentUserEmailNotAdmin;

        return SignUtil.sendCode(key, null, true, TempBizCodeEnum.RESULT_SYS_ERROR,
            (code) -> MyEmailUtil.send(currentUserEmailNotAdmin, EmailMessageEnum.UPDATE_EMAIL, code));

    }

    /**
     * 修改邮箱
     */
    @Override
    public String updateEmail(SignEmailUpdateEmailDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null); // 检查：是否可以进行操作

        return SignUtil.updateAccount(dto.getOldEmailCode(), dto.getNewEmailCode(), BaseRedisKeyEnum.PRE_EMAIL,
            BaseRedisKeyEnum.PRE_EMAIL, dto.getNewEmail(), null, null);

    }

    /**
     * 设置微信：发送验证码
     */
    @Override
    public String setWxSendCode() {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null); // 检查：是否可以进行操作

        String currentUserEmailNotAdmin = MyUserUtil.getCurrentUserEmail();

        String key = BaseRedisKeyEnum.PRE_EMAIL + currentUserEmailNotAdmin;

        return SignUtil.sendCode(key, null, true, TempBizCodeEnum.RESULT_SYS_ERROR,
            (code) -> MyEmailUtil.send(currentUserEmailNotAdmin, EmailMessageEnum.BIND_WX, code));

    }

    /**
     * 设置微信：获取二维码地址
     */
    @Override
    public GetQrCodeVO setWxGetQrCodeUrl() {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null); // 检查：是否可以进行操作

        // 执行
        return SignUtil.getQrCodeUrlWx(UserUtil.getCurrentTenantIdDefault(), true, SysQrCodeSceneTypeEnum.WX_BIND);

    }

    /**
     * 设置微信：获取二维码是否已经被扫描
     */
    @Override
    public SysQrCodeSceneBindVO getQrCodeSceneFlag(NotNullId notNullId) {

        // 执行
        return SignUtil.getSysQrCodeSceneBindVoAndHandle(notNullId.getId(), false, null);

    }

    /**
     * 设置微信
     */
    @Override
    public SysQrCodeSceneBindVO setWx(SignEmailSetWxDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, UserUtil.getCurrentTenantIdDefault(), null); // 检查：是否可以进行操作

        String currentUserEmailNotAdmin = UserUtil.getCurrentUserEmailNotAdmin();

        String codeKey = BaseRedisKeyEnum.PRE_EMAIL + currentUserEmailNotAdmin;

        // 执行
        return SignUtil.setWx(dto.getQrCodeId(), dto.getEmailCode(), codeKey, null);

    }

    /**
     * 设置手机：发送邮箱验证码
     */
    @Override
    public String setPhoneSendCodeEmail() {

        Long currentTenantIdDefault = UserUtil.getCurrentTenantIdDefault();

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, currentTenantIdDefault, null); // 检查：是否可以进行操作

        String currentUserEmailNotAdmin = UserUtil.getCurrentUserEmailNotAdmin();

        String key = BaseRedisKeyEnum.PRE_EMAIL + currentUserEmailNotAdmin;

        return SignUtil.sendCode(key, null, true, BaseBizCodeEnum.API_RESULT_SYS_ERROR,
            (code) -> MyEmailUtil.send(currentUserEmailNotAdmin, EmailMessageEnum.BIND_PHONE, code,
                currentTenantIdDefault), currentTenantIdDefault);

    }

    /**
     * 设置手机：发送手机验证码
     */
    @Override
    public String setPhoneSendCodePhone(SignEmailSetPhoneSendCodePhoneDTO dto) {

        Long currentTenantIdDefault = UserUtil.getCurrentTenantIdDefault();

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, currentTenantIdDefault, null); // 检查：是否可以进行操作

        String key = BaseRedisKeyEnum.PRE_PHONE + dto.getPhone();

        return SignUtil.sendCode(key,
            ChainWrappers.lambdaQueryChain(baseUserMapper).eq(SysUserDO::getPhone, dto.getPhone()), false,
            BizCodeEnum.PHONE_HAS_BEEN_REGISTERED, (code) -> SysSmsUtil.sendSetPhone(
                SysSmsHelper.getSysSmsSendBO(currentTenantIdDefault, code, dto.getPhone())), currentTenantIdDefault);

    }

    /**
     * 设置手机
     */
    @Override
    public String setPhone(SignEmailSetPhoneDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, UserUtil.getCurrentTenantIdDefault(), null); // 检查：是否可以进行操作

        return SignUtil.updateAccount(dto.getEmailCode(), dto.getPhoneCode(), BaseRedisKeyEnum.PRE_EMAIL,
            BaseRedisKeyEnum.PRE_PHONE, dto.getPhone(), null, null);

    }

    /**
     * 设置统一登录：微信：发送邮箱验证码
     */
    @Override
    public String setSingleSignInWxSendCode() {

        Long currentTenantIdDefault = UserUtil.getCurrentTenantIdDefault();

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, currentTenantIdDefault, null); // 检查：是否可以进行操作

        String currentUserEmailNotAdmin = UserUtil.getCurrentUserEmailNotAdmin();

        String key = BaseRedisKeyEnum.PRE_EMAIL + currentUserEmailNotAdmin;

        return SignUtil.sendCode(key, null, true, BaseBizCodeEnum.API_RESULT_SYS_ERROR,
            (code) -> MyEmailUtil.send(currentUserEmailNotAdmin, EmailMessageEnum.SET_SINGLE_SIGN_IN, code,
                currentTenantIdDefault), currentTenantIdDefault);

    }

    /**
     * 设置统一登录：微信：获取统一登录微信的二维码地址
     */
    @Override
    public GetQrCodeVO setSingleSignInWxGetQrCodeUrl() {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, UserUtil.getCurrentTenantIdDefault(), null); // 检查：是否可以进行操作

        // 执行
        return SignUtil.getQrCodeUrlWxForSingleSignIn(true, SysQrCodeSceneTypeEnum.WX_SINGLE_SIGN_IN_BIND);

    }

    /**
     * 设置统一登录：微信：获取统一登录微信的二维码是否已经被扫描
     */
    @Override
    public SysQrCodeSceneBindVO setSingleSignInWxGetQrCodeSceneFlag(NotNullId notNullId) {

        // 执行
        return SignUtil.getSysQrCodeSceneBindVoAndHandleForSingleSignIn(notNullId.getId(), false, null);

    }

    /**
     * 设置统一登录：微信
     */
    @Override
    public SysQrCodeSceneBindVO setSingleSignInWx(SignEmailSetSingleSignInWxDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, UserUtil.getCurrentTenantIdDefault(), null); // 检查：是否可以进行操作

        String currentUserEmailNotAdmin = UserUtil.getCurrentUserEmailNotAdmin();

        String codeKey = BaseRedisKeyEnum.PRE_EMAIL + currentUserEmailNotAdmin;

        // 执行
        return SignUtil.setWxForSingleSignIn(dto.getQrCodeId(), dto.getEmailCode(), codeKey, null);

    }

    /**
     * 设置统一登录：手机验证码：发送当前账号已经绑定邮箱的验证码
     */
    @Override
    public String setSingleSignInPhoneSendCodeCurrent() {

        Long currentTenantIdDefault = UserUtil.getCurrentTenantIdDefault();

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, currentTenantIdDefault, null); // 检查：是否可以进行操作

        String currentUserEmailNotAdmin = UserUtil.getCurrentUserEmailNotAdmin();

        String key = BaseRedisKeyEnum.PRE_EMAIL + currentUserEmailNotAdmin;

        return SignUtil.sendCode(key, null, true, BaseBizCodeEnum.API_RESULT_SYS_ERROR,
            (code) -> MyEmailUtil.send(currentUserEmailNotAdmin, EmailMessageEnum.SET_SINGLE_SIGN_IN, code,
                currentTenantIdDefault), currentTenantIdDefault);

    }

    /**
     * 设置统一登录：手机验证码：发送要绑定统一登录手机的验证码
     */
    @Override
    public String setSingleSignInSendCodePhone(SignEmailSetSingleSignInPhoneSendCodeDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, UserUtil.getCurrentTenantIdDefault(), null); // 检查：是否可以进行操作

        // 执行
        return SignUtil.sendCodeForSingle(dto.getPhone(), false, "操作失败：该手机号已被绑定",
            (code) -> SysSmsUtil.sendSetSingleSignIn(
                SysSmsHelper.getSysSmsSendBO(code, dto.getPhone(), singleSignInProperties.getSmsConfigurationId())),
            BaseRedisKeyEnum.PRE_SYS_SINGLE_SIGN_IN_SET_PHONE);

    }

    /**
     * 设置统一登录：手机验证码
     */
    @Override
    public String setSingleSignInPhone(SignEmailSetSingleSignInPhoneDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, UserUtil.getCurrentTenantIdDefault(), null); // 检查：是否可以进行操作

        // 执行
        return SignUtil.bindAccountForSingle(dto.getSingleSignInPhoneCode(),
            BaseRedisKeyEnum.PRE_SYS_SINGLE_SIGN_IN_SET_PHONE, dto.getSingleSignInPhone(), null,
            dto.getCurrentEmailCode(), BaseRedisKeyEnum.PRE_EMAIL);

    }

    /**
     * 忘记密码-发送验证码
     */
    @Override
    public String forgetPasswordSendCode(EmailNotBlankDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, dto.getEmail(), dto.getTenantId(), null); // 检查：是否可以进行操作

        String key = BaseRedisKeyEnum.PRE_EMAIL + dto.getEmail();

        return SignUtil.sendCode(key,
            ChainWrappers.lambdaQueryChain(baseUserMapper).eq(SysUserDO::getEmail, dto.getEmail()), true,
            com.cmcorg20230301.be.engine.email.exception.BizCodeEnum.EMAIL_NOT_REGISTERED,
            (code) -> MyEmailUtil.send(dto.getEmail(), EmailMessageEnum.FORGET_PASSWORD, code, dto.getTenantId()),
            dto.getTenantId());

    }

    /**
     * 忘记密码
     */
    @Override
    public String forgetPassword(SignEmailForgetPasswordDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, dto.getEmail(), dto.getTenantId(), null); // 检查：是否可以进行操作

        return SignUtil.forgetPassword(dto.getNewPassword(), dto.getOriginNewPassword(), dto.getCode(),
            BaseRedisKeyEnum.PRE_EMAIL, dto.getEmail(),
            ChainWrappers.lambdaQueryChain(baseUserMapper).eq(SysUserDO::getEmail, dto.getEmail()), dto.getTenantId());

    }

    /**
     * 账号注销-发送验证码
     */
    @Override
    public String signDeleteSendCode() {

        Long currentTenantIdDefault = UserUtil.getCurrentTenantIdDefault();

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, currentTenantIdDefault, null); // 检查：是否可以进行操作

        return SignUtil.getAccountAndSendCode(BaseRedisKeyEnum.PRE_EMAIL,
            (code, account) -> MyEmailUtil.send(account, EmailMessageEnum.SIGN_DELETE, code, currentTenantIdDefault));

    }

    /**
     * 账号注销
     */
    @Override
    public String signDelete(NotBlankCodeDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, UserUtil.getCurrentTenantIdDefault(), null); // 检查：是否可以进行操作

        return SignUtil.signDelete(dto.getCode(), BaseRedisKeyEnum.PRE_EMAIL, null, null);

    }

}
