package com.kar20240901.be.base.web.service.base.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.exception.base.BaseBizCodeEnum;
import com.kar20240901.be.base.web.mapper.base.BaseUserInfoMapper;
import com.kar20240901.be.base.web.mapper.base.BaseUserMapper;
import com.kar20240901.be.base.web.model.constant.base.ParamConstant;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.constant.base.TempRegexConstant;
import com.kar20240901.be.base.web.model.domain.base.BaseRoleRefUserDO;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.base.TempUserDO;
import com.kar20240901.be.base.web.model.domain.base.TempUserInfoDO;
import com.kar20240901.be.base.web.model.dto.base.BaseUserInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.base.BaseUserPageDTO;
import com.kar20240901.be.base.web.model.dto.base.BaseUserUpdatePasswordDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.enums.base.BaseRedisKeyEnum;
import com.kar20240901.be.base.web.model.interfaces.base.IRedisKey;
import com.kar20240901.be.base.web.model.vo.base.BaseUserPageVO;
import com.kar20240901.be.base.web.model.vo.base.DictVO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.base.TempUserInfoByIdVO;
import com.kar20240901.be.base.web.service.base.BaseRoleRefUserService;
import com.kar20240901.be.base.web.service.base.BaseUserService;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import com.kar20240901.be.base.web.util.base.MyMapUtil;
import com.kar20240901.be.base.web.util.base.MyParamUtil;
import com.kar20240901.be.base.web.util.base.MyRsaUtil;
import com.kar20240901.be.base.web.util.base.MyThreadUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.base.NicknameUtil;
import com.kar20240901.be.base.web.util.base.PasswordConvertUtil;
import com.kar20240901.be.base.web.util.base.RedissonUtil;
import com.kar20240901.be.base.web.util.base.SignUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
public class BaseUserServiceImpl extends ServiceImpl<BaseUserMapper, TempUserDO> implements BaseUserService {

    @Resource
    BaseRoleRefUserService baseRoleRefUserService;

    private static RedissonClient redissonClient;

    @Resource
    public void setRedissonClient(RedissonClient redissonClient) {
        BaseUserServiceImpl.redissonClient = redissonClient;
    }

    @Resource
    BaseUserInfoMapper baseUserInfoMapper;

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseUserPageVO> myPage(BaseUserPageDTO dto) {

        Page<BaseUserPageVO> dtoPage = dto.pageOrder(false);

        // 备注：mysql 是先 group by 再 order by
        Page<BaseUserPageVO> page = baseMapper.myPage(dtoPage, dto);

        for (BaseUserPageVO item : page.getRecords()) {

            // 备注：要和 userSelfInfo接口保持一致
            item.setEmail(DesensitizedUtil.email(item.getEmail())); // 脱敏
            item.setUsername(DesensitizedUtil.chineseName(item.getUsername())); // 脱敏
            item.setPhone(DesensitizedUtil.mobilePhone(item.getPhone())); // 脱敏
            item.setWxOpenId(StrUtil.hide(item.getWxOpenId(), 3, item.getWxOpenId().length() - 4)); // 脱敏：只显示前 3位，后 4位
            item.setWxAppId(StrUtil.hide(item.getWxAppId(), 3, item.getWxAppId().length() - 4)); // 脱敏：只显示前 3位，后 4位

        }

        return page;

    }

    /**
     * 获取：是否允许后台登录
     */
    public static boolean getManageSignInFlag(Long userId) {

        if (MyUserUtil.getCurrentUserAdminFlag()) {
            return true;
        }

        Boolean manageSignInFlag =
            redissonClient.<Boolean>getBucket(BaseRedisKeyEnum.PRE_USER_MANAGE_SIGN_IN_FLAG.name() + ":" + userId)
                .get();

        if (manageSignInFlag == null) {

            String defaultManageSignInFlagStr = MyParamUtil.getValueByUuid(ParamConstant.DEFAULT_MANAGE_SIGN_IN_FLAG);

            return Convert.toBool(defaultManageSignInFlagStr, false);

        }

        return manageSignInFlag;

    }

    /**
     * 下拉列表
     */
    @Override
    public Page<DictVO> dictList() {

        // 获取所有：用户信息
        List<TempUserInfoDO> tempUserInfoDOList = ChainWrappers.lambdaQueryChain(baseUserInfoMapper)
            .select(TempUserInfoDO::getId, TempUserInfoDO::getNickname).orderByDesc(TempUserInfoDO::getId).list();

        List<DictVO> dictVOList = tempUserInfoDOList.stream().map(it -> new DictVO(it.getId(), it.getNickname()))
            .collect(Collectors.toList());

        return new Page<DictVO>().setTotal(dictVOList.size()).setRecords(dictVOList);

    }

    /**
     * 新增/修改
     */
    @Override
    @DSTransactional
    public String insertOrUpdate(BaseUserInsertOrUpdateDTO dto) {

        boolean emailBlank = StrUtil.isBlank(dto.getEmail());
        boolean userNameBlank = StrUtil.isBlank(dto.getUsername());
        boolean phoneBlank = StrUtil.isBlank(dto.getPhone());
        boolean wxAppIdBlank = StrUtil.isBlank(dto.getWxAppId());
        boolean wxOpenIdBlank = StrUtil.isBlank(dto.getWxOpenId());

        if (emailBlank && userNameBlank && phoneBlank && wxAppIdBlank && wxOpenIdBlank) {
            R.error(BaseBizCodeEnum.ACCOUNT_CANNOT_BE_EMPTY);
        }

        if ((!wxAppIdBlank && wxOpenIdBlank) || (wxAppIdBlank && !wxOpenIdBlank)) {
            R.errorMsg("操作失败：微信appId和微信openId，必须都有值");
        }

        boolean passwordFlag = StrUtil.isNotBlank(dto.getPassword()) && StrUtil.isNotBlank(dto.getOriginPassword());

        if (dto.getId() == null && passwordFlag) { // 只有新增时，才可以设置密码

            // 处理密码
            insertOrUpdateHandlePassword(dto);

        }

        Set<Enum<? extends IRedisKey>> redisKeyEnumSet = CollUtil.newHashSet();

        if (!emailBlank) {
            redisKeyEnumSet.add(BaseRedisKeyEnum.PRE_EMAIL);
        }

        if (!userNameBlank) {
            redisKeyEnumSet.add(BaseRedisKeyEnum.PRE_USER_NAME);
        }

        if (!phoneBlank) {
            redisKeyEnumSet.add(BaseRedisKeyEnum.PRE_PHONE);
        }

        if (!wxAppIdBlank) {
            redisKeyEnumSet.add(BaseRedisKeyEnum.PRE_WX_APP_ID);
        }

        if (!wxOpenIdBlank) {
            redisKeyEnumSet.add(BaseRedisKeyEnum.PRE_WX_OPEN_ID);
        }

        // 执行
        Long userId = doInsertOrUpdate(dto, redisKeyEnumSet);

        BaseRoleServiceImpl.deleteAuthCache(CollUtil.newHashSet(userId)); // 删除权限缓存

        BaseRoleServiceImpl.deleteMenuCache(CollUtil.newHashSet(userId)); // 删除菜单缓存

        return TempBizCodeEnum.OK;

    }

    /**
     * 执行：新增/修改
     */
    private Long doInsertOrUpdate(BaseUserInsertOrUpdateDTO dto, Set<Enum<? extends IRedisKey>> redisKeyEnumSet) {

        return RedissonUtil.doMultiLock(null, redisKeyEnumSet, () -> {

            Map<Enum<? extends IRedisKey>, String> accountMap = MapUtil.newHashMap();

            for (Enum<? extends IRedisKey> item : redisKeyEnumSet) {

                // 检查：账号是否存在
                if (accountIsExist(dto, item, accountMap)) {

                    SignUtil.accountIsExistError();

                }

            }

            TempUserDO tempUserDO;

            if (dto.getId() == null) { // 新增：用户

                TempUserInfoDO tempUserInfoDO = new TempUserInfoDO();

                tempUserInfoDO.setNickname(dto.getNickname());
                tempUserInfoDO.setBio(dto.getBio());

                tempUserDO =
                    SignUtil.insertUser(dto.getPassword(), accountMap, false, tempUserInfoDO, dto.getEnableFlag());

                insertOrUpdateSub(tempUserDO, dto); // 新增数据到子表

            } else { // 修改：用户

                // 删除子表数据
                SignUtil.doSignDeleteSub(CollUtil.newHashSet(dto.getId()), false);

                tempUserDO = new TempUserDO();

                tempUserDO.setId(dto.getId());
                tempUserDO.setEnableFlag(BooleanUtil.isTrue(dto.getEnableFlag()));
                tempUserDO.setEmail(MyEntityUtil.getNotNullStr(dto.getEmail()));
                tempUserDO.setPhone(MyEntityUtil.getNotNullStr(dto.getPhone()));
                tempUserDO.setUsername(MyEntityUtil.getNotNullStr(dto.getUsername()));
                tempUserDO.setWxAppId(MyEntityUtil.getNotNullStr(dto.getWxAppId()));
                tempUserDO.setWxOpenId(MyEntityUtil.getNotNullStr(dto.getWxOpenId()));
                tempUserDO.setWxUnionId(MyEntityUtil.getNotNullStr(dto.getWxUnionId()));

                baseMapper.updateById(tempUserDO);

                // 新增数据到子表
                insertOrUpdateSub(tempUserDO, dto);

                TempUserInfoDO tempUserInfoDO = new TempUserInfoDO();

                tempUserInfoDO.setId(dto.getId());

                tempUserInfoDO.setNickname(
                    MyEntityUtil.getNotNullStr(dto.getNickname(), NicknameUtil.getRandomNickname()));

                tempUserInfoDO.setBio(MyEntityUtil.getNotNullStr(dto.getBio()));

                baseUserInfoMapper.updateById(tempUserInfoDO);

            }

            if (dto.getManageSignInFlag() != null) {

                // 设置
                redissonClient.<Boolean>getBucket(
                        BaseRedisKeyEnum.PRE_USER_MANAGE_SIGN_IN_FLAG.name() + ":" + tempUserDO.getId())
                    .set(dto.getManageSignInFlag());

            }

            return tempUserDO.getId();

        });

    }

    /**
     * 新增/修改：新增数据到子表
     */
    private void insertOrUpdateSub(TempUserDO tempUserDO, BaseUserInsertOrUpdateDTO dto) {

        if (BooleanUtil.isFalse(tempUserDO.getEnableFlag())) {

            MyUserUtil.setDisable(tempUserDO.getId()); // 设置：账号被冻结

        } else {

            MyUserUtil.removeDisable(tempUserDO.getId()); // 移除：账号被冻结

        }

        // 新增数据到：角色用户关联表
        if (CollUtil.isNotEmpty(dto.getRoleIdSet())) {

            List<BaseRoleRefUserDO> insertList =
                new ArrayList<>(MyMapUtil.getInitialCapacity(dto.getRoleIdSet().size()));

            for (Long item : dto.getRoleIdSet()) {

                BaseRoleRefUserDO baseRoleRefUserDO = new BaseRoleRefUserDO();

                baseRoleRefUserDO.setRoleId(item);
                baseRoleRefUserDO.setUserId(tempUserDO.getId());

                insertList.add(baseRoleRefUserDO);

            }

            baseRoleRefUserService.saveBatch(insertList);

        }

    }

    /**
     * 判断：账号是否重复
     */
    private boolean accountIsExist(BaseUserInsertOrUpdateDTO dto, Enum<? extends IRedisKey> item,
        Map<Enum<? extends IRedisKey>, String> map) {

        boolean exist = false;

        if (BaseRedisKeyEnum.PRE_EMAIL.equals(item)) {

            exist = SignUtil.accountIsExists(item, dto.getEmail(), dto.getId(), null);
            map.put(item, dto.getEmail());

        } else if (BaseRedisKeyEnum.PRE_USER_NAME.equals(item)) {

            exist = SignUtil.accountIsExists(item, dto.getUsername(), dto.getId(), null);
            map.put(item, dto.getUsername());

        } else if (BaseRedisKeyEnum.PRE_PHONE.equals(item)) {

            exist = SignUtil.accountIsExists(item, dto.getPhone(), dto.getId(), null);
            map.put(item, dto.getPhone());

        } else if (BaseRedisKeyEnum.PRE_WX_OPEN_ID.equals(item)) {

            exist = SignUtil.accountIsExists(item, dto.getWxOpenId(), dto.getId(), dto.getWxAppId());
            map.put(BaseRedisKeyEnum.PRE_WX_APP_ID, dto.getWxAppId());
            map.put(item, dto.getWxOpenId());

        }

        return exist;

    }

    /**
     * 处理密码
     */
    private void insertOrUpdateHandlePassword(BaseUserInsertOrUpdateDTO dto) {

        // 私钥
        dto.setOriginPassword(MyRsaUtil.rsaDecrypt(dto.getOriginPassword()));
        dto.setPassword(MyRsaUtil.rsaDecrypt(dto.getPassword()));

        if (BooleanUtil.isFalse(ReUtil.isMatch(TempRegexConstant.PASSWORD_REGEXP, dto.getOriginPassword()))) {

            R.error(BaseBizCodeEnum.PASSWORD_RESTRICTIONS); // 不合法直接抛出异常

        }

    }

    /**
     * 通过主键id，查看详情
     */
    @SneakyThrows
    @Override
    public TempUserInfoByIdVO infoById(NotNullId notNullId) {

        TempUserDO tempUserDO = lambdaQuery().eq(TempEntity::getId, notNullId.getId()).one();

        if (tempUserDO == null) {
            return null;
        }

        TempUserInfoByIdVO baseUserInfoByIdVO = BeanUtil.copyProperties(tempUserDO, TempUserInfoByIdVO.class);

        CountDownLatch countDownLatch = ThreadUtil.newCountDownLatch(2);

        MyThreadUtil.execute(() -> {

            TempUserInfoDO tempUserInfoDO =
                ChainWrappers.lambdaQueryChain(baseUserInfoMapper).eq(TempUserInfoDO::getId, notNullId.getId())
                    .select(TempUserInfoDO::getNickname, TempUserInfoDO::getAvatarFileId, TempUserInfoDO::getBio).one();

            baseUserInfoByIdVO.setNickname(tempUserInfoDO.getNickname());
            baseUserInfoByIdVO.setAvatarFileId(tempUserInfoDO.getAvatarFileId());
            baseUserInfoByIdVO.setBio(tempUserInfoDO.getBio());

            // 获取
            Boolean manageSignInFlag = getManageSignInFlag(tempUserDO.getId());

            baseUserInfoByIdVO.setManageSignInFlag(manageSignInFlag);

        }, countDownLatch);

        MyThreadUtil.execute(() -> {

            // 获取：用户绑定的角色 idSet
            List<BaseRoleRefUserDO> baseRoleRefUserDoList =
                baseRoleRefUserService.lambdaQuery().eq(BaseRoleRefUserDO::getUserId, notNullId.getId())
                    .select(BaseRoleRefUserDO::getRoleId).list();

            Set<Long> roleIdSet =
                baseRoleRefUserDoList.stream().map(BaseRoleRefUserDO::getRoleId).collect(Collectors.toSet());

            baseUserInfoByIdVO.setRoleIdSet(roleIdSet);

        }, countDownLatch);

        countDownLatch.await();

        return baseUserInfoByIdVO;

    }

    /**
     * 是否允许后台登录
     */
    @Override
    public Boolean manageSignInFlag() {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        if (MyUserUtil.getCurrentUserSuperAdminFlag(currentUserId)) {
            return true;
        }

        // 执行
        return getManageSignInFlag(currentUserId);

    }

    /**
     * 批量：注销用户
     */
    @Override
    @DSTransactional
    public String deleteByIdSet(NotEmptyIdSet notEmptyIdSet) {

        notEmptyIdSet.getIdSet().remove(TempConstant.ADMIN_ID);

        if (CollUtil.isEmpty(notEmptyIdSet.getIdSet())) {
            return TempBizCodeEnum.OK;
        }

        // 执行：账号注销
        SignUtil.doSignDelete(notEmptyIdSet.getIdSet());

        return TempBizCodeEnum.OK;

    }

    /**
     * 批量：重置头像
     */
    @Override
    @DSTransactional
    public String resetAvatar(NotEmptyIdSet notEmptyIdSet) {

        if (CollUtil.isEmpty(notEmptyIdSet.getIdSet())) {
            return TempBizCodeEnum.OK;
        }

        ChainWrappers.lambdaUpdateChain(baseUserInfoMapper).in(TempUserInfoDO::getId, notEmptyIdSet.getIdSet())
            .set(TempUserInfoDO::getAvatarFileId, -1).update();

        return TempBizCodeEnum.OK;

    }

    /**
     * 批量：修改密码
     */
    @Override
    @DSTransactional
    public String updatePassword(BaseUserUpdatePasswordDTO dto) {

        dto.getIdSet().remove(TempConstant.ADMIN_ID);

        if (CollUtil.isEmpty(dto.getIdSet())) {
            return TempBizCodeEnum.OK;
        }

        boolean passwordFlag =
            StrUtil.isNotBlank(dto.getNewPassword()) && StrUtil.isNotBlank(dto.getNewOriginPassword());

        String password = "";

        if (passwordFlag) {

            dto.setNewOriginPassword(MyRsaUtil.rsaDecrypt(dto.getNewOriginPassword()));

            dto.setNewPassword(MyRsaUtil.rsaDecrypt(dto.getNewPassword()));

            if (BooleanUtil.isFalse(ReUtil.isMatch(TempRegexConstant.PASSWORD_REGEXP, dto.getNewOriginPassword()))) {

                R.error(BaseBizCodeEnum.PASSWORD_RESTRICTIONS); // 不合法直接抛出异常

            }

            password = PasswordConvertUtil.convert(dto.getNewPassword(), true);

        }

        lambdaUpdate().in(TempEntity::getId, dto.getIdSet()).set(TempUserDO::getPassword, password).update();

        // 移除：jwt相关
        SignUtil.removeJwt(dto.getIdSet());

        return TempBizCodeEnum.OK;

    }

    /**
     * 批量：解冻
     */
    @Override
    @DSTransactional
    public String thaw(NotEmptyIdSet notEmptyIdSet) {

        notEmptyIdSet.getIdSet().remove(TempConstant.ADMIN_ID);

        if (CollUtil.isEmpty(notEmptyIdSet.getIdSet())) {
            return TempBizCodeEnum.OK;
        }

        lambdaUpdate().in(TempEntity::getId, notEmptyIdSet.getIdSet()).eq(TempUserDO::getEnableFlag, false)
            .set(TempUserDO::getEnableFlag, true).update();

        MyUserUtil.removeDisable(notEmptyIdSet.getIdSet()); // 设置：账号被冻结

        return TempBizCodeEnum.OK;

    }

    /**
     * 批量：冻结
     */
    @Override
    @DSTransactional
    public String freeze(NotEmptyIdSet notEmptyIdSet) {

        notEmptyIdSet.getIdSet().remove(TempConstant.ADMIN_ID);

        if (CollUtil.isEmpty(notEmptyIdSet.getIdSet())) {
            return TempBizCodeEnum.OK;
        }

        lambdaUpdate().in(TempEntity::getId, notEmptyIdSet.getIdSet()).eq(TempUserDO::getEnableFlag, true)
            .set(TempUserDO::getEnableFlag, false).update();

        MyUserUtil.setDisable(notEmptyIdSet.getIdSet()); // 设置：账号被冻结

        return TempBizCodeEnum.OK;

    }

    /**
     * 批量：退出登录
     */
    @Override
    public String signOutByIdSet(NotEmptyIdSet notEmptyIdSet) {

        if (CollUtil.isEmpty(notEmptyIdSet.getIdSet())) {
            return TempBizCodeEnum.OK;
        }

        // 移除：jwt
        SignUtil.removeJwt(notEmptyIdSet.getIdSet());

        return TempBizCodeEnum.OK;

    }

    /**
     * 全部退出登录
     */
    @Override
    public String signOutAll() {

        List<TempUserDO> tempUserDOList = lambdaQuery().select(TempEntity::getId).list();

        if (CollUtil.isEmpty(tempUserDOList)) {
            return TempBizCodeEnum.OK;
        }

        Set<Long> userIdSet = tempUserDOList.stream().map(TempEntity::getId).collect(Collectors.toSet());

        // 移除：jwt
        SignUtil.removeJwt(userIdSet);

        return TempBizCodeEnum.OK;

    }

}
