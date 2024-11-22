package com.kar20240901.be.base.web.service.base.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.base.BaseUserMapper;
import com.kar20240901.be.base.web.model.domain.base.BaseUserConfigurationDO;
import com.kar20240901.be.base.web.model.domain.base.TempUserDO;
import com.kar20240901.be.base.web.model.dto.base.SignUserNameJwtRefreshTokenDTO;
import com.kar20240901.be.base.web.model.dto.base.SignUserNameSignDeleteDTO;
import com.kar20240901.be.base.web.model.dto.base.SignUserNameSignInPasswordDTO;
import com.kar20240901.be.base.web.model.dto.base.SignUserNameSignUpDTO;
import com.kar20240901.be.base.web.model.dto.base.SignUserNameUpdatePasswordDTO;
import com.kar20240901.be.base.web.model.dto.base.SignUserNameUpdateUserNameDTO;
import com.kar20240901.be.base.web.model.enums.base.BaseRedisKeyEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.base.SignInVO;
import com.kar20240901.be.base.web.service.base.BaseUserConfigurationService;
import com.kar20240901.be.base.web.service.base.SignUserNameService;
import com.kar20240901.be.base.web.util.base.BaseJwtUtil;
import com.kar20240901.be.base.web.util.base.MyJwtUtil;
import com.kar20240901.be.base.web.util.base.RequestUtil;
import com.kar20240901.be.base.web.util.base.SignUtil;
import javax.annotation.Resource;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
public class SignUserNameServiceImpl implements SignUserNameService {

    private static final BaseRedisKeyEnum PRE_REDIS_KEY_ENUM = BaseRedisKeyEnum.PRE_USER_NAME;

    @Resource
    BaseUserConfigurationService baseUserConfigurationService;

    @Resource
    BaseUserMapper baseUserMapper;

    @Resource
    RedissonClient redissonClient;

    /**
     * 注册
     */
    @Override
    public String signUp(SignUserNameSignUpDTO dto) {

        checkSignUpEnable(); // 检查：是否允许注册

        return SignUtil.signUp(dto.getPassword(), dto.getOriginPassword(), null, PRE_REDIS_KEY_ENUM, dto.getUsername());

    }

    /**
     * 检查：是否允许注册
     */
    private void checkSignUpEnable() {

        BaseUserConfigurationDO baseUserConfigurationDO = baseUserConfigurationService.getBaseUserConfigurationDo();

        if (BooleanUtil.isFalse(baseUserConfigurationDO.getUserNameSignUpEnable())) {
            R.errorMsg("操作失败：不允许用户名注册，请联系管理员");
        }

    }

    /**
     * 账号密码登录
     */
    @Override
    public SignInVO signInPassword(SignUserNameSignInPasswordDTO dto) {

        return SignUtil.signInPassword(
            ChainWrappers.lambdaQueryChain(baseUserMapper).eq(TempUserDO::getUsername, dto.getUsername()),
            dto.getPassword(), RequestUtil.getRequestCategoryEnum());

    }

    /**
     * 修改密码
     */
    @Override
    public String updatePassword(SignUserNameUpdatePasswordDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null); // 检查：是否可以进行操作

        return SignUtil.updatePassword(dto.getNewPassword(), dto.getOriginNewPassword(), PRE_REDIS_KEY_ENUM, null,
            dto.getOldPassword());

    }

    /**
     * 修改用户名
     */
    @Override
    public String updateUserName(SignUserNameUpdateUserNameDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null); // 检查：是否可以进行操作

        return SignUtil.updateAccount(null, null, PRE_REDIS_KEY_ENUM, PRE_REDIS_KEY_ENUM, dto.getNewUserName(),
            dto.getCurrentPassword(), null);

    }

    /**
     * 账号注销
     */
    @Override
    public String signDelete(SignUserNameSignDeleteDTO dto) {

        SignUtil.checkWillError(PRE_REDIS_KEY_ENUM, null, null); // 检查：是否可以进行操作

        return SignUtil.signDelete(null, PRE_REDIS_KEY_ENUM, dto.getCurrentPassword(), null);

    }

    /**
     * 刷新token
     */
    @Override
    public SignInVO jwtRefreshToken(SignUserNameJwtRefreshTokenDTO dto) {

        JWT jwtRefreshToken = JWT.of(dto.getJwtRefreshToken());

        JSONObject claimsJson = jwtRefreshToken.getPayload().getClaimsJson();

        // 获取：userId的值
        Long userId = MyJwtUtil.getPayloadMapUserIdValue(claimsJson);

        String jwtRefreshTokenRedisKey = MyJwtUtil.generateRedisJwtRefreshToken(dto.getJwtRefreshToken(), userId);

        boolean exists = redissonClient.<String>getBucket(jwtRefreshTokenRedisKey).isExists();

        if (!exists) {
            R.error(TempBizCodeEnum.LOGIN_EXPIRED);
        }

        return BaseJwtUtil.generateJwt(userId, null, false, RequestUtil.getRequestCategoryEnum(),
            dto.getJwtRefreshToken());

    }

}
