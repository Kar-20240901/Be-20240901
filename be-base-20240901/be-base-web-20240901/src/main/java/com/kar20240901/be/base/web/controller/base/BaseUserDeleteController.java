package com.kar20240901.be.base.web.controller.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.base.BaseUserDeleteLogDO;
import com.kar20240901.be.base.web.model.dto.base.BaseUserDeleteLogPageDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.base.BaseUserDeleteLogService;
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
@RequestMapping(value = "/base/userDeleteLog")
@Tag(name = "基础-用户删除日志-管理")
public class BaseUserDeleteController {

    @Resource
    BaseUserDeleteLogService baseService;

    @Operation(summary = "分页排序查询")
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('baseUserDeleteLog:page')")
    public R<Page<BaseUserDeleteLogDO>> myPage(@RequestBody @Valid BaseUserDeleteLogPageDTO dto) {
        return R.okData(baseService.myPage(dto));
    }

    @Operation(summary = "通过主键id，查看详情")
    @PostMapping("/infoById")
    @PreAuthorize("hasAuthority('baseUserDeleteLog:infoById')")
    public R<BaseUserDeleteLogDO> infoById(@RequestBody @Valid NotNullId notNullId) {
        return R.okData(baseService.infoById(notNullId));
    }

    @Operation(summary = "批量删除")
    @PostMapping("/deleteByIdSet")
    @PreAuthorize("hasAuthority('baseUserDeleteLog:deleteByIdSet')")
    public R<String> deleteByIdSet(@RequestBody @Valid NotEmptyIdSet notEmptyIdSet) {
        return R.okMsg(baseService.deleteByIdSet(notEmptyIdSet));
    }

}
