package com.kar20240901.be.base.web.util.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.lang.func.VoidFunc0;
import cn.hutool.core.lang.func.VoidFunc1;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.exception.base.BaseBizCodeEnum;
import com.kar20240901.be.base.web.mapper.base.BaseDeptRefUserMapper;
import com.kar20240901.be.base.web.mapper.base.BasePostRefUserMapper;
import com.kar20240901.be.base.web.mapper.base.BaseRoleRefUserMapper;
import com.kar20240901.be.base.web.mapper.base.BaseUserInfoMapper;
import com.kar20240901.be.base.web.mapper.base.BaseUserMapper;
import com.kar20240901.be.base.web.mapper.thirdapp.BaseThirdAppMapper;
import com.kar20240901.be.base.web.model.bo.base.BaseQrCodeSceneBindBO;
import com.kar20240901.be.base.web.model.configuration.base.IUserSignConfiguration;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.constant.base.TempRegexConstant;
import com.kar20240901.be.base.web.model.domain.base.BaseDeptRefUserDO;
import com.kar20240901.be.base.web.model.domain.base.BasePostRefUserDO;
import com.kar20240901.be.base.web.model.domain.base.BaseRoleRefUserDO;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoId;
import com.kar20240901.be.base.web.model.domain.base.TempUserDO;
import com.kar20240901.be.base.web.model.domain.base.TempUserInfoDO;
import com.kar20240901.be.base.web.model.domain.thirdapp.BaseThirdAppDO;
import com.kar20240901.be.base.web.model.enums.base.BaseRedisKeyEnum;
import com.kar20240901.be.base.web.model.enums.base.BaseRequestCategoryEnum;
import com.kar20240901.be.base.web.model.enums.base.TempRedisKeyEnum;
import com.kar20240901.be.base.web.model.enums.thirdapp.BaseThirdAppTypeEnum;
import com.kar20240901.be.base.web.model.interfaces.base.IBaseQrCodeSceneType;
import com.kar20240901.be.base.web.model.interfaces.base.IBizCode;
import com.kar20240901.be.base.web.model.interfaces.base.IRedisKey;
import com.kar20240901.be.base.web.model.vo.base.BaseQrCodeSceneBindVO;
import com.kar20240901.be.base.web.model.vo.base.GetQrCodeVO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.base.SignInVO;
import com.kar20240901.be.base.web.properties.base.BaseSecurityProperties;
import com.kar20240901.be.base.web.service.base.impl.BaseRoleServiceImpl;
import com.kar20240901.be.base.web.util.thirdapp.WxUtil;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBatch;
import org.redisson.api.RBucket;
import org.redisson.api.RKeysAsync;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
public class SignUtil {

    private static RedissonClient redissonClient;

    @Resource
    public void setRedissonClient(RedissonClient redissonClient) {
        SignUtil.redissonClient = redissonClient;
    }

    private static BaseUserMapper baseUserMapper;

    @Resource
    public void setBaseUserMapper(BaseUserMapper baseUserMapper) {
        SignUtil.baseUserMapper = baseUserMapper;
    }

    private static BaseUserInfoMapper baseUserInfoMapper;

    @Resource
    public void setBaseUserInfoMapper(BaseUserInfoMapper baseUserInfoMapper) {
        SignUtil.baseUserInfoMapper = baseUserInfoMapper;
    }

    private static BaseSecurityProperties baseSecurityProperties;

    @Resource
    public void setBaseSecurityProperties(BaseSecurityProperties baseSecurityProperties) {
        SignUtil.baseSecurityProperties = baseSecurityProperties;
    }

    private static BaseRoleRefUserMapper baseRoleRefUserMapper;

    @Resource
    public void setBaseRoleRefUserMapper(BaseRoleRefUserMapper baseRoleRefUserMapper) {
        SignUtil.baseRoleRefUserMapper = baseRoleRefUserMapper;
    }

    private static BaseThirdAppMapper baseThirdAppMapper;

    @Resource
    public void setBaseThirdAppMapper(BaseThirdAppMapper baseThirdAppMapper) {
        SignUtil.baseThirdAppMapper = baseThirdAppMapper;
    }

    @Nullable
    private static List<IUserSignConfiguration> iUserSignConfigurationList;

    @Resource
    public void setiUserSignConfigurationList(@Nullable List<IUserSignConfiguration> iUserSignConfigurationList) {
        SignUtil.iUserSignConfigurationList = iUserSignConfigurationList;
    }

    private static BaseDeptRefUserMapper baseDeptRefUserMapper;

    @Resource
    public void setBaseDeptRefUserMapper(BaseDeptRefUserMapper baseDeptRefUserMapper) {
        SignUtil.baseDeptRefUserMapper = baseDeptRefUserMapper;
    }

    private static BasePostRefUserMapper basePostRefUserMapper;

    @Resource
    public void setBasePostRefUserMapper(BasePostRefUserMapper basePostRefUserMapper) {
        SignUtil.basePostRefUserMapper = basePostRefUserMapper;
    }

    /**
     * 发送验证码
     *
     * @param mustExist 是否必须存在，如果为 null，则，不存在和 存在都不会报错，例如：手机验证码注册并登录时
     */
    public static String sendCode(String key, @Nullable LambdaQueryChainWrapper<TempUserDO> lambdaQueryChainWrapper,
        @Nullable Boolean mustExist, IBizCode iBizCode, Consumer<String> consumer) {

        // 检查：账户是否存在
        checkAccountExistWillError(lambdaQueryChainWrapper, mustExist, iBizCode);

        String code = CodeUtil.getCode();

        consumer.accept(code); // 进行额外的处理

        // 保存到 redis中，设置 10分钟过期
        redissonClient.getBucket(key).set(code, Duration.ofMillis(TempConstant.LONG_CODE_EXPIRE_TIME));

        return TempBizCodeEnum.SEND_OK;

    }

    /**
     * 检查：账号是否存在
     *
     * @param mustExist 是否必须存在，如果为 null，则，不存在和 存在都不会报错，例如：手机验证码注册并登录时
     */
    @SneakyThrows
    public static void checkAccountExistWillError(@Nullable LambdaQueryChainWrapper<TempUserDO> lambdaQueryChainWrapper,
        @Nullable Boolean mustExist, IBizCode iBizCode) {

        // 判断是否存在
        boolean exists;

        if (lambdaQueryChainWrapper == null) {

            exists = true;

        } else {

            exists = lambdaQueryChainWrapper.exists();

        }

        if (mustExist == null) {
            return;
        }

        if (mustExist) {

            if (!exists) {
                R.error(iBizCode);
            }

        } else {

            if (exists) {
                R.error(iBizCode);
            }

        }

    }

    /**
     * 获取账户信息，并执行发送验证码操作
     */
    public static String getAccountAndSendCode(Enum<? extends IRedisKey> redisKeyEnum,
        VoidFunc2<String, String> voidFunc2) {

        // 获取：账号，通过：redisKeyEnum
        String account = getAccountByUserIdAndRedisKeyEnum(redisKeyEnum, MyUserUtil.getCurrentUserIdNotSuperAdmin());

        if (BaseRedisKeyEnum.PRE_EMAIL.equals(redisKeyEnum)) {

            if (StrUtil.isBlank(account)) {

                R.error(BaseBizCodeEnum.THIS_OPERATION_CANNOT_BE_PERFORMED_WITHOUT_BINDING_AN_EMAIL_ADDRESS);

            }

        } else if (BaseRedisKeyEnum.PRE_PHONE.equals(redisKeyEnum)) {

            if (StrUtil.isBlank(account)) {

                R.error(BaseBizCodeEnum.THERE_IS_NO_BOUND_MOBILE_PHONE_NUMBER_SO_THIS_OPERATION_CANNOT_BE_PERFORMED);

            }

        }

        String code = CodeUtil.getCode();

        // 保存到 redis中，设置 10分钟过期
        redissonClient.getBucket(redisKeyEnum + ":" + account)
            .set(code, Duration.ofMillis(TempConstant.LONG_CODE_EXPIRE_TIME));

        // 执行：发送验证码操作
        voidFunc2.call(code, account);

        return TempBizCodeEnum.SEND_OK;

    }

    /**
     * 绑定登录账号
     *
     * @param accountRedisKeyEnum 不能为 null
     */
    public static String bindAccount(@Nullable String code, Enum<? extends IRedisKey> accountRedisKeyEnum,
        String account, String appId, @Nullable String codeKey, @Nullable String password) {

        Long currentUserIdNotAdmin = MyUserUtil.getCurrentUserIdNotSuperAdmin();

        if (StrUtil.isNotBlank(password)) {

            // 检查密码是否正确
            checkCurrentPasswordWillError(password, currentUserIdNotAdmin, null);

        }

        String accountKey = accountRedisKeyEnum + ":" + account;

        boolean codeKeyBlankFlag = StrUtil.isBlank(codeKey);

        Set<String> nameSet = CollUtil.newHashSet(accountKey);

        if (!codeKeyBlankFlag) {

            nameSet.add(codeKey);

        }

        return RedissonUtil.doMultiLock(null, nameSet, () -> {

            RBucket<String> bucket = redissonClient.getBucket(codeKeyBlankFlag ? accountKey : codeKey);

            // 检查：绑定的登录账号是否存在
            boolean exist = accountIsExists(accountRedisKeyEnum, account, null, appId);

            boolean deleteRedisFlag = StrUtil.isNotBlank(code);

            if (exist) {

                if (deleteRedisFlag) {

                    bucket.delete();

                }

                R.errorMsg("操作失败：账号已被绑定，请重试");

            }

            if (deleteRedisFlag) {

                CodeUtil.checkCode(code, bucket.get()); // 检查 code是否正确

            }

            TempUserDO tempUserDO = new TempUserDO();

            // 通过：BaseRedisKeyEnum，设置：账号
            setTempUserDoAccountByRedisKeyEnum(accountRedisKeyEnum, account, tempUserDO, appId);

            tempUserDO.setId(currentUserIdNotAdmin);

            return TransactionUtil.exec(() -> {

                baseUserMapper.updateById(tempUserDO); // 保存：用户

                if (deleteRedisFlag) {

                    bucket.delete();

                }

                // 移除：jwt相关
                SignUtil.removeJwt(CollUtil.newHashSet(currentUserIdNotAdmin));

                return TempBizCodeEnum.OK;

            });

        });

    }

    /**
     * 注册
     */
    public static String signUp(String password, String originPassword, String code,
        Enum<? extends IRedisKey> redisKeyEnum, String account) {

        if (StrUtil.isNotBlank(password) && StrUtil.isNotBlank(originPassword)) {

            password = MyRsaUtil.rsaDecrypt(password);

            originPassword = MyRsaUtil.rsaDecrypt(originPassword);

            if (BooleanUtil.isFalse(ReUtil.isMatch(TempRegexConstant.PASSWORD_REGEXP, originPassword))) {

                R.error(BaseBizCodeEnum.PASSWORD_RESTRICTIONS); // 不合法直接抛出异常

            }

        }

        String key = redisKeyEnum + ":" + account;

        String finalPassword = password;

        RBucket<String> bucket = redissonClient.getBucket(key);

        boolean checkCodeFlag =
            BaseRedisKeyEnum.PRE_EMAIL.equals(redisKeyEnum) || BaseRedisKeyEnum.PRE_PHONE.equals(redisKeyEnum);

        return RedissonUtil.doLock(key, () -> {

            if (checkCodeFlag) {
                CodeUtil.checkCode(code, bucket.get()); // 检查 code是否正确
            }

            // 检查：注册的登录账号是否存在
            boolean exist = accountIsExists(redisKeyEnum, account, null, null);

            if (exist) {

                if (checkCodeFlag) {
                    bucket.delete(); // 删除：验证码
                }

                R.error(BaseBizCodeEnum.THE_ACCOUNT_HAS_ALREADY_BEEN_REGISTERED);

            }

            Map<Enum<? extends IRedisKey>, String> accountMap = MapUtil.newHashMap();

            accountMap.put(redisKeyEnum, account);

            // 新增：用户
            SignUtil.insertUser(finalPassword, accountMap, true, null, null);

            if (checkCodeFlag) {
                bucket.delete(); // 删除：验证码
            }

            return "注册成功";

        });

    }

    /**
     * 抛出：该账号已被注册，异常
     */
    public static void accountIsExistError() {

        R.error(BaseBizCodeEnum.THE_ACCOUNT_HAS_ALREADY_BEEN_REGISTERED);

    }

    /**
     * 新增：用户
     */
    @NotNull
    public static TempUserDO insertUser(String password, Map<Enum<? extends IRedisKey>, String> accountMap,
        boolean checkPasswordBlank, @Nullable TempUserInfoDO tempTempUserInfoDO, Boolean enableFlag) {

        // 获取：TempUserDO对象
        TempUserDO tempUserDO = insertUserGetTempUserDO(password, accountMap, checkPasswordBlank, enableFlag);

        return TransactionUtil.exec(() -> {

            baseUserMapper.insert(tempUserDO); // 保存：用户

            TempUserInfoDO tempUserInfoDO = new TempUserInfoDO();

            tempUserInfoDO.setId(tempUserDO.getId());

            tempUserInfoDO.setLastActiveTime(tempUserDO.getCreateTime());

            if (tempTempUserInfoDO == null) {

                tempUserInfoDO.setNickname(NicknameUtil.getRandomNickname());
                tempUserInfoDO.setBio("");

                tempUserInfoDO.setAvatarFileId(-1L);

                tempUserInfoDO.setSignUpType(RequestUtil.getRequestCategoryEnum());

                tempUserInfoDO.setLastIp(RequestUtil.getIp());

            } else {

                tempUserInfoDO.setNickname(
                    MyEntityUtil.getNotNullStr(tempTempUserInfoDO.getNickname(), NicknameUtil.getRandomNickname()));

                tempUserInfoDO.setBio(MyEntityUtil.getNotNullStr(tempTempUserInfoDO.getBio()));

                tempUserInfoDO.setAvatarFileId(MyEntityUtil.getNotNullLong(tempTempUserInfoDO.getAvatarFileId()));

                tempUserInfoDO.setSignUpType(MyEntityUtil.getNotNullObject(tempTempUserInfoDO.getSignUpType(),
                    RequestUtil.getRequestCategoryEnum()));

                tempUserInfoDO.setLastIp(
                    MyEntityUtil.getNotNullStr(tempTempUserInfoDO.getLastIp(), RequestUtil.getIp()));

            }

            tempUserInfoDO.setLastRegion("");

            baseUserInfoMapper.insert(tempUserInfoDO); // 保存：用户基本信息

            MyUserInfoUtil.add(tempUserDO.getId(), tempUserInfoDO.getLastActiveTime(), tempUserInfoDO.getLastIp(),
                null);

            return tempUserDO;

        });

    }

    /**
     * 获取：TempUserDO对象
     */
    @NotNull
    private static TempUserDO insertUserGetTempUserDO(String password,
        Map<Enum<? extends IRedisKey>, String> accountMap, boolean checkPasswordBlank, Boolean enableFlag) {

        TempUserDO tempUserDO = new TempUserDO();

        if (enableFlag == null) {
            tempUserDO.setEnableFlag(true);
        } else {
            tempUserDO.setEnableFlag(enableFlag);
        }

        tempUserDO.setEmail("");
        tempUserDO.setUsername("");
        tempUserDO.setPhone("");
        tempUserDO.setWxOpenId("");
        tempUserDO.setWxAppId("");
        tempUserDO.setWxUnionId("");
        tempUserDO.setRemark("");

        for (Map.Entry<Enum<? extends IRedisKey>, String> item : accountMap.entrySet()) {

            if (BaseRedisKeyEnum.PRE_EMAIL.equals(item.getKey())) {

                tempUserDO.setEmail(item.getValue());

            } else if (BaseRedisKeyEnum.PRE_USER_NAME.equals(item.getKey())) {

                tempUserDO.setUsername(item.getValue());

            } else if (BaseRedisKeyEnum.PRE_PHONE.equals(item.getKey())) {

                tempUserDO.setPhone(item.getValue());

            } else if (BaseRedisKeyEnum.PRE_WX_APP_ID.equals(item.getKey())) {

                tempUserDO.setWxAppId(item.getValue());

            } else if (BaseRedisKeyEnum.PRE_WX_OPEN_ID.equals(item.getKey())) {

                tempUserDO.setWxOpenId(item.getValue());

            } else if (BaseRedisKeyEnum.PRE_WX_UNION_ID.equals(item.getKey())) {

                tempUserDO.setWxUnionId(item.getValue());

            }

        }

        tempUserDO.setPassword(PasswordConvertUtil.convert(password, checkPasswordBlank));

        return tempUserDO;

    }

    /**
     * 检查登录账号是否存在
     */
    public static boolean accountIsExists(Enum<? extends IRedisKey> redisKeyEnum, String newAccount, @Nullable Long id,
        String appId) {

        LambdaQueryChainWrapper<TempUserDO> lambdaQueryChainWrapper =
            ChainWrappers.lambdaQueryChain(baseUserMapper).ne(id != null, TempEntity::getId, id);

        if (BaseRedisKeyEnum.PRE_EMAIL.equals(redisKeyEnum)) {

            lambdaQueryChainWrapper.eq(TempUserDO::getEmail, newAccount);

        } else if (BaseRedisKeyEnum.PRE_USER_NAME.equals(redisKeyEnum)) {

            lambdaQueryChainWrapper.eq(TempUserDO::getUsername, newAccount);

        } else if (BaseRedisKeyEnum.PRE_PHONE.equals(redisKeyEnum)) {

            lambdaQueryChainWrapper.eq(TempUserDO::getPhone, newAccount);

        } else if (BaseRedisKeyEnum.PRE_WX_OPEN_ID.equals(redisKeyEnum)) {

            lambdaQueryChainWrapper.eq(TempUserDO::getWxAppId, appId).eq(TempUserDO::getWxOpenId, newAccount);

        } else {

            R.sysError();

        }

        return lambdaQueryChainWrapper.exists();

    }

    /**
     * 账号密码登录
     */
    public static SignInVO signInPassword(LambdaQueryChainWrapper<TempUserDO> lambdaQueryChainWrapper, String password,
        BaseRequestCategoryEnum baseRequestCategoryEnum) {

        // 密码解密
        password = MyRsaUtil.rsaDecrypt(password);

        // 登录时，获取账号信息
        TempUserDO tempUserDO = signInGetTempUserDO(lambdaQueryChainWrapper, true);

        if (tempUserDO == null || StrUtil.isBlank(tempUserDO.getPassword())) {
            R.error(BaseBizCodeEnum.NO_PASSWORD_SET); // 未设置密码，请点击【忘记密码】，进行密码设置
        }

        if (BooleanUtil.isFalse(PasswordConvertUtil.match(tempUserDO.getPassword(), password))) {

            // 密码输入错误处理
            passwordErrorHandlerWillError(tempUserDO.getId());

        }

        // 登录时，获取：jwt
        return signInGetJwt(tempUserDO, true, baseRequestCategoryEnum, true);

    }

    /**
     * 登录时，获取：jwt
     */
    @Nullable
    public static SignInVO signInGetJwt(TempUserDO tempUserDO, boolean generateRefreshTokenFlag,
        BaseRequestCategoryEnum baseRequestCategoryEnum, boolean removePasswordErrorFlag) {

        // 校验密码，成功之后，再判断是否被冻结，免得透露用户被封号的信息
        if (BooleanUtil.isFalse(MyUserUtil.getCurrentUserSuperAdminFlag(tempUserDO.getId())) && BooleanUtil.isFalse(
            tempUserDO.getEnableFlag())) {

            R.error(TempBizCodeEnum.ACCOUNT_IS_DISABLED);

        }

        // 颁发，并返回 jwt
        return BaseJwtUtil.generateJwt(tempUserDO.getId(), payloadMap -> {

            payloadMap.set(MyJwtUtil.PAYLOAD_MAP_WX_APP_ID_KEY, tempUserDO.getWxAppId());

            payloadMap.set(MyJwtUtil.PAYLOAD_MAP_WX_OPEN_ID_KEY, tempUserDO.getWxOpenId());

        }, generateRefreshTokenFlag, baseRequestCategoryEnum, null, removePasswordErrorFlag);

    }

    /**
     * 密码错误次数过多，直接锁定账号，可以进行【忘记密码】操作，解除锁定
     */
    private static void passwordErrorHandlerWillError(Long userId) {

        if (userId == null) {
            R.sysError();
        }

        RAtomicLong atomicLong =
            redissonClient.getAtomicLong(BaseRedisKeyEnum.PRE_PASSWORD_ERROR_COUNT.name() + ":" + userId);

        long count = atomicLong.incrementAndGet(); // 次数 + 1

        if (count == 1) {
            atomicLong.expire(Duration.ofMillis(TempConstant.DAY_30_EXPIRE_TIME)); // 等于 1表示，是第一次访问，则设置过期时间
        }

        if (count >= 10) {

            // 超过十次密码错误，则封禁账号
            redissonClient.<String>getBucket(BaseRedisKeyEnum.PRE_TOO_MANY_PASSWORD_ERROR.name() + ":" + userId)
                .set("密码错误次数过多，被锁定的账号");

            atomicLong.delete(); // 清空错误次数

            R.error(BaseBizCodeEnum.TOO_MANY_PASSWORD_ERROR);

        }

        R.error(BaseBizCodeEnum.ACCOUNT_OR_PASSWORD_NOT_VALID);

    }

    /**
     * 登录时，获取：账号信息
     */
    @Nullable
    private static TempUserDO signInGetTempUserDO(LambdaQueryChainWrapper<TempUserDO> lambdaQueryChainWrapper,
        boolean errorFlag) {

        TempUserDO tempUserDO = lambdaQueryChainWrapper.one();

        // 账户是否存在
        if (tempUserDO == null) {

            if (errorFlag) {

                R.error(BaseBizCodeEnum.ACCOUNT_OR_PASSWORD_NOT_VALID);

            } else {

                return null;

            }

        }

        // 判断：密码错误次数过多
        checkTooManyPasswordWillError(tempUserDO.getId());

        return tempUserDO;

    }

    /**
     * 判断：密码错误次数过多
     */
    public static void checkTooManyPasswordWillError(Long userId) {

        boolean exists =
            redissonClient.getBucket(BaseRedisKeyEnum.PRE_TOO_MANY_PASSWORD_ERROR.name() + ":" + userId).isExists();

        if (exists) {
            R.error(BaseBizCodeEnum.TOO_MANY_PASSWORD_ERROR);
        }

    }

    /**
     * 检查：是否可以进行操作：敏感操作都需要调用此方法
     *
     * @param baseRedisKeyEnum    操作账户的类型：用户名，邮箱，微信，手机号
     * @param account             账号信息，一般情况为 null，只有：忘记密码，才会传值
     * @param allowSuperAdminFlag 是否允许超级管理员进行操作操作
     */
    public static void checkWillError(BaseRedisKeyEnum baseRedisKeyEnum, @Nullable String account,
        @Nullable String appId, boolean allowSuperAdminFlag) {

        Long userId = null;

        boolean accountBlankFlag = StrUtil.isBlank(account);

        if (accountBlankFlag) {

            if (allowSuperAdminFlag) {

                userId = MyUserUtil.getCurrentUserId();

            } else {

                userId = MyUserUtil.getCurrentUserIdNotSuperAdmin();

            }

        }

        // 敏感操作：
        // 1 设置或者修改：密码，用户名，邮箱，手机，微信
        // 2 忘记密码
        // 3 账户注销
        LambdaQueryChainWrapper<TempUserDO> lambdaQueryChainWrapper =
            ChainWrappers.lambdaQueryChain(baseUserMapper).eq(accountBlankFlag, TempEntity::getId, userId);

        // 处理：lambdaQueryChainWrapper对象
        checkWillErrorHandleLambdaQueryChainWrapper(baseRedisKeyEnum, account, appId, lambdaQueryChainWrapper,
            accountBlankFlag);

        TempUserDO tempUserDO = lambdaQueryChainWrapper.one();

        if (tempUserDO == null) {
            return;
        }

        // 执行：检查
        checkWillErrorDoCheck(baseRedisKeyEnum, tempUserDO);

    }

    /**
     * 处理：lambdaQueryChainWrapper对象
     */
    private static void checkWillErrorHandleLambdaQueryChainWrapper(BaseRedisKeyEnum baseRedisKeyEnum, String account,
        String appId, LambdaQueryChainWrapper<TempUserDO> lambdaQueryChainWrapper, boolean accountBlankFlag) {

        if (accountBlankFlag) {
            return;
        }

        if (baseRedisKeyEnum.equals(BaseRedisKeyEnum.PRE_USER_NAME)) { // 用户名

            lambdaQueryChainWrapper.eq(TempUserDO::getUsername, account);

        } else if (baseRedisKeyEnum.equals(BaseRedisKeyEnum.PRE_EMAIL)) { // 邮箱

            lambdaQueryChainWrapper.eq(TempUserDO::getEmail, account);

        } else if (baseRedisKeyEnum.equals(BaseRedisKeyEnum.PRE_WX_OPEN_ID)) { // 微信

            if (StrUtil.isBlank(appId)) {
                R.errorMsg(TempBizCodeEnum.ILLEGAL_REQUEST.getMsg() + "：wxAppId" + "，请联系管理员");
            }

            lambdaQueryChainWrapper.eq(TempUserDO::getWxAppId, appId).eq(TempUserDO::getWxOpenId, account);

        } else if (baseRedisKeyEnum.equals(BaseRedisKeyEnum.PRE_PHONE)) { // 手机

            lambdaQueryChainWrapper.eq(TempUserDO::getPhone, account);

        } else {

            R.errorMsg(TempBizCodeEnum.ILLEGAL_REQUEST.getMsg() + "：" + baseRedisKeyEnum.name() + "，请联系管理员");

        }

    }

    /**
     * 执行：检查
     */
    private static void checkWillErrorDoCheck(BaseRedisKeyEnum baseRedisKeyEnum, TempUserDO tempUserDO) {

        if (baseRedisKeyEnum.equals(BaseRedisKeyEnum.PRE_USER_NAME)) { // 用户名

            // 必须有密码，并且，邮箱为空，手机为空，微信为空
            if (StrUtil.isBlank(tempUserDO.getPassword())) {
                R.errorMsg("操作失败：请设置密码之后再试");
            }

            if (StrUtil.isNotBlank(tempUserDO.getEmail())) {
                R.errorMsg("操作失败：请用邮箱验证码进行操作");
            }

            if (StrUtil.isNotBlank(tempUserDO.getWxAppId())) {
                R.errorMsg("操作失败：请用微信扫码进行操作");
            }

            if (StrUtil.isNotBlank(tempUserDO.getPhone())) {
                R.errorMsg("操作失败：请用手机验证码进行操作");
            }

        } else if (baseRedisKeyEnum.equals(BaseRedisKeyEnum.PRE_EMAIL)) { // 邮箱

            if (StrUtil.isNotBlank(tempUserDO.getWxAppId())) {
                R.errorMsg("操作失败：请用微信扫码进行操作");
            }

            if (StrUtil.isNotBlank(tempUserDO.getPhone())) {
                R.errorMsg("操作失败：请用手机验证码进行操作");
            }

        } else if (baseRedisKeyEnum.equals(BaseRedisKeyEnum.PRE_WX_OPEN_ID)) { // 微信

            // 必须手机为空
            if (StrUtil.isNotBlank(tempUserDO.getPhone())) {
                R.errorMsg("操作失败：请用手机验证码进行操作");
            }

        } else if (baseRedisKeyEnum.equals(BaseRedisKeyEnum.PRE_PHONE)) { // 手机

            // 目前：手机支持任何操作

        }

    }

    /**
     * 修改密码
     */
    public static String updatePassword(String newPasswordTemp, String originNewPasswordTemp,
        Enum<? extends IRedisKey> redisKeyEnum, String code, String oldPassword) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        if (BaseRedisKeyEnum.PRE_USER_NAME.equals(redisKeyEnum)) {

            // 检查：当前密码是否正确
            checkCurrentPasswordWillError(oldPassword, currentUserId, null);

        }

        String newPassword = MyRsaUtil.rsaDecrypt(newPasswordTemp);
        String originNewPassword = MyRsaUtil.rsaDecrypt(originNewPasswordTemp);

        if (BooleanUtil.isFalse(ReUtil.isMatch(TempRegexConstant.PASSWORD_REGEXP, originNewPassword))) {
            R.error(BaseBizCodeEnum.PASSWORD_RESTRICTIONS); // 不合法直接抛出异常
        }

        // 获取：账号
        String account = getAccountByUserIdAndRedisKeyEnum(redisKeyEnum, currentUserId);

        String key = redisKeyEnum + ":" + account;

        return RedissonUtil.doLock(key, () -> {

            RBucket<String> bucket = redissonClient.getBucket(key);

            // 是否检查：验证码
            boolean checkCodeFlag = getDeleteRedisFlag(redisKeyEnum);

            if (checkCodeFlag) {
                CodeUtil.checkCode(code, bucket.get()); // 检查 code是否正确
            }

            TempUserDO tempUserDO = new TempUserDO();

            tempUserDO.setId(currentUserId);

            tempUserDO.setPassword(PasswordConvertUtil.convert(newPassword, true));

            return TransactionUtil.exec(() -> {

                baseUserMapper.updateById(tempUserDO); // 保存：用户

                RedissonUtil.batch((batch) -> {

                    // 移除密码错误次数相关
                    batch.getBucket(BaseRedisKeyEnum.PRE_PASSWORD_ERROR_COUNT.name() + ":" + currentUserId)
                        .deleteAsync();
                    batch.getBucket(BaseRedisKeyEnum.PRE_TOO_MANY_PASSWORD_ERROR.name() + ":" + currentUserId)
                        .deleteAsync();

                    if (checkCodeFlag) {
                        batch.getBucket(key).deleteAsync(); // 删除：验证码
                    }

                });

                // 移除：jwt相关
                SignUtil.removeJwt(CollUtil.newHashSet(currentUserId));

                return TempBizCodeEnum.OK;

            });

        });

    }

    /**
     * 是否删除：redis中的验证码
     */
    private static boolean getDeleteRedisFlag(Enum<? extends IRedisKey> redisKeyEnum) {

        return BaseRedisKeyEnum.PRE_EMAIL.equals(redisKeyEnum) || BaseRedisKeyEnum.PRE_PHONE.equals(redisKeyEnum);

    }

    /**
     * 检查：当前密码是否正确
     *
     * @param checkPassword 前端传过来的密码
     * @param userPassword  用户，数据库里面的密码
     */
    public static void checkCurrentPasswordWillError(String checkPassword, Long currentUserIdNotAdmin,
        @Nullable String userPassword) {

        if (StrUtil.isBlank(checkPassword)) {

            R.error(TempBizCodeEnum.PARAMETER_CHECK_ERROR);

        }

        // 判断：密码错误次数过多，是否被冻结
        checkTooManyPasswordWillError(currentUserIdNotAdmin);

        if (StrUtil.isBlank(userPassword)) {

            TempUserDO tempUserDO =
                ChainWrappers.lambdaQueryChain(baseUserMapper).eq(TempEntity::getId, currentUserIdNotAdmin)
                    .select(TempUserDO::getPassword).one();

            if (tempUserDO == null) {
                R.error(BaseBizCodeEnum.USER_DOES_NOT_EXIST);
            }

            userPassword = tempUserDO.getPassword();

        }

        checkPassword = MyRsaUtil.rsaDecrypt(checkPassword);

        if (BooleanUtil.isFalse(PasswordConvertUtil.match(userPassword, checkPassword))) {

            // 密码输入错误处理
            passwordErrorHandlerWillError(currentUserIdNotAdmin);

        }

    }

    /**
     * 获取：账号，通过：redisKeyEnum
     */
    @NotNull
    private static String getAccountByUserIdAndRedisKeyEnum(Enum<? extends IRedisKey> redisKeyEnum,
        Long currentUserIdNotAdmin) {

        // 获取：用户信息
        TempUserDO tempUserDO = getTempUserDoByIdAndRedisKeyEnum(redisKeyEnum, currentUserIdNotAdmin);

        if (BaseRedisKeyEnum.PRE_EMAIL.equals(redisKeyEnum)) {

            return tempUserDO.getEmail();

        } else if (BaseRedisKeyEnum.PRE_USER_NAME.equals(redisKeyEnum)) {

            return tempUserDO.getUsername();

        } else if (BaseRedisKeyEnum.PRE_PHONE.equals(redisKeyEnum)) {

            return tempUserDO.getPhone();

        } else if (BaseRedisKeyEnum.PRE_WX_OPEN_ID.equals(redisKeyEnum)) {

            return tempUserDO.getWxOpenId();

        } else {

            R.sysError();

            return null; // 这里不会执行，只是为了通过语法检查

        }

    }

    /**
     * 获取：TempUserDO，通过：userId和 redisKeyEnum
     */
    @NotNull
    private static TempUserDO getTempUserDoByIdAndRedisKeyEnum(Enum<? extends IRedisKey> redisKeyEnum,
        Long currentUserIdNotAdmin) {

        LambdaQueryChainWrapper<TempUserDO> lambdaQueryChainWrapper =
            ChainWrappers.lambdaQueryChain(baseUserMapper).eq(TempEntity::getId, currentUserIdNotAdmin);

        if (BaseRedisKeyEnum.PRE_EMAIL.equals(redisKeyEnum)) {

            lambdaQueryChainWrapper.select(TempUserDO::getEmail);

        } else if (BaseRedisKeyEnum.PRE_USER_NAME.equals(redisKeyEnum)) {

            lambdaQueryChainWrapper.select(TempUserDO::getUsername);

        } else if (BaseRedisKeyEnum.PRE_PHONE.equals(redisKeyEnum)) {

            lambdaQueryChainWrapper.select(TempUserDO::getPhone);

        } else if (BaseRedisKeyEnum.PRE_WX_OPEN_ID.equals(redisKeyEnum)) {

            lambdaQueryChainWrapper.select(TempUserDO::getWxOpenId, TempUserDO::getWxAppId);

        } else {

            R.sysError();

        }

        TempUserDO tempUserDO = lambdaQueryChainWrapper.one();

        if (tempUserDO == null) {
            R.error(BaseBizCodeEnum.USER_DOES_NOT_EXIST);
        }

        return tempUserDO;

    }

    /**
     * 修改登录账号
     *
     * @param oldRedisKeyEnum 这个参数不能为 null
     * @param newRedisKeyEnum 这个参数不能为 null
     */
    public static String updateAccount(String oldCode, String newCode, Enum<? extends IRedisKey> oldRedisKeyEnum,
        Enum<? extends IRedisKey> newRedisKeyEnum, String newAccount, String currentPassword, String appId) {

        Long currentUserIdNotAdmin = MyUserUtil.getCurrentUserIdNotSuperAdmin();

        if (BaseRedisKeyEnum.PRE_USER_NAME.equals(newRedisKeyEnum)) {
            checkCurrentPasswordWillError(currentPassword, currentUserIdNotAdmin, null);
        }

        // 获取：旧的账号
        String oldAccount = getAccountByUserIdAndRedisKeyEnum(oldRedisKeyEnum, currentUserIdNotAdmin);

        String oldKey = oldRedisKeyEnum.name() + ":" + oldAccount;
        String newKey = newRedisKeyEnum.name() + ":" + newAccount;

        return RedissonUtil.doMultiLock(null, CollUtil.newHashSet(oldKey, newKey), () -> {

            RBucket<String> oldBucket = redissonClient.getBucket(oldKey);

            if (BaseRedisKeyEnum.PRE_EMAIL.equals(oldRedisKeyEnum)) {

                // 检查 code是否正确
                CodeUtil.checkCode(oldCode, oldBucket.get(), "操作失败：请先获取旧邮箱的验证码",
                    "旧邮箱的验证码有误，请重新输入");

            } else if (BaseRedisKeyEnum.PRE_PHONE.equals(oldRedisKeyEnum)) {

                // 检查 code是否正确
                CodeUtil.checkCode(oldCode, oldBucket.get(), "操作失败：请先获取旧手机的验证码",
                    "旧手机的验证码有误，请重新输入");

            }

            RBucket<String> newBucket = redissonClient.getBucket(newKey);

            if (BaseRedisKeyEnum.PRE_EMAIL.equals(newRedisKeyEnum)) {

                // 检查 code是否正确
                CodeUtil.checkCode(newCode, newBucket.get(), "操作失败：请先获取新邮箱的验证码",
                    "新邮箱的验证码有误，请重新输入");

            } else if (BaseRedisKeyEnum.PRE_PHONE.equals(newRedisKeyEnum)) {

                // 检查 code是否正确
                CodeUtil.checkCode(newCode, newBucket.get(), "操作失败：请先获取新手机的验证码",
                    "新手机的验证码有误，请重新输入");

            }

            // 检查：新的登录账号是否存在
            boolean exist = accountIsExists(newRedisKeyEnum, newAccount, null, appId);

            // 是否删除：redis中的验证码
            boolean oldDeleteRedisFlag = getDeleteRedisFlag(oldRedisKeyEnum);

            boolean newDeleteRedisFlag = getDeleteRedisFlag(newRedisKeyEnum);

            if (exist) {

                if (newDeleteRedisFlag) {
                    newBucket.delete();
                }

                if (BaseRedisKeyEnum.PRE_EMAIL.equals(newRedisKeyEnum)) {

                    R.errorMsg("操作失败：邮箱已被人占用");

                } else if (BaseRedisKeyEnum.PRE_PHONE.equals(newRedisKeyEnum)) {

                    R.errorMsg("操作失败：手机号码已被人占用");

                } else {

                    R.errorMsg("操作失败：账号已被人占用");

                }

            }

            TempUserDO tempUserDO = new TempUserDO();

            tempUserDO.setId(currentUserIdNotAdmin);

            // 通过：BaseRedisKeyEnum，设置：账号
            setTempUserDoAccountByRedisKeyEnum(newRedisKeyEnum, newAccount, tempUserDO, appId);

            return TransactionUtil.exec(() -> {

                baseUserMapper.updateById(tempUserDO); // 更新：用户

                if (oldDeleteRedisFlag) {

                    // 删除：验证码
                    oldBucket.delete();

                }

                if (newDeleteRedisFlag) {

                    // 删除：验证码
                    newBucket.delete();

                }

                // 移除：jwt相关
                SignUtil.removeJwt(CollUtil.newHashSet(currentUserIdNotAdmin));

                return TempBizCodeEnum.OK;

            });

        });

    }

    /**
     * 通过：BaseRedisKeyEnum，设置：账号
     */
    private static void setTempUserDoAccountByRedisKeyEnum(Enum<? extends IRedisKey> redisKeyEnum, String newAccount,
        TempUserDO tempUserDO, String appId) {

        if (BaseRedisKeyEnum.PRE_EMAIL.equals(redisKeyEnum)) {

            tempUserDO.setEmail(newAccount);

        } else if (BaseRedisKeyEnum.PRE_USER_NAME.equals(redisKeyEnum)) {

            tempUserDO.setUsername(newAccount);

        } else if (BaseRedisKeyEnum.PRE_PHONE.equals(redisKeyEnum)) {

            tempUserDO.setPhone(newAccount);

        } else if (BaseRedisKeyEnum.PRE_WX_OPEN_ID.equals(redisKeyEnum)) {

            tempUserDO.setWxAppId(appId);
            tempUserDO.setWxOpenId(newAccount);

        } else {

            R.sysError();

        }

    }

    /**
     * 账号注销
     */
    public static String signDelete(@Nullable String code, Enum<? extends IRedisKey> redisKeyEnum,
        String currentPassword, @Nullable Long userId) {

        if (userId == null) {
            userId = MyUserUtil.getCurrentUserIdNotSuperAdmin();
        }

        if (StrUtil.isNotBlank(currentPassword)) {
            checkCurrentPasswordWillError(currentPassword, userId, null);
        }

        // 通过：redisKeyEnum，获取账号
        String account = getAccountByUserIdAndRedisKeyEnum(redisKeyEnum, userId);

        String key = redisKeyEnum + ":" + account;

        Long finalUserId = userId;

        return RedissonUtil.doLock(key, () -> {

            RBucket<String> bucket = redissonClient.getBucket(key);

            // 是否：检查验证码
            boolean deleteRedisFlag = getDeleteRedisFlag(redisKeyEnum);

            if (deleteRedisFlag) {
                CodeUtil.checkCode(code, bucket.get()); // 检查 code是否正确
            }

            return TransactionUtil.exec(() -> {

                // 执行：账号注销
                doSignDelete(CollUtil.newHashSet(finalUserId));

                if (deleteRedisFlag) {
                    bucket.delete(); // 删除：验证码
                }

                return TempBizCodeEnum.OK;

            });

        });

    }

    /**
     * 执行：账号注销
     */
    public static void doSignDelete(Set<Long> userIdSet) {

        if (CollUtil.isEmpty(userIdSet)) {
            return;
        }

        TransactionUtil.exec(() -> {

            doSignDeleteSub(userIdSet, true); // 删除子表数据

            baseUserMapper.deleteByIds(userIdSet); // 直接：删除用户

            // 删除：jwt相关
            removeJwt(userIdSet);

            // 删除：冻结相关
            MyUserUtil.removeDisable(userIdSet);

            // 删除：后台登录相关
            MyUserUtil.removeManageSignInFlag(userIdSet);

            BaseRoleServiceImpl.deleteAuthCache(userIdSet); // 删除权限缓存

            BaseRoleServiceImpl.deleteMenuCache(userIdSet); // 删除菜单缓存

        });

    }

    /**
     * 删除：jwt相关
     */
    public static void removeJwt(Set<Long> userIdSet) {

        if (CollUtil.isEmpty(userIdSet)) {
            return;
        }

        RBatch rBatch = redissonClient.createBatch();

        RKeysAsync rKeysAsync = rBatch.getKeys();

        for (Long item : userIdSet) {

            String jwtRefreshTokenRedisPreKey = TempRedisKeyEnum.PRE_JWT_REFRESH_TOKEN + ":" + item + ":*";

            String jwtRedisPreKey = TempRedisKeyEnum.PRE_JWT + ":" + item + ":*";

            rKeysAsync.deleteByPatternAsync(jwtRefreshTokenRedisPreKey);

            rKeysAsync.deleteByPatternAsync(jwtRedisPreKey);

        }

        rBatch.execute();

    }

    /**
     * 执行：账号注销，删除子表数据
     *
     * @param deleteFlag true 账号注销，需要删除用户相关的数据 false 修改用户的基础绑定信息
     */
    public static void doSignDeleteSub(Set<Long> userIdSet, boolean deleteFlag) {

        TransactionUtil.exec(() -> {

            if (deleteFlag) {

                if (CollUtil.isNotEmpty(iUserSignConfigurationList) && CollUtil.isNotEmpty(userIdSet)) {

                    for (IUserSignConfiguration item : iUserSignConfigurationList) {

                        item.delete(userIdSet); // 移除：用户额外的数据

                    }

                }

                // 直接：删除用户基本信息
                ChainWrappers.lambdaUpdateChain(baseUserInfoMapper).in(TempUserInfoDO::getId, userIdSet).remove();

            }

            // 直接：删除用户绑定的角色
            ChainWrappers.lambdaUpdateChain(baseRoleRefUserMapper).in(BaseRoleRefUserDO::getUserId, userIdSet).remove();

            // 直接：删除用户绑定的部门
            ChainWrappers.lambdaUpdateChain(baseDeptRefUserMapper).in(BaseDeptRefUserDO::getUserId, userIdSet).remove();

            // 直接：删除用户绑定的岗位
            ChainWrappers.lambdaUpdateChain(basePostRefUserMapper).in(BasePostRefUserDO::getUserId, userIdSet).remove();

        });

    }

    /**
     * 获取：微信，二维码地址
     */
    @SneakyThrows
    @Nullable
    public static GetQrCodeVO getQrCodeUrlWx(boolean getQrCodeUrlFlag, IBaseQrCodeSceneType iBaseQrCodeSceneType) {

        // 执行
        return getQrCodeUrl(getQrCodeUrlFlag, BaseThirdAppTypeEnum.WX_OFFICIAL.getCode(), baseThirdAppDO -> {

            String accessToken = WxUtil.getAccessToken(baseThirdAppDO.getAppId());

            Long qrCodeId = IdGeneratorUtil.nextId();

            String qrCodeUrl = WxUtil.getQrCodeUrl(accessToken, iBaseQrCodeSceneType, qrCodeId.toString());

            return new GetQrCodeVO(qrCodeUrl, qrCodeId,
                System.currentTimeMillis() + ((iBaseQrCodeSceneType.getExpireSecond() - 10) * 1000L));

        }, null);

    }

    /**
     * 获取：二维码地址
     */
    @SneakyThrows
    @Nullable
    public static GetQrCodeVO getQrCodeUrl(boolean getQrCodeUrlFlag, @Nullable Integer thirdAppType,
        Func1<BaseThirdAppDO, GetQrCodeVO> func1,
        @Nullable Consumer<LambdaQueryChainWrapper<BaseThirdAppDO>> lambdaQueryChainWrapperConsumer) {

        if (thirdAppType == null) {
            thirdAppType = BaseThirdAppTypeEnum.WX_OFFICIAL.getCode();
        }

        LambdaQueryChainWrapper<BaseThirdAppDO> lambdaQueryChainWrapper =
            ChainWrappers.lambdaQueryChain(baseThirdAppMapper).eq(BaseThirdAppDO::getType, thirdAppType)
                .eq(TempEntityNoId::getEnableFlag, true);

        if (lambdaQueryChainWrapperConsumer != null) {

            lambdaQueryChainWrapperConsumer.accept(lambdaQueryChainWrapper);

        }

        Page<BaseThirdAppDO> page =
            lambdaQueryChainWrapper.select(BaseThirdAppDO::getAppId).page(MyPageUtil.getLimit1Page());

        if (CollUtil.isEmpty(page.getRecords())) {
            return null;
        }

        if (!getQrCodeUrlFlag) {
            return new GetQrCodeVO(); // 这里回复一个对象，然后前端可以根据这个值，和前面的 null值进行判断，是否要进一步获取二维码地址，原因：二维码地址获取速度慢
        }

        BaseThirdAppDO baseThirdAppDO = page.getRecords().get(0);

        return func1.call(baseThirdAppDO);

    }

    /**
     * 获取：微信绑定信息
     */
    @SneakyThrows
    @NotNull
    public static BaseQrCodeSceneBindVO getBaseQrCodeSceneBindVoAndHandle(Long qrCodeId, boolean deleteFlag,
        @Nullable VoidFunc1<BaseQrCodeSceneBindBO> voidFunc1) {

        // 执行
        return execGetBaseQrCodeSceneBindVoAndHandle(qrCodeId, deleteFlag, BaseRedisKeyEnum.PRE_BASE_WX_QR_CODE_BIND,
            voidFunc1);

    }

    /**
     * 获取：微信绑定信息
     */
    @SneakyThrows
    @NotNull
    public static BaseQrCodeSceneBindVO execGetBaseQrCodeSceneBindVoAndHandle(Long qrCodeId, boolean deleteFlag,
        BaseRedisKeyEnum baseRedisKeyEnum, @Nullable VoidFunc1<BaseQrCodeSceneBindBO> voidFunc1) {

        RBucket<BaseQrCodeSceneBindBO> rBucket = redissonClient.getBucket(baseRedisKeyEnum.name() + qrCodeId);

        BaseQrCodeSceneBindBO baseQrCodeSceneBindBO;

        if (deleteFlag) {

            baseQrCodeSceneBindBO = rBucket.getAndDelete();

        } else {

            baseQrCodeSceneBindBO = rBucket.get();

        }

        BaseQrCodeSceneBindVO baseQrCodeSceneBindVO = new BaseQrCodeSceneBindVO();

        if (baseQrCodeSceneBindBO == null) {

            baseQrCodeSceneBindVO.setSceneFlag(false);

        } else {

            baseQrCodeSceneBindVO.setSceneFlag(true);

            Long qrCodeUserId = baseQrCodeSceneBindBO.getUserId();

            if (qrCodeUserId == null) { // 如果：不存在用户，则开始绑定

                if (voidFunc1 != null) {

                    voidFunc1.call(baseQrCodeSceneBindBO);

                }

            } else {

                Long currentUserIdNotAdmin = MyUserUtil.getCurrentUserIdNotSuperAdmin();

                if (currentUserIdNotAdmin.equals(qrCodeUserId)) {

                    baseQrCodeSceneBindVO.setErrorMsg("操作失败：您已绑定该微信，请勿重复绑定");

                } else {

                    baseQrCodeSceneBindVO.setErrorMsg("操作失败：该微信已被绑定");

                }

            }

        }

        return baseQrCodeSceneBindVO;

    }

    /**
     * 绑定微信
     */
    @NotNull
    public static BaseQrCodeSceneBindVO setWx(Long qrCodeId, String code, String codeKey, String currentPassword) {

        // 执行
        return getBaseQrCodeSceneBindVoAndHandle(qrCodeId, true, baseQrCodeSceneBindBO -> {

            // 执行
            SignUtil.bindAccount(code, BaseRedisKeyEnum.PRE_WX_OPEN_ID, baseQrCodeSceneBindBO.getOpenId(),
                baseQrCodeSceneBindBO.getAppId(), codeKey, currentPassword);

        });

    }

    /**
     * 忘记密码
     */
    public static String forgetPassword(String newPasswordTemp, String originNewPasswordTemp, String code,
        Enum<? extends IRedisKey> redisKeyEnum, String account,
        LambdaQueryChainWrapper<TempUserDO> lambdaQueryChainWrapper) {

        String newPassword = MyRsaUtil.rsaDecrypt(newPasswordTemp);
        String originNewPassword = MyRsaUtil.rsaDecrypt(originNewPasswordTemp);

        if (BooleanUtil.isFalse(ReUtil.isMatch(TempRegexConstant.PASSWORD_REGEXP, originNewPassword))) {
            R.error(BaseBizCodeEnum.PASSWORD_RESTRICTIONS); // 不合法直接抛出异常
        }

        String key = redisKeyEnum.name() + account;

        return RedissonUtil.doLock(key, () -> {

            RBucket<String> bucket = redissonClient.getBucket(key);

            CodeUtil.checkCode(code, bucket.get()); // 检查 code是否正确

            // 获取：用户 id
            TempUserDO tempUserDO = lambdaQueryChainWrapper.select(TempEntity::getId).one();

            if (tempUserDO == null) {

                bucket.delete(); // 删除：验证码
                R.error(BaseBizCodeEnum.USER_DOES_NOT_EXIST);

            }

            tempUserDO.setPassword(PasswordConvertUtil.convert(newPassword, true));

            return TransactionUtil.exec(() -> {

                baseUserMapper.updateById(tempUserDO); // 保存：用户

                RedissonUtil.batch((batch) -> {

                    // 移除密码错误次数相关
                    batch.getBucket(BaseRedisKeyEnum.PRE_PASSWORD_ERROR_COUNT.name() + ":" + tempUserDO.getId())
                        .deleteAsync();

                    batch.getMap(BaseRedisKeyEnum.PRE_TOO_MANY_PASSWORD_ERROR.name()).removeAsync(tempUserDO.getId());

                    // 删除：验证码
                    batch.getBucket(key).deleteAsync();

                });

                // 移除：jwt相关
                SignUtil.removeJwt(CollUtil.newHashSet(tempUserDO.getId()));

                return TempBizCodeEnum.OK;

            });

        });

    }

    /**
     * 获取：已经绑定了微信的用户。进行扫码操作
     */
    @SneakyThrows
    @NotNull
    public static BaseQrCodeSceneBindVO getBaseQrCodeSceneBindVoAndHandleForUserId(Long qrCodeId, boolean deleteFlag,
        BaseRedisKeyEnum baseRedisKeyEnum, @Nullable VoidFunc0 voidFunc0) {

        RBucket<Long> bucket = redissonClient.getBucket(baseRedisKeyEnum.name() + qrCodeId);

        Long userId;

        if (deleteFlag) {

            userId = bucket.getAndDelete();

        } else {

            userId = bucket.get();

        }

        BaseQrCodeSceneBindVO baseQrCodeSceneBindVO = new BaseQrCodeSceneBindVO();

        if (userId == null) {

            baseQrCodeSceneBindVO.setSceneFlag(false);

        } else {

            baseQrCodeSceneBindVO.setSceneFlag(true);

            Long currentUserIdNotAdmin = MyUserUtil.getCurrentUserIdNotSuperAdmin();

            if (!userId.equals(currentUserIdNotAdmin)) {

                R.errorMsg("操作失败：扫码用户不是当前用户，请重新进行扫码操作");

            }

            if (voidFunc0 != null) {

                voidFunc0.call();

            }

        }

        return baseQrCodeSceneBindVO;

    }

    /**
     * 验证码登录
     */
    @Nullable
    public static SignInVO signInCode(LambdaQueryChainWrapper<TempUserDO> lambdaQueryChainWrapper, String code,
        Enum<? extends IRedisKey> redisKeyEnum, String account, @Nullable VoidFunc0 voidFunc0,
        BaseRequestCategoryEnum baseRequestCategoryEnum) {

        // 登录时，获取账号信息
        TempUserDO tempUserDO = signInGetTempUserDO(lambdaQueryChainWrapper, false);

        String key = redisKeyEnum + ":" + account;

        return RedissonUtil.doLock(key, () -> {

            // 执行
            return doSignInCode(code, redisKeyEnum, account, voidFunc0, key, tempUserDO, baseRequestCategoryEnum);

        });

    }

    /**
     * 执行：验证码登录
     */
    @SneakyThrows
    @Nullable
    private static SignInVO doSignInCode(String code, Enum<? extends IRedisKey> redisKeyEnum, String account,
        @Nullable VoidFunc0 voidFunc0, String key, TempUserDO tempUserDoTemp,
        BaseRequestCategoryEnum baseRequestCategoryEnum) {

        RBucket<String> bucket = redissonClient.getBucket(key);

        CodeUtil.checkCode(code, bucket.get()); // 检查 code是否正确

        TempUserDO tempUserDO = tempUserDoTemp;

        if (tempUserDO == null) {

            if (voidFunc0 != null) {

                voidFunc0.call();

            }

            // 如果登录的账号不存在，则进行新增
            Map<Enum<? extends IRedisKey>, String> accountMap = MapUtil.newHashMap();

            accountMap.put(redisKeyEnum, account);

            tempUserDO = SignUtil.insertUser(null, accountMap, false, null, null);

        }

        bucket.delete(); // 删除：验证码

        // 登录时，获取：jwt
        return signInGetJwt(tempUserDO, true, baseRequestCategoryEnum, true);

    }

}
