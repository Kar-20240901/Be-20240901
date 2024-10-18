package com.kar20240901.be.base.web.controller.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.base.BaseAuthDO;
import com.kar20240901.be.base.web.model.dto.base.BaseAuthInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.base.BaseAuthPageDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.vo.base.BaseAuthInfoByIdVO;
import com.kar20240901.be.base.web.model.vo.base.DictVO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.base.BaseAuthService;
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
@RequestMapping(value = "/base/auth")
@Tag(name = "基础-权限-管理")
public class BaseAuthController {

    @Resource
    BaseAuthService baseService;

    @Operation(summary = "新增/修改")
    @PostMapping("/insertOrUpdate")
    @PreAuthorize("hasAuthority('baseAuth:insertOrUpdate')")
    public R<String> insertOrUpdate(@RequestBody @Valid BaseAuthInsertOrUpdateDTO dto) {
        return R.okMsg(baseService.insertOrUpdate(dto));
    }

    @Operation(summary = "分页排序查询")
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('baseAuth:page')")
    public R<Page<BaseAuthDO>> myPage(@RequestBody @Valid BaseAuthPageDTO dto) {
        return R.okData(baseService.myPage(dto));
    }

    @Operation(summary = "下拉列表")
    @PostMapping("/dictList")
    @PreAuthorize("hasAuthority('baseAuth:dictList')")
    public R<Page<DictVO>> dictList() {
        return R.okData(baseService.dictList());
    }

    @Operation(summary = "通过主键id，查看详情")
    @PostMapping("/infoById")
    @PreAuthorize("hasAuthority('baseAuth:infoById')")
    public R<BaseAuthInfoByIdVO> infoById(@RequestBody @Valid NotNullId notNullId) {
        return R.okData(baseService.infoById(notNullId));
    }

    @Operation(summary = "批量删除")
    @PostMapping("/deleteByIdSet")
    @PreAuthorize("hasAuthority('baseAuth:deleteByIdSet')")
    public R<String> deleteByIdSet(@RequestBody @Valid NotEmptyIdSet notEmptyIdSet) {
        return R.okMsg(baseService.deleteByIdSet(notEmptyIdSet));
    }

}
