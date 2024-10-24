package com.kar20240901.be.base.web.controller.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.request.BaseRequestDO;
import com.kar20240901.be.base.web.model.dto.request.BaseRequestPageDTO;
import com.kar20240901.be.base.web.model.dto.request.BaseRequestSelfLoginRecordPageDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.request.SysRequestAllAvgVO;
import com.kar20240901.be.base.web.service.request.BaseRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/base/request")
@Tag(name = "基础-请求-管理")
public class BaseRequestController {

    @Resource
    BaseRequestService baseService;

    @PreAuthorize("hasAuthority('baseRequest:page')")
    @PostMapping("/page")
    @Operation(summary = "分页排序查询")
    public R<Page<BaseRequestDO>> myPage(@RequestBody @Valid BaseRequestPageDTO dto) {
        return R.okData(baseService.myPage(dto));
    }

    @PostMapping("/allAvgPro")
    @Operation(summary = "所有请求的平均耗时-增强：增加筛选项")
    public R<SysRequestAllAvgVO> allAvgPro(@RequestBody @Valid BaseRequestPageDTO dto) {
        return R.okData(baseService.allAvgPro(dto));
    }

    @PostMapping("/self/loginRecord")
    @Operation(summary = "当前用户：登录记录")
    public R<Page<BaseRequestDO>> selfLoginRecord(@RequestBody @Valid BaseRequestSelfLoginRecordPageDTO dto) {
        return R.okData(baseService.selfLoginRecord(dto));
    }

}
