package com.kar20240901.be.base.web.service.request.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.mapper.request.SysRequestMapper;
import com.kar20240901.be.base.web.model.constant.base.OperationDescriptionConstant;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoIdSuper;
import com.kar20240901.be.base.web.model.domain.request.SysRequestDO;
import com.kar20240901.be.base.web.model.dto.request.SysRequestPageDTO;
import com.kar20240901.be.base.web.model.dto.request.SysRequestSelfLoginRecordPageDTO;
import com.kar20240901.be.base.web.model.vo.request.SysRequestAllAvgVO;
import com.kar20240901.be.base.web.service.request.SysRequestService;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import org.springframework.stereotype.Service;

@Service
public class SysRequestServiceImpl extends ServiceImpl<SysRequestMapper, SysRequestDO> implements SysRequestService {

    /**
     * 分页排序查询
     */
    @Override
    public Page<SysRequestDO> myPage(SysRequestPageDTO dto) {

        return getMyPageLambdaQueryChainWrapper(dto) //
            .orderByDesc(TempEntityNoIdSuper::getCreateTime)
            .select(SysRequestDO::getIp, SysRequestDO::getUri, SysRequestDO::getSuccessFlag,
                TempEntityNoIdSuper::getCreateTime, TempEntityNoIdSuper::getCreateId, SysRequestDO::getName,
                SysRequestDO::getCategory, SysRequestDO::getIp, SysRequestDO::getRegion, TempEntity::getId)
            .page(dto.pageOrder());

    }

    private LambdaQueryChainWrapper<SysRequestDO> getMyPageLambdaQueryChainWrapper(SysRequestPageDTO dto) {

        return lambdaQuery().like(StrUtil.isNotBlank(dto.getUri()), SysRequestDO::getUri, dto.getUri()) //
            .like(StrUtil.isNotBlank(dto.getName()), SysRequestDO::getName, dto.getName()) //
            .like(StrUtil.isNotBlank(dto.getIp()), SysRequestDO::getIp, dto.getIp()) //
            .like(StrUtil.isNotBlank(dto.getRegion()), SysRequestDO::getRegion, dto.getRegion()) //
            .like(StrUtil.isNotBlank(dto.getType()), SysRequestDO::getType, dto.getType()) //
            .le(dto.getEndCostMs() != null, SysRequestDO::getCostMs, dto.getEndCostMs()) //
            .ge(dto.getBeginCostMs() != null, SysRequestDO::getCostMs, dto.getBeginCostMs()) //
            .le(dto.getCtEndTime() != null, SysRequestDO::getCreateTime, dto.getCtEndTime()) //
            .ge(dto.getCtBeginTime() != null, SysRequestDO::getCreateTime, dto.getCtBeginTime()) //
            .eq(dto.getCategory() != null, SysRequestDO::getCategory, dto.getCategory()) //
            .eq(dto.getCreateId() != null, TempEntity::getCreateId, dto.getCreateId()) //
            .eq(dto.getSuccessFlag() != null, SysRequestDO::getSuccessFlag, dto.getSuccessFlag());

    }

    /**
     * 所有请求的平均耗时-增强：增加筛选项
     */
    @Override
    public SysRequestAllAvgVO allAvgPro(SysRequestPageDTO dto) {

        query().select("");

        return null;

    }

    /**
     * 当前用户：登录记录
     */
    @Override
    public Page<SysRequestDO> selfLoginRecord(SysRequestSelfLoginRecordPageDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        SysRequestPageDTO sysRequestPageDTO = new SysRequestPageDTO();

        sysRequestPageDTO.setType(OperationDescriptionConstant.SIGN_IN);
        sysRequestPageDTO.setCreateId(currentUserId);
        sysRequestPageDTO.setCategory(dto.getCategory());
        sysRequestPageDTO.setRegion(dto.getRegion());
        sysRequestPageDTO.setIp(dto.getIp());
        sysRequestPageDTO.setCurrent(dto.getCurrent());
        sysRequestPageDTO.setPageSize(dto.getPageSize());
        sysRequestPageDTO.setOrder(dto.getOrder());

        // 执行
        return myPage(sysRequestPageDTO);

    }

}
