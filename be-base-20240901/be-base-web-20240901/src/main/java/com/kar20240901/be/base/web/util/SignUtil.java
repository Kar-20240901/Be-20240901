package com.kar20240901.be.base.web.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.BaseBizCodeEnum;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.BaseRoleRefUserMapper;
import com.kar20240901.be.base.web.mapper.BaseUserInfoMapper;
import com.kar20240901.be.base.web.mapper.BaseUserMapper;
import com.kar20240901.be.base.web.model.constant.TempConstant;
import com.kar20240901.be.base.web.model.constant.TempRegexConstant;
import com.kar20240901.be.base.web.model.domain.BaseRoleRefUserDO;
import com.kar20240901.be.base.web.model.domain.BaseUserDO;
import com.kar20240901.be.base.web.model.domain.BaseUserInfoDO;
import com.kar20240901.be.base.web.model.domain.TempEntity;
import com.kar20240901.be.base.web.model.enums.BaseRedisKeyEnum;
import com.kar20240901.be.base.web.model.enums.BaseRequestCategoryEnum;
import com.kar20240901.be.base.web.model.enums.TempRedisKeyEnum;
import com.kar20240901.be.base.web.model.interfaces.IRedisKey;
import com.kar20240901.be.base.web.model.vo.R;
import com.kar20240901.be.base.web.model.vo.SignInVO;
import com.kar20240901.be.base.web.properties.BaseSecurityProperties;
import java.time.Duration;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBatch;
import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
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
    public void setSecurityProperties(BaseSecurityProperties baseSecurityProperties) {
        SignUtil.baseSecurityProperties = baseSecurityProperties;
    }

    private static BaseRoleRefUserMapper baseRoleRefUserMapper;

    @Resource
    public void setBaseRoleUserMapper(BaseRoleRefUserMapper baseRoleRefUserMapper) {
        SignUtil.baseRoleRefUserMapper = baseRoleRefUserMapper;
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
    public static BaseUserDO insertUser(String password, Map<Enum<? extends IRedisKey>, String> accountMap,
        boolean checkPasswordBlank, @Nullable BaseUserInfoDO tempBaseUserInfoDO, Boolean enableFlag) {

        // 获取：TempUserDO对象
        BaseUserDO baseUserDO = insertUserGetTempUserDO(password, accountMap, checkPasswordBlank, enableFlag);

        return TransactionUtil.exec(() -> {

            baseUserMapper.insert(baseUserDO); // 保存：用户

            BaseUserInfoDO baseUserInfoDO = new BaseUserInfoDO();

            baseUserInfoDO.setId(baseUserDO.getId());

            baseUserInfoDO.setLastActiveTime(baseUserDO.getCreateTime());

            if (tempBaseUserInfoDO == null) {

                baseUserInfoDO.setNickname(NicknameUtil.getRandomNickname());
                baseUserInfoDO.setBio("");

                baseUserInfoDO.setAvatarFileId(-1L);

                baseUserInfoDO.setSignUpType(RequestUtil.getRequestCategoryEnum());

                baseUserInfoDO.setLastIp(RequestUtil.getIp());

            } else {

                baseUserInfoDO.setNickname(
                    MyEntityUtil.getNotNullStr(tempBaseUserInfoDO.getNickname(), NicknameUtil.getRandomNickname()));

                baseUserInfoDO.setBio(MyEntityUtil.getNotNullStr(tempBaseUserInfoDO.getBio()));

                baseUserInfoDO.setAvatarFileId(MyEntityUtil.getNotNullLong(tempBaseUserInfoDO.getAvatarFileId()));

                baseUserInfoDO.setSignUpType(MyEntityUtil.getNotNullObject(tempBaseUserInfoDO.getSignUpType(),
                    RequestUtil.getRequestCategoryEnum()));

                baseUserInfoDO.setLastIp(
                    MyEntityUtil.getNotNullStr(tempBaseUserInfoDO.getLastIp(), RequestUtil.getIp()));

            }

            baseUserInfoDO.setLastRegion("");

            baseUserInfoMapper.insert(baseUserInfoDO); // 保存：用户基本信息

            return baseUserDO;

        });

    }

    /**
     * 获取：TempUserDO对象
     */
    @NotNull
    private static BaseUserDO insertUserGetTempUserDO(String password,
        Map<Enum<? extends IRedisKey>, String> accountMap, boolean checkPasswordBlank, Boolean enableFlag) {

        BaseUserDO baseUserDO = new BaseUserDO();

        if (enableFlag == null) {
            baseUserDO.setEnableFlag(true);
        } else {
            baseUserDO.setEnableFlag(enableFlag);
        }

        baseUserDO.setEmail("");
        baseUserDO.setUsername("");
        baseUserDO.setPhone("");
        baseUserDO.setWxOpenId("");
        baseUserDO.setWxAppId("");
        baseUserDO.setWxUnionId("");
        baseUserDO.setRemark("");

        for (Map.Entry<Enum<? extends IRedisKey>, String> item : accountMap.entrySet()) {

            if (BaseRedisKeyEnum.PRE_EMAIL.equals(item.getKey())) {

                baseUserDO.setEmail(item.getValue());

            } else if (BaseRedisKeyEnum.PRE_USER_NAME.equals(item.getKey())) {

                baseUserDO.setUsername(item.getValue());

            } else if (BaseRedisKeyEnum.PRE_PHONE.equals(item.getKey())) {

                baseUserDO.setPhone(item.getValue());

            } else if (BaseRedisKeyEnum.PRE_WX_APP_ID.equals(item.getKey())) {

                baseUserDO.setWxAppId(item.getValue());

            } else if (BaseRedisKeyEnum.PRE_WX_OPEN_ID.equals(item.getKey())) {

                baseUserDO.setWxOpenId(item.getValue());

            } else if (BaseRedisKeyEnum.PRE_WX_UNION_ID.equals(item.getKey())) {

                baseUserDO.setWxUnionId(item.getValue());

            }

        }

        baseUserDO.setPassword(PasswordConvertUtil.convert(password, checkPasswordBlank));

        return baseUserDO;

    }

    /**
     * 检查登录账号是否存在
     */
    public static boolean accountIsExists(Enum<? extends IRedisKey> redisKeyEnum, String newAccount, @Nullable Long id,
        String appId) {

        LambdaQueryChainWrapper<BaseUserDO> lambdaQueryChainWrapper =
            ChainWrappers.lambdaQueryChain(baseUserMapper).ne(id != null, TempEntity::getId, id);

        if (BaseRedisKeyEnum.PRE_EMAIL.equals(redisKeyEnum)) {

            lambdaQueryChainWrapper.eq(BaseUserDO::getEmail, newAccount);

        } else if (BaseRedisKeyEnum.PRE_USER_NAME.equals(redisKeyEnum)) {

            lambdaQueryChainWrapper.eq(BaseUserDO::getUsername, newAccount);

        } else if (BaseRedisKeyEnum.PRE_PHONE.equals(redisKeyEnum)) {

            lambdaQueryChainWrapper.eq(BaseUserDO::getPhone, newAccount);

        } else if (BaseRedisKeyEnum.PRE_WX_OPEN_ID.equals(redisKeyEnum)) {

            lambdaQueryChainWrapper.eq(BaseUserDO::getWxAppId, appId).eq(BaseUserDO::getWxOpenId, newAccount);

        } else {

            R.sysError();

        }

        return lambdaQueryChainWrapper.exists();

    }

    /**
     * 账号密码登录
     */
    public static SignInVO signInPassword(LambdaQueryChainWrapper<BaseUserDO> lambdaQueryChainWrapper, String password,
        String account, BaseRequestCategoryEnum baseRequestCategoryEnum) {

        // 密码解密
        password = MyRsaUtil.rsaDecrypt(password);

        // 登录时，获取账号信息
        BaseUserDO baseUserDO = signInGetTempUserDO(lambdaQueryChainWrapper, true);

        if (baseUserDO == null || StrUtil.isBlank(baseUserDO.getPassword())) {
            R.error(BaseBizCodeEnum.NO_PASSWORD_SET); // 未设置密码，请点击【忘记密码】，进行密码设置
        }

        if (BooleanUtil.isFalse(PasswordConvertUtil.match(baseUserDO.getPassword(), password))) {

            // 密码输入错误处理
            passwordErrorHandlerWillError(baseUserDO.getId());

        }

        // 登录时，获取：jwt
        return signInGetJwt(baseUserDO, true, baseRequestCategoryEnum);

    }

    /**
     * 登录时，获取：jwt
     */
    @Nullable
    public static SignInVO signInGetJwt(BaseUserDO baseUserDO, boolean generateRefreshTokenFlag,
        BaseRequestCategoryEnum baseRequestCategoryEnum) {

        // 校验密码，成功之后，再判断是否被冻结，免得透露用户被封号的信息
        if (BooleanUtil.isFalse(baseUserDO.getEnableFlag())) {

            R.error(TempBizCodeEnum.ACCOUNT_IS_DISABLED);

        }

        // 颁发，并返回 jwt
        return BaseJwtUtil.generateJwt(baseUserDO.getId(), payloadMap -> {

            payloadMap.set(MyJwtUtil.PAYLOAD_MAP_WX_APP_ID_KEY, baseUserDO.getWxAppId());

            payloadMap.set(MyJwtUtil.PAYLOAD_MAP_WX_OPEN_ID_KEY, baseUserDO.getWxOpenId());

        }, generateRefreshTokenFlag, baseRequestCategoryEnum, null);

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
    private static BaseUserDO signInGetTempUserDO(LambdaQueryChainWrapper<BaseUserDO> lambdaQueryChainWrapper,
        boolean errorFlag) {

        BaseUserDO baseUserDO = lambdaQueryChainWrapper.one();

        // 账户是否存在
        if (baseUserDO == null) {

            if (errorFlag) {

                R.error(BaseBizCodeEnum.ACCOUNT_OR_PASSWORD_NOT_VALID);

            } else {

                return null;

            }

        }

        // 判断：密码错误次数过多
        checkTooManyPasswordWillError(baseUserDO.getId());

        return baseUserDO;

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
     * @param baseRedisKeyEnum 操作账户的类型：用户名，邮箱，微信，手机号
     * @param account          账号信息，一般情况为 null，只有：忘记密码，才会传值
     */
    public static void checkWillError(BaseRedisKeyEnum baseRedisKeyEnum, @Nullable String account,
        @Nullable String appId) {

        Long userId = null;

        boolean accountBlankFlag = StrUtil.isBlank(account);

        if (accountBlankFlag) {
            userId = MyUserUtil.getCurrentUserIdNotAdmin();
        }

        // 敏感操作：
        // 1 设置或者修改：密码，用户名，邮箱，手机，微信
        // 2 忘记密码
        // 3 账户注销
        LambdaQueryChainWrapper<BaseUserDO> lambdaQueryChainWrapper =
            ChainWrappers.lambdaQueryChain(baseUserMapper).eq(accountBlankFlag, TempEntity::getId, userId);

        // 处理：lambdaQueryChainWrapper对象
        checkWillErrorHandleLambdaQueryChainWrapper(baseRedisKeyEnum, account, appId, lambdaQueryChainWrapper,
            accountBlankFlag);

        BaseUserDO baseUserDO = lambdaQueryChainWrapper.one();

        if (baseUserDO == null) {
            return;
        }

        // 执行：检查
        checkWillErrorDoCheck(baseRedisKeyEnum, baseUserDO);

    }

    /**
     * 处理：lambdaQueryChainWrapper对象
     */
    private static void checkWillErrorHandleLambdaQueryChainWrapper(BaseRedisKeyEnum baseRedisKeyEnum, String account,
        String appId, LambdaQueryChainWrapper<BaseUserDO> lambdaQueryChainWrapper, boolean accountBlankFlag) {

        if (accountBlankFlag) {
            return;
        }

        if (baseRedisKeyEnum.equals(BaseRedisKeyEnum.PRE_USER_NAME)) { // 用户名

            lambdaQueryChainWrapper.eq(BaseUserDO::getUsername, account);

        } else if (baseRedisKeyEnum.equals(BaseRedisKeyEnum.PRE_EMAIL)) { // 邮箱

            lambdaQueryChainWrapper.eq(BaseUserDO::getEmail, account);

        } else if (baseRedisKeyEnum.equals(BaseRedisKeyEnum.PRE_WX_OPEN_ID)) { // 微信

            if (StrUtil.isBlank(appId)) {
                R.errorMsg(TempBizCodeEnum.ILLEGAL_REQUEST.getMsg() + "：wxAppId" + "，请联系管理员");
            }

            lambdaQueryChainWrapper.eq(BaseUserDO::getWxAppId, appId).eq(BaseUserDO::getWxOpenId, account);

        } else if (baseRedisKeyEnum.equals(BaseRedisKeyEnum.PRE_PHONE)) { // 手机

            lambdaQueryChainWrapper.eq(BaseUserDO::getPhone, account);

        } else {

            R.errorMsg(TempBizCodeEnum.ILLEGAL_REQUEST.getMsg() + "：" + baseRedisKeyEnum.name() + "，请联系管理员");

        }

    }

    /**
     * 执行：检查
     */
    private static void checkWillErrorDoCheck(BaseRedisKeyEnum baseRedisKeyEnum, BaseUserDO baseUserDO) {

        if (baseRedisKeyEnum.equals(BaseRedisKeyEnum.PRE_USER_NAME)) { // 用户名

            // 必须有密码，并且，邮箱为空，手机为空，微信为空
            if (StrUtil.isBlank(baseUserDO.getPassword())) {
                R.errorMsg("操作失败：请设置密码之后再试");
            }

            if (StrUtil.isNotBlank(baseUserDO.getEmail())) {
                R.errorMsg("操作失败：请用邮箱验证码进行操作");
            }

            if (StrUtil.isNotBlank(baseUserDO.getWxAppId())) {
                R.errorMsg("操作失败：请用微信扫码进行操作");
            }

            if (StrUtil.isNotBlank(baseUserDO.getPhone())) {
                R.errorMsg("操作失败：请用手机验证码进行操作");
            }

        } else if (baseRedisKeyEnum.equals(BaseRedisKeyEnum.PRE_EMAIL)) { // 邮箱

            // 必须有密码，并且，手机为空，微信为空
            if (StrUtil.isBlank(baseUserDO.getPassword())) {
                R.errorMsg("操作失败：请设置密码之后再试");
            }

            if (StrUtil.isNotBlank(baseUserDO.getWxAppId())) {
                R.errorMsg("操作失败：请用微信扫码进行操作");
            }

            if (StrUtil.isNotBlank(baseUserDO.getPhone())) {
                R.errorMsg("操作失败：请用手机验证码进行操作");
            }

        } else if (baseRedisKeyEnum.equals(BaseRedisKeyEnum.PRE_WX_OPEN_ID)) { // 微信

            // 必须手机为空
            if (StrUtil.isNotBlank(baseUserDO.getPhone())) {
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

        Long currentUserIdNotAdmin = MyUserUtil.getCurrentUserIdNotAdmin();

        if (BaseRedisKeyEnum.PRE_USER_NAME.equals(redisKeyEnum)) {

            // 检查：当前密码是否正确
            checkCurrentPasswordWillError(oldPassword, currentUserIdNotAdmin, null);

        }

        String newPassword = MyRsaUtil.rsaDecrypt(newPasswordTemp);
        String originNewPassword = MyRsaUtil.rsaDecrypt(originNewPasswordTemp);

        if (BooleanUtil.isFalse(ReUtil.isMatch(TempRegexConstant.PASSWORD_REGEXP, originNewPassword))) {
            R.error(BaseBizCodeEnum.PASSWORD_RESTRICTIONS); // 不合法直接抛出异常
        }

        // 获取：账号
        String account = getAccountByUserIdAndRedisKeyEnum(redisKeyEnum, currentUserIdNotAdmin);

        String key = redisKeyEnum + ":" + account;

        return RedissonUtil.doLock(key, () -> {

            RBucket<String> bucket = redissonClient.getBucket(key);

            // 是否检查：验证码
            boolean checkCodeFlag = getDeleteRedisFlag(redisKeyEnum);

            if (checkCodeFlag) {
                CodeUtil.checkCode(code, bucket.get()); // 检查 code是否正确
            }

            BaseUserDO baseUserDO = new BaseUserDO();

            baseUserDO.setId(currentUserIdNotAdmin);

            baseUserDO.setPassword(PasswordConvertUtil.convert(newPassword, true));

            return TransactionUtil.exec(() -> {

                baseUserMapper.updateById(baseUserDO); // 保存：用户

                RedissonUtil.batch((batch) -> {

                    // 移除密码错误次数相关
                    batch.getBucket(BaseRedisKeyEnum.PRE_PASSWORD_ERROR_COUNT.name() + ":" + currentUserIdNotAdmin)
                        .deleteAsync();
                    batch.getBucket(BaseRedisKeyEnum.PRE_TOO_MANY_PASSWORD_ERROR.name() + ":" + currentUserIdNotAdmin)
                        .deleteAsync();

                    if (checkCodeFlag) {
                        batch.getBucket(key).deleteAsync(); // 删除：验证码
                    }

                });

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

            BaseUserDO baseUserDO =
                ChainWrappers.lambdaQueryChain(baseUserMapper).eq(TempEntity::getId, currentUserIdNotAdmin)
                    .select(BaseUserDO::getPassword).one();

            if (baseUserDO == null) {
                R.error(BaseBizCodeEnum.USER_DOES_NOT_EXIST);
            }

            userPassword = baseUserDO.getPassword();

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
        BaseUserDO baseUserDO = getTempUserDoByIdAndRedisKeyEnum(redisKeyEnum, currentUserIdNotAdmin);

        if (BaseRedisKeyEnum.PRE_EMAIL.equals(redisKeyEnum)) {

            return baseUserDO.getEmail();

        } else if (BaseRedisKeyEnum.PRE_USER_NAME.equals(redisKeyEnum)) {

            return baseUserDO.getUsername();

        } else if (BaseRedisKeyEnum.PRE_PHONE.equals(redisKeyEnum)) {

            return baseUserDO.getPhone();

        } else if (BaseRedisKeyEnum.PRE_WX_OPEN_ID.equals(redisKeyEnum)) {

            return baseUserDO.getWxOpenId();

        } else {

            R.sysError();

            return null; // 这里不会执行，只是为了通过语法检查

        }

    }

    /**
     * 获取：BaseUserDO，通过：userId和 redisKeyEnum
     */
    @NotNull
    private static BaseUserDO getTempUserDoByIdAndRedisKeyEnum(Enum<? extends IRedisKey> redisKeyEnum,
        Long currentUserIdNotAdmin) {

        LambdaQueryChainWrapper<BaseUserDO> lambdaQueryChainWrapper =
            ChainWrappers.lambdaQueryChain(baseUserMapper).eq(TempEntity::getId, currentUserIdNotAdmin);

        if (BaseRedisKeyEnum.PRE_EMAIL.equals(redisKeyEnum)) {

            lambdaQueryChainWrapper.select(BaseUserDO::getEmail);

        } else if (BaseRedisKeyEnum.PRE_USER_NAME.equals(redisKeyEnum)) {

            lambdaQueryChainWrapper.select(BaseUserDO::getUsername);

        } else if (BaseRedisKeyEnum.PRE_PHONE.equals(redisKeyEnum)) {

            lambdaQueryChainWrapper.select(BaseUserDO::getPhone);

        } else if (BaseRedisKeyEnum.PRE_WX_OPEN_ID.equals(redisKeyEnum)) {

            lambdaQueryChainWrapper.select(BaseUserDO::getWxOpenId, BaseUserDO::getWxAppId);

        } else {

            R.sysError();

        }

        BaseUserDO baseUserDO = lambdaQueryChainWrapper.one();

        if (baseUserDO == null) {
            R.error(BaseBizCodeEnum.USER_DOES_NOT_EXIST);
        }

        return baseUserDO;

    }

    /**
     * 修改登录账号
     *
     * @param oldRedisKeyEnum 这个参数不能为 null
     * @param newRedisKeyEnum 这个参数不能为 null
     */
    public static String updateAccount(String oldCode, String newCode, Enum<? extends IRedisKey> oldRedisKeyEnum,
        Enum<? extends IRedisKey> newRedisKeyEnum, String newAccount, String currentPassword, String appId) {

        Long currentUserIdNotAdmin = MyUserUtil.getCurrentUserIdNotAdmin();

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

            BaseUserDO baseUserDO = new BaseUserDO();

            baseUserDO.setId(currentUserIdNotAdmin);

            // 通过：BaseRedisKeyEnum，设置：账号
            setTempUserDoAccountByRedisKeyEnum(newRedisKeyEnum, newAccount, baseUserDO, appId);

            return TransactionUtil.exec(() -> {

                baseUserMapper.updateById(baseUserDO); // 更新：用户

                if (oldDeleteRedisFlag) {

                    // 删除：验证码
                    oldBucket.delete();

                }

                if (newDeleteRedisFlag) {

                    // 删除：验证码
                    newBucket.delete();

                }

                return TempBizCodeEnum.OK;

            });

        });

    }

    /**
     * 通过：BaseRedisKeyEnum，设置：账号
     */
    private static void setTempUserDoAccountByRedisKeyEnum(Enum<? extends IRedisKey> redisKeyEnum, String newAccount,
        BaseUserDO baseUserDO, String appId) {

        if (BaseRedisKeyEnum.PRE_EMAIL.equals(redisKeyEnum)) {

            baseUserDO.setEmail(newAccount);

        } else if (BaseRedisKeyEnum.PRE_USER_NAME.equals(redisKeyEnum)) {

            baseUserDO.setUsername(newAccount);

        } else if (BaseRedisKeyEnum.PRE_PHONE.equals(redisKeyEnum)) {

            baseUserDO.setPhone(newAccount);

        } else if (BaseRedisKeyEnum.PRE_WX_OPEN_ID.equals(redisKeyEnum)) {

            baseUserDO.setWxAppId(appId);
            baseUserDO.setWxOpenId(newAccount);

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
            userId = MyUserUtil.getCurrentUserIdNotAdmin();
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

            // 删除：权限相关
            removeAuth(userIdSet);

        });

    }

    /**
     * 删除：权限相关
     */
    public static void removeAuth(Set<Long> userIdSet) {

        RKeys keys = redissonClient.getKeys();

        String[] redisKeyArr =
            userIdSet.stream().map(it -> TempRedisKeyEnum.PRE_USER_AUTH.name() + ":" + it).toArray(String[]::new);

        keys.delete(redisKeyArr);

    }

    /**
     * 删除：jwt相关
     */
    public static void removeJwt(Set<Long> userIdSet) {

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

                // 直接：删除用户基本信息
                ChainWrappers.lambdaUpdateChain(baseUserInfoMapper).in(BaseUserInfoDO::getId, userIdSet).remove();

            }

            // 直接：删除用户绑定的角色
            ChainWrappers.lambdaUpdateChain(baseRoleRefUserMapper).in(BaseRoleRefUserDO::getUserId, userIdSet).remove();

        });

    }

}
