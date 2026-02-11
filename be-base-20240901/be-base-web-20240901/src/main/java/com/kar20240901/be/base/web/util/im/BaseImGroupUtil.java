package com.kar20240901.be.base.web.util.im;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.im.BaseImGroupMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImGroupRefUserMapper;
import com.kar20240901.be.base.web.model.domain.im.BaseImGroupDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImGroupRefUserDO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class BaseImGroupUtil {

    private static BaseImGroupMapper baseImGroupMapper;

    @Resource
    public void setBaseImGroupMapper(BaseImGroupMapper baseImGroupMapper) {
        BaseImGroupUtil.baseImGroupMapper = baseImGroupMapper;
    }

    private static BaseImGroupRefUserMapper baseImGroupRefUserMapper;

    @Resource
    public void setBaseImGroupRefUserMapper(BaseImGroupRefUserMapper baseImGroupRefUserMapper) {
        BaseImGroupUtil.baseImGroupRefUserMapper = baseImGroupRefUserMapper;
    }

    /**
     * 检查：是否有权限
     *
     * @param onlyCreateFlag 是否是只能群主进行操作
     */
    public static void checkGroupIdSetAuth(Set<Long> groupIdSet, boolean onlyCreateFlag) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        Long count = ChainWrappers.lambdaQueryChain(baseImGroupMapper).eq(BaseImGroupDO::getBelongId, currentUserId)
            .in(BaseImGroupDO::getId, groupIdSet).count();

        if (groupIdSet.size() == count) {
            return;
        }

        if (onlyCreateFlag) {
            R.error("操作失败：只能群主进行该操作", groupIdSet);
        }

        count =
            ChainWrappers.lambdaQueryChain(baseImGroupRefUserMapper).eq(BaseImGroupRefUserDO::getUserId, currentUserId)
                .eq(BaseImGroupRefUserDO::getManageFlag, true).in(BaseImGroupRefUserDO::getGroupId, groupIdSet).count();

        if (count != groupIdSet.size()) {
            R.error("操作失败：只能群管理员进行该操作", groupIdSet);
        }

    }

    /**
     * 获取：是否是群主
     */
    public static boolean getGroupCreateFlag(Long groupId) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        return ChainWrappers.lambdaQueryChain(baseImGroupMapper).eq(BaseImGroupDO::getBelongId, currentUserId)
            .eq(BaseImGroupDO::getId, groupId).exists();

    }

    /**
     * 检查：是否有权限
     *
     * @param onlyCreateFlag 是否是只能群主进行操作
     */
    public static boolean checkGroupAuth(Long groupId, boolean onlyCreateFlag) {

        // 获取：是否是群主
        boolean groupCreateFlag = getGroupCreateFlag(groupId);

        if (groupCreateFlag) {
            return groupCreateFlag;
        }

        if (onlyCreateFlag) {
            R.error("操作失败：只能群主进行该操作", groupId);
        }

        Long currentUserId = MyUserUtil.getCurrentUserId();

        boolean exists =
            ChainWrappers.lambdaQueryChain(baseImGroupRefUserMapper).eq(BaseImGroupRefUserDO::getUserId, currentUserId)
                .eq(BaseImGroupRefUserDO::getManageFlag, true).eq(BaseImGroupRefUserDO::getGroupId, groupId).exists();

        if (!exists) {
            R.error("操作失败：只能群管理员进行该操作", groupId);
        }

        return groupCreateFlag;

    }

    /**
     * 检查：是否有权限，操作目标用户主键 id集合
     * <p>
     * 如果当前用户是普通用户，则无权限
     * <p>
     * 如果当前用户是管理员，则目标用户主键 id，不能是创建者和管理员
     * <p>
     * 如果当前用户是创建者，则一直有权限
     */
    public static void checkForTargetUserId(Long groupId, Set<Long> userIdSet) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        BaseImGroupDO baseImGroupDO =
            ChainWrappers.lambdaQueryChain(baseImGroupMapper).eq(BaseImGroupDO::getId, groupId)
                .select(BaseImGroupDO::getBelongId).one();

        if (baseImGroupDO == null) {
            R.error(TempBizCodeEnum.ILLEGAL_REQUEST, groupId);
        }

        if (currentUserId.equals(baseImGroupDO.getBelongId())) {
            return;
        }

        if (userIdSet.contains(baseImGroupDO.getBelongId())) {
            R.error(TempBizCodeEnum.ILLEGAL_REQUEST, groupId);
        }

        if (userIdSet.contains(currentUserId)) {
            R.error(TempBizCodeEnum.ILLEGAL_REQUEST, groupId);
        }

        BaseImGroupRefUserDO baseImGroupRefUserDO =
            ChainWrappers.lambdaQueryChain(baseImGroupRefUserMapper).eq(BaseImGroupRefUserDO::getUserId, currentUserId)
                .eq(BaseImGroupRefUserDO::getGroupId, groupId).select(BaseImGroupRefUserDO::getManageFlag).one();

        if (baseImGroupRefUserDO == null) {
            R.error(TempBizCodeEnum.ILLEGAL_REQUEST, groupId);
        }

        if (!baseImGroupRefUserDO.getManageFlag()) {
            R.error(TempBizCodeEnum.ILLEGAL_REQUEST, groupId);
        }

        boolean exists =
            ChainWrappers.lambdaQueryChain(baseImGroupRefUserMapper).in(BaseImGroupRefUserDO::getUserId, userIdSet)
                .eq(BaseImGroupRefUserDO::getManageFlag, true).exists();

        if (exists) {
            R.error(TempBizCodeEnum.ILLEGAL_REQUEST, groupId);
        }

    }

    /**
     * 检查：是否禁言
     */
    public static void checkMuteFlag(Long groupId) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        BaseImGroupDO baseImGroupDO =
            ChainWrappers.lambdaQueryChain(baseImGroupMapper).eq(BaseImGroupDO::getId, groupId)
                .select(BaseImGroupDO::getNormalMuteFlag, BaseImGroupDO::getManageMuteFlag, BaseImGroupDO::getBelongId)
                .one();

        if (baseImGroupDO == null) {
            R.error(TempBizCodeEnum.ILLEGAL_REQUEST, groupId);
        }

        if (baseImGroupDO.getBelongId().equals(currentUserId)) {
            return;
        }

        BaseImGroupRefUserDO baseImGroupRefUserDO =
            ChainWrappers.lambdaQueryChain(baseImGroupRefUserMapper).eq(BaseImGroupRefUserDO::getUserId, currentUserId)
                .eq(BaseImGroupRefUserDO::getGroupId, groupId)
                .select(BaseImGroupRefUserDO::getManageFlag, BaseImGroupRefUserDO::getMuteFlag).one();

        if (baseImGroupRefUserDO == null) {
            R.error("操作失败：您不是该群成员，无法发送消息", groupId);
        }

        if (baseImGroupRefUserDO.getMuteFlag()) {
            R.error("操作失败：您已被禁言，无法发送消息", groupId);
        }

        Boolean normalMuteFlag = baseImGroupDO.getNormalMuteFlag();

        Boolean manageMuteFlag = baseImGroupDO.getManageMuteFlag();

        Boolean manageFlag = baseImGroupRefUserDO.getManageFlag();

        boolean muteFlag;

        if (manageFlag) {

            muteFlag = BooleanUtil.isTrue(manageMuteFlag);

        } else {

            muteFlag = BooleanUtil.isTrue(normalMuteFlag);
        }

        if (muteFlag) {
            R.error("操作失败：您已被禁言，无法发送消息", groupId);
        }

    }

}
