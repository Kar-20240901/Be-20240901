package com.kar20240901.be.base.web.service.im.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.im.BaseImSessionContentRefUserMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImSessionRefUserMapper;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionContentRefUserDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionRefUserDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.ScrollListDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSessionContentRefUserPageDTO;
import com.kar20240901.be.base.web.model.vo.im.BaseImSessionContentRefUserPageVO;
import com.kar20240901.be.base.web.service.im.BaseImSessionContentRefUserService;
import com.kar20240901.be.base.web.service.im.BaseImSessionRefUserService;
import com.kar20240901.be.base.web.util.base.MyPageUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BaseImSessionContentRefUserServiceImpl
    extends ServiceImpl<BaseImSessionContentRefUserMapper, BaseImSessionContentRefUserDO>
    implements BaseImSessionContentRefUserService {

    private static BaseImSessionRefUserMapper baseImSessionRefUserMapper;

    @Resource
    public void setBaseImSessionRefUserMapper(BaseImSessionRefUserMapper baseImSessionRefUserMapper) {
        BaseImSessionContentRefUserServiceImpl.baseImSessionRefUserMapper = baseImSessionRefUserMapper;
    }

    @Resource
    BaseImSessionRefUserService baseImSessionRefUserService;

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseImSessionContentRefUserPageVO> myPage(BaseImSessionContentRefUserPageDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        return baseMapper.myPage(dto.pageOrder(), dto, currentUserId);

    }

    /**
     * 滚动加载
     */
    @Override
    public List<BaseImSessionContentRefUserPageVO> scroll(ScrollListDTO dto) {

        Assert.notNull(dto.getRefId());

        Long currentUserId = MyUserUtil.getCurrentUserId();

        if (BooleanUtil.isFalse(dto.getBoolean1())) {
            updateLastOpenTs(currentUserId, dto.getRefId()); // 更新最后一次打开会话的时间
        }

        boolean backwardFlag = BooleanUtil.isTrue(dto.getBackwardFlag());

        // 获取：滚动加载时的 id
        Long contentId = MyPageUtil.getScrollId(dto);

        BaseImSessionContentRefUserPageDTO pageDTO = new BaseImSessionContentRefUserPageDTO();

        pageDTO.setBackwardFlag(backwardFlag);

        pageDTO.setContentId(contentId);

        pageDTO.setContent(dto.getSearchKey());

        pageDTO.setSessionId(dto.getRefId());

        List<BaseImSessionContentRefUserPageVO> records =
            baseMapper.myPage(MyPageUtil.getScrollPage(dto.getPageSize()), pageDTO, currentUserId).getRecords();

        // 后续处理：records
        return scrollHandleRecords(dto, records, backwardFlag, pageDTO, currentUserId);

    }

    /**
     * 后续处理：records
     */
    private List<BaseImSessionContentRefUserPageVO> scrollHandleRecords(ScrollListDTO dto,
        List<BaseImSessionContentRefUserPageVO> records, boolean backwardFlag,
        BaseImSessionContentRefUserPageDTO pageDTO, Long currentUserId) {

        if (!BooleanUtil.isTrue(dto.getQueryMoreFlag())) {
            return records;
        }

        if (CollUtil.isEmpty(records)) {
            return records;
        }

        long pageSize;

        long morePageSize = 2L;

        if (records.size() < dto.getPageSize()) {

            pageSize = dto.getPageSize() + morePageSize - records.size();

        } else {

            pageSize = morePageSize;

        }

        pageDTO.setBackwardFlag(!backwardFlag);

        List<BaseImSessionContentRefUserPageVO> moreRecords =
            baseMapper.myPage(MyPageUtil.getScrollPage(pageSize), pageDTO, currentUserId).getRecords();

        if (CollUtil.isEmpty(moreRecords)) {
            return records;
        }

        if (backwardFlag) {

            CollUtil.addAll(moreRecords, records);

            return moreRecords;

        } else {

            CollUtil.addAll(records, moreRecords);

            return records;

        }

    }

    /**
     * 更新最后一次打开会话的时间
     */
    public static void updateLastOpenTs(Long userId, Long sessionId) {

        ChainWrappers.lambdaUpdateChain(baseImSessionRefUserMapper).eq(BaseImSessionRefUserDO::getUserId, userId)
            .eq(BaseImSessionRefUserDO::getSessionId, sessionId)
            .set(BaseImSessionRefUserDO::getLastOpenTs, new Date().getTime())
            .set(BaseImSessionRefUserDO::getShowFlag, true).update();

    }

    /**
     * 清空聊天记录
     */
    @Override
    public String deleteSessionContentRefUser(NotEmptyIdSet dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        Set<Long> sessionIdSet = dto.getIdSet();

        lambdaUpdate().eq(BaseImSessionContentRefUserDO::getUserId, currentUserId)
            .in(BaseImSessionContentRefUserDO::getSessionId, sessionIdSet).remove();

        return TempBizCodeEnum.OK;

    }

    /**
     * 清空聊天记录并隐藏会话
     */
    @Override
    @DSTransactional
    public String deleteSessionContentRefUserAndHiddenSession(NotEmptyIdSet dto) {

        deleteSessionContentRefUser(dto);

        baseImSessionRefUserService.hidden(dto);

        return TempBizCodeEnum.OK;

    }

    /**
     * 隐藏消息内容
     */
    @Override
    public String hideSessionContentRefUser(NotEmptyIdSet dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        Set<Long> contentIdSet = dto.getIdSet();

        lambdaUpdate().eq(BaseImSessionContentRefUserDO::getUserId, currentUserId)
            .in(BaseImSessionContentRefUserDO::getContentId, contentIdSet)
            .set(BaseImSessionContentRefUserDO::getShowFlag, false).update();

        return TempBizCodeEnum.OK;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Item {

        private String name;

        private String value;

        private int type;

        private String gap;

    }

    public static void main(String[] args) {

        //        int base = 64 + 762; // 400
        //
        //        List<Item> list = new ArrayList<>();
        //
        //        list.add(new Item("伤害", "7", 102, "0.7")); // 700+240
        //
        //        list.add(new Item("智慧", "3.6", 201, "0.29")); // 720+
        //        list.add(new Item("伤害额外", "0.5", 201, "0.15")); // 35.2+27.5+17+24.5+29+36.5+64+
        //        list.add(new Item("法术额外", "0.5", 201, "0.15")); // 16.4+18.5+25
        //        list.add(new Item("冰冷额外", "1", 201, "0.15")); // 41.5+13+166+64+112
        //        list.add(new Item("投射物额外", "1.2", 201, "0.36")); // 40.5+135+
        //        list.add(new Item("纠缠额外", "1.2", 201, "0.4")); // 40+40
        //        list.add(new Item("冰封额外", "0.3", 201, "0.15")); // 10+10
        //        list.add(new Item("软弱额外", "0.15", 201, "0.15")); // 15+
        //        list.add(new Item("击中额外", "0.15", 201, "0.15")); // 35+
        //        list.add(new Item("同时封印生命和魔力额外", "0.15", 201, "0.15")); // 10

        int base = 64 + 762; // 826

        List<Item> list = new ArrayList<>();

        list.add(new Item("伤害", "9.4", 102, "0.7")); // 700+240

        list.add(new Item("智慧", "3.6", 201, "0.29")); // 720+
        list.add(new Item("伤害额外", "2.33", 201, "0.15")); // 35.2+27.5+17+24.5+29+36.5+64+
        list.add(new Item("法术额外", "0.6", 201, "0.15")); // 16.4+18.5+25
        list.add(new Item("冰冷额外", "3.96", 201, "0.15")); // 41.5+13+166+64+112
        list.add(new Item("投射物额外", "1.35", 201, "0.3")); // 135+
        list.add(new Item("纠缠额外", "0.8", 201, "0.4")); // 40+40
        list.add(new Item("冰封额外", "0.2", 201, "0.1")); // 10+10
        list.add(new Item("软弱额外", "0.15", 201, "0.15")); // 15+
        list.add(new Item("击中额外", "0.35", 201, "0.15")); // 35+
        list.add(new Item("同时封印生命和魔力额外", "0.1", 201, "0.1")); // 10

        BigDecimal baseBigDecimal = calc(base, list, "原本", null, "60");

        for (Item item : list) {

            String originalValue = item.getValue();

            item.setValue(new BigDecimal(originalValue).add(new BigDecimal(item.getGap())).toPlainString());

            calc(base, list, "增加\033[35m" + item.getGap() + "\033[0m" + item.getName(), baseBigDecimal,
                item.getGap());

            item.setValue(originalValue);

        }

    }

    /**
     * 计算
     */
    private static BigDecimal calc(int base, List<Item> list, String remark, BigDecimal baseBigDecimal, String gap) {

        BigDecimal result = new BigDecimal(base);

        Map<Integer, List<Item>> map = new HashMap<>();

        for (Item item : list) {

            map.computeIfAbsent(item.getType(), k -> new ArrayList<>()).add(item);

        }

        for (Entry<Integer, List<Item>> entry : map.entrySet()) {

            Integer key = entry.getKey();

            List<Item> valueList = entry.getValue();

            if (key >= 100 && key < 200) {

                BigDecimal calc = new BigDecimal(1);

                for (Item item : valueList) {

                    BigDecimal bigDecimalTemp = new BigDecimal(item.getValue());

                    calc = calc.add(bigDecimalTemp);

                }

                //                BigDecimal original = new BigDecimal(result.toPlainString());

                result = result.multiply(calc);

                //                log.info("inc，伤害：{}，倍率：{}", result.toPlainString(),
                //                    result.divide(original, 2, RoundingMode.HALF_UP));

            } else if (key >= 200 && key < 300) {

                for (Item item : valueList) {

                    BigDecimal bigDecimalTemp = new BigDecimal(item.getValue());

                    //                    BigDecimal original = new BigDecimal(result.toPlainString());

                    result = result.multiply(BigDecimal.valueOf(1).add(bigDecimalTemp));

                    //                    log.info("more，{}，伤害：{}，倍率：{}", item.getName(), result.toPlainString(),
                    //                        result.divide(original, 2, RoundingMode.HALF_UP));

                }

            }

        }

        if (baseBigDecimal == null) {

            log.info("{}伤害：{}", remark, result.toPlainString());

        } else {

            log.info("{}，伤害：{}，最终增加：\033[36m{}\033[0m", remark, result.toPlainString(),
                result.divide(baseBigDecimal, 2, RoundingMode.HALF_UP).subtract(BigDecimal.ONE));

        }

        return result;

    }

}
