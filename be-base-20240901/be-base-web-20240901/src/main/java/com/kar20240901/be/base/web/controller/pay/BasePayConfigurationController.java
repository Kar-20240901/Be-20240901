package com.kar20240901.be.base.web.controller.pay;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.pay.BasePayConfigurationDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.pay.BasePayConfigurationInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.pay.BasePayConfigurationPageDTO;
import com.kar20240901.be.base.web.model.vo.base.DictVO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.pay.BasePayConfigurationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/base/payConfiguration")
@RestController
@Tag(name = "基础-支付配置-管理")
public class BasePayConfigurationController {

    @Resource
    BasePayConfigurationService baseService;

    @Operation(summary = "新增/修改")
    @PostMapping("/insertOrUpdate")
    @PreAuthorize("hasAuthority('basePayConfiguration:insertOrUpdate')")
    public R<String> insertOrUpdate(@RequestBody @Valid BasePayConfigurationInsertOrUpdateDTO dto) {
        return R.okMsg(baseService.insertOrUpdate(dto));
    }

    @Operation(summary = "分页排序查询")
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('basePayConfiguration:page')")
    public R<Page<BasePayConfigurationDO>> myPage(@RequestBody @Valid BasePayConfigurationPageDTO dto) {
        return R.okData(baseService.myPage(dto));
    }

    @Operation(summary = "下拉列表")
    @PostMapping("/dictList")
    @PreAuthorize("hasAuthority('basePayConfiguration:dictList')")
    public R<Page<DictVO>> dictList() {
        return R.okData(baseService.dictList());
    }

    @Operation(summary = "通过主键id，查看详情")
    @PostMapping("/infoById")
    @PreAuthorize("hasAuthority('basePayConfiguration:infoById')")
    public R<BasePayConfigurationDO> infoById(@RequestBody @Valid NotNullId notNullId) {
        return R.okData(baseService.infoById(notNullId));
    }

    @Operation(summary = "批量删除")
    @PostMapping("/deleteByIdSet")
    @PreAuthorize("hasAuthority('basePayConfiguration:deleteByIdSet')")
    public R<String> deleteByIdSet(@RequestBody @Valid NotEmptyIdSet notEmptyIdSet) {
        return R.okMsg(baseService.deleteByIdSet(notEmptyIdSet));
    }

}
