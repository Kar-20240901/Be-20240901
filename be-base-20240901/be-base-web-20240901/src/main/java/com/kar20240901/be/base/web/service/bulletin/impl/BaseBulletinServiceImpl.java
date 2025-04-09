package com.kar20240901.be.base.web.service.bulletin.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinMapper;
import com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinReadTimeRefUserMapper;
import com.kar20240901.be.base.web.model.bo.socket.BaseWebSocketEventBO;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoId;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoIdSuper;
import com.kar20240901.be.base.web.model.domain.bulletin.BaseBulletinDO;
import com.kar20240901.be.base.web.model.domain.bulletin.BaseBulletinReadTimeRefUserDO;
import com.kar20240901.be.base.web.model.dto.base.MyOrderDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.bulletin.BaseBulletinInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.bulletin.BaseBulletinPageDTO;
import com.kar20240901.be.base.web.model.dto.bulletin.BaseBulletinUserSelfPageDTO;
import com.kar20240901.be.base.web.model.dto.socket.WebSocketMessageDTO;
import com.kar20240901.be.base.web.model.enums.base.BaseRedisKeyEnum;
import com.kar20240901.be.base.web.model.enums.bulletin.BaseBulletinStatusEnum;
import com.kar20240901.be.base.web.model.enums.socket.BaseWebSocketUriEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.bulletin.BaseBulletinService;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.base.RedissonUtil;
import com.kar20240901.be.base.web.util.kafka.TempKafkaUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import javax.annotation.Resource;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class BaseBulletinServiceImpl extends ServiceImpl<BaseBulletinMapper, BaseBulletinDO>
    implements BaseBulletinService {

    @Resource
    BaseBulletinReadTimeRefUserMapper baseBulletinReadTimeRefUserMapper;

    /**
     * 新增/修改
     */
    @Override
    public String insertOrUpdate(BaseBulletinInsertOrUpdateDTO dto) {

        if (dto.getId() == null) {

            doInsertOrUpdate(dto);

        } else {

            RedissonUtil.doLock(BaseRedisKeyEnum.PRE_BULLETIN_ID + ":" + dto.getId(), () -> {

                doInsertOrUpdate(dto);

            });

        }

        return TempBizCodeEnum.OK;

    }

    /**
     * 执行：新增/修改
     */
    private void doInsertOrUpdate(BaseBulletinInsertOrUpdateDTO dto) {

        if (dto.getId() != null) {

            boolean exists = lambdaQuery().eq(BaseBulletinDO::getId, dto.getId())
                .eq(BaseBulletinDO::getStatus, BaseBulletinStatusEnum.DRAFT).exists();

            if (!exists) {
                R.errorMsg("操作失败：只能修改草稿状态的公告");
            }

        }

        BaseBulletinDO baseBulletinDO = new BaseBulletinDO();

        baseBulletinDO.setContent(dto.getContent());
        baseBulletinDO.setTitle(dto.getTitle());
        baseBulletinDO.setPublishTime(dto.getPublishTime());
        baseBulletinDO.setStatus(BaseBulletinStatusEnum.DRAFT);
        baseBulletinDO.setId(dto.getId());
        baseBulletinDO.setEnableFlag(dto.getEnableFlag());

        saveOrUpdate(baseBulletinDO);

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseBulletinDO> myPage(BaseBulletinPageDTO dto) {

        boolean userSelfFlag = BooleanUtil.isTrue(dto.getUserSelfFlag());

        ArrayList<SFunction<BaseBulletinDO, ?>> myPageSelectList = getMyPageSelectList();

        // 查询用户最新一条公告
        if (userSelfFlag && dto.getPageSize() == 1) {

            myPageSelectList.add(BaseBulletinDO::getContent);

        }

        return lambdaQuery().like(StrUtil.isNotBlank(dto.getTitle()), BaseBulletinDO::getTitle, dto.getTitle())
            .like(StrUtil.isNotBlank(dto.getContent()), BaseBulletinDO::getContent, dto.getContent())
            .le(dto.getPtEndTime() != null, BaseBulletinDO::getPublishTime, dto.getPtEndTime())
            .ge(dto.getPtBeginTime() != null, BaseBulletinDO::getPublishTime, dto.getPtBeginTime())
            .eq(dto.getEnableFlag() != null, TempEntityNoId::getEnableFlag, dto.getEnableFlag())
            .select(myPageSelectList).page(dto.updateTimeDescDefaultOrderPage());

    }

    /**
     * 获取：需要查询的字段
     */
    private static ArrayList<SFunction<BaseBulletinDO, ?>> getMyPageSelectList() {

        return CollUtil.newArrayList(TempEntity::getId, BaseBulletinDO::getTitle, BaseBulletinDO::getPublishTime);

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseBulletinDO infoById(NotNullId notNullId) {

        return lambdaQuery().eq(TempEntity::getId, notNullId.getId()).one();

    }

    /**
     * 批量删除
     */
    @Override
    public String deleteByIdSet(NotEmptyIdSet notEmptyIdSet) {

        RedissonUtil.doMultiLock(BaseRedisKeyEnum.PRE_BULLETIN_ID + ":", notEmptyIdSet.getIdSet(), () -> {

            lambdaUpdate().in(TempEntity::getId, notEmptyIdSet.getIdSet())
                .eq(BaseBulletinDO::getStatus, BaseBulletinStatusEnum.DRAFT).remove();

        });

        return TempBizCodeEnum.OK;

    }

    /**
     * 发布
     */
    @Override
    public String publish(NotEmptyIdSet notEmptyIdSet) {

        Date date = new Date();

        Long currentUserId = MyUserUtil.getCurrentUserId();

        RedissonUtil.doMultiLock(BaseRedisKeyEnum.PRE_BULLETIN_ID + ":", notEmptyIdSet.getIdSet(), () -> {

            lambdaUpdate().in(TempEntity::getId, notEmptyIdSet.getIdSet())
                .eq(BaseBulletinDO::getStatus, BaseBulletinStatusEnum.DRAFT).ge(BaseBulletinDO::getPublishTime, date)
                .set(BaseBulletinDO::getStatus, BaseBulletinStatusEnum.PUBLICITY)
                .set(BaseBulletinDO::getUpdateTime, date).set(TempEntityNoIdSuper::getUpdateId, currentUserId).update();

        });

        return TempBizCodeEnum.OK;

    }

    /**
     * 撤回
     */
    @Override
    public String revoke(NotEmptyIdSet notEmptyIdSet) {

        Date date = new Date();

        Long currentUserId = MyUserUtil.getCurrentUserId();

        RedissonUtil.doMultiLock(BaseRedisKeyEnum.PRE_BULLETIN_ID + ":", notEmptyIdSet.getIdSet(), () -> {

            lambdaUpdate().in(TempEntity::getId, notEmptyIdSet.getIdSet())
                .eq(BaseBulletinDO::getStatus, BaseBulletinStatusEnum.PUBLICITY)
                .ge(BaseBulletinDO::getPublishTime, date).set(BaseBulletinDO::getStatus, BaseBulletinStatusEnum.DRAFT)
                .set(BaseBulletinDO::getUpdateTime, date).set(TempEntityNoIdSuper::getUpdateId, currentUserId).update();

        });

        return TempBizCodeEnum.OK;

    }

    /**
     * 分页排序查询：当前用户可以查看的公告
     */
    @Override
    public Page<BaseBulletinDO> userSelfPage(BaseBulletinUserSelfPageDTO dto) {

        BaseBulletinPageDTO baseBulletinPageDTO = BeanUtil.copyProperties(dto, BaseBulletinPageDTO.class);

        // 只能查询：已公示的公告，并且已经到了发布时间的公告
        baseBulletinPageDTO.setStatus(BaseBulletinStatusEnum.PUBLICITY);

        baseBulletinPageDTO.setPtEndTime(new Date());

        baseBulletinPageDTO.setUserSelfFlag(true);

        baseBulletinPageDTO.setEnableFlag(true);

        // 根据发布时间，倒序
        MyOrderDTO myOrderDTO = new MyOrderDTO();

        myOrderDTO.setName("publishTime");

        myOrderDTO.setValue("descend");

        baseBulletinPageDTO.setOrder(myOrderDTO);

        return myPage(baseBulletinPageDTO);

    }

    /**
     * 当前用户查看公告详情
     */
    @Override
    public BaseBulletinDO userSelfInfoById(NotNullId notNullId) {

        Date date = new Date();

        return lambdaQuery().eq(TempEntity::getId, notNullId.getId()).le(BaseBulletinDO::getPublishTime, date)
            .eq(BaseBulletinDO::getStatus, BaseBulletinStatusEnum.PUBLICITY).eq(TempEntityNoId::getEnableFlag, true)
            .select(TempEntity::getId, BaseBulletinDO::getTitle, BaseBulletinDO::getPublishTime,
                BaseBulletinDO::getContent).one();

    }

    /**
     * 当前用户可以查看的公告总数
     */
    @Override
    public Long userSelfCount() {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        BaseBulletinReadTimeRefUserDO baseBulletinReadTimeRefUserDO =
            ChainWrappers.lambdaQueryChain(baseBulletinReadTimeRefUserMapper)
                .eq(BaseBulletinReadTimeRefUserDO::getUserId, currentUserId)
                .select(BaseBulletinReadTimeRefUserDO::getBulletinReadTime).one();

        Date date;

        if (baseBulletinReadTimeRefUserDO == null) {

            date = new Date();

            doUserSelfUpdateReadTime(currentUserId, date);

        } else {

            date = baseBulletinReadTimeRefUserDO.getBulletinReadTime();

        }

        return lambdaQuery().eq(TempEntityNoId::getEnableFlag, true)
            .eq(BaseBulletinDO::getStatus, BaseBulletinStatusEnum.PUBLICITY).le(BaseBulletinDO::getPublishTime, date)
            .count();

    }

    /**
     * 当前用户更新公告最近查看时间
     */
    @Override
    public String userSelfUpdateReadTime() {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        // 执行：更新最近查看时间
        doUserSelfUpdateReadTime(currentUserId, new Date());

        // 发送：刷新公告
        sendRefreshBulletin(CollUtil.newHashSet(currentUserId));

        return TempBizCodeEnum.OK;

    }

    /**
     * 执行：更新最近查看时间
     */
    private void doUserSelfUpdateReadTime(Long currentUserId, Date readTime) {

        baseBulletinReadTimeRefUserMapper.insertOrUpdateReadTime(currentUserId, readTime);

    }

    /**
     * 发送：刷新公告
     */
    private void sendRefreshBulletin(@Nullable Set<Long> userIdSet) {

        BaseWebSocketEventBO<Long> baseWebSocketEventBO = new BaseWebSocketEventBO<>();

        baseWebSocketEventBO.setUserIdSet(userIdSet);

        WebSocketMessageDTO<Long> webSocketMessageDTO =
            WebSocketMessageDTO.okData(BaseWebSocketUriEnum.BASE_REFRESH_BULLETIN, null);

        baseWebSocketEventBO.setWebSocketMessageDTO(webSocketMessageDTO);

        // 通知该用户，刷新公告信息
        TempKafkaUtil.sendBaseWebSocketEventTopic(baseWebSocketEventBO);

    }

}
