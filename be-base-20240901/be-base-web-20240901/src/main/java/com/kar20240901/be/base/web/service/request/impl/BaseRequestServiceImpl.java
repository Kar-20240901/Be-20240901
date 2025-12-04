package com.kar20240901.be.base.web.service.request.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.mapper.request.BaseRequestMapper;
import com.kar20240901.be.base.web.model.constant.base.OperationDescriptionConstant;
import com.kar20240901.be.base.web.model.domain.request.BaseRequestDO;
import com.kar20240901.be.base.web.model.dto.request.BaseRequestPageDTO;
import com.kar20240901.be.base.web.model.dto.request.BaseRequestSelfLoginRecordPageDTO;
import com.kar20240901.be.base.web.model.vo.request.BaseRequestAllAvgVO;
import com.kar20240901.be.base.web.service.request.BaseRequestService;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import org.springframework.stereotype.Service;

@Service
public class BaseRequestServiceImpl extends ServiceImpl<BaseRequestMapper, BaseRequestDO>
    implements BaseRequestService {

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseRequestDO> myPage(BaseRequestPageDTO dto) {

        return getMyPageLambdaQueryChainWrapper(dto) //
            .select(BaseRequestDO::getIp, BaseRequestDO::getUri, BaseRequestDO::getSuccessFlag,
                BaseRequestDO::getCreateTime, BaseRequestDO::getCreateId, BaseRequestDO::getName,
                BaseRequestDO::getCategory, BaseRequestDO::getIp, BaseRequestDO::getRegion, BaseRequestDO::getId,
                BaseRequestDO::getCostMs, BaseRequestDO::getType, BaseRequestDO::getMethod)
            .page(dto.createTimeDescDefaultOrderPage());

    }

    /**
     * 获取：查询对象
     */
    private LambdaQueryChainWrapper<BaseRequestDO> getMyPageLambdaQueryChainWrapper(BaseRequestPageDTO dto) {

        return lambdaQuery().like(StrUtil.isNotBlank(dto.getUri()), BaseRequestDO::getUri, dto.getUri()) //
            .like(StrUtil.isNotBlank(dto.getName()), BaseRequestDO::getName, dto.getName()) //
            .like(StrUtil.isNotBlank(dto.getIp()), BaseRequestDO::getIp, dto.getIp()) //
            .like(StrUtil.isNotBlank(dto.getRegion()), BaseRequestDO::getRegion, dto.getRegion()) //
            .eq(StrUtil.isNotBlank(dto.getType()), BaseRequestDO::getType, dto.getType()) //
            .le(dto.getEndCostMs() != null, BaseRequestDO::getCostMs, dto.getEndCostMs()) //
            .ge(dto.getBeginCostMs() != null, BaseRequestDO::getCostMs, dto.getBeginCostMs()) //
            .le(dto.getCtEndTime() != null, BaseRequestDO::getCreateTime, dto.getCtEndTime()) //
            .ge(dto.getCtBeginTime() != null, BaseRequestDO::getCreateTime, dto.getCtBeginTime()) //
            .eq(dto.getCategory() != null, BaseRequestDO::getCategory, dto.getCategory()) //
            .eq(dto.getCreateId() != null, BaseRequestDO::getCreateId, dto.getCreateId()) //
            .eq(dto.getSuccessFlag() != null, BaseRequestDO::getSuccessFlag, dto.getSuccessFlag());

    }

    /**
     * 所有请求的平均耗时-增强：增加筛选项
     */
    @Override
    public BaseRequestAllAvgVO allAvgPro(BaseRequestPageDTO dto) {

        BaseRequestDO baseRequestDO =
            getMyPageLambdaQueryChainWrapper(dto).select(BaseRequestDO::getCount, BaseRequestDO::getAvgMs)
                .gt(BaseRequestDO::getCostMs, 1).one();

        return new BaseRequestAllAvgVO(baseRequestDO.getCount(), baseRequestDO.getAvgMs());

    }

    /**
     * 当前用户：登录记录
     */
    @Override
    public Page<BaseRequestDO> selfLoginRecord(BaseRequestSelfLoginRecordPageDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        BaseRequestPageDTO baseRequestPageDTO = new BaseRequestPageDTO();

        baseRequestPageDTO.setType(OperationDescriptionConstant.SIGN_IN);
        baseRequestPageDTO.setCreateId(currentUserId);
        baseRequestPageDTO.setCategory(dto.getCategory());
        baseRequestPageDTO.setRegion(dto.getRegion());
        baseRequestPageDTO.setIp(dto.getIp());
        baseRequestPageDTO.setCurrent(dto.getCurrent());
        baseRequestPageDTO.setPageSize(dto.getPageSize());
        baseRequestPageDTO.setOrder(dto.getOrder());

        // 执行
        return myPage(baseRequestPageDTO);

    }

}
