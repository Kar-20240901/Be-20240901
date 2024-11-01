package com.kar20240901.be.base.web.controller.otherapp;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.otherapp.BaseOtherAppDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.otherapp.BaseOtherAppInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.otherapp.BaseOtherAppPageDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.otherapp.BaseOtherAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/base/otherApp")
@RestController
@Tag(name = "基础-第三方应用-管理")
public class BaseOtherAppController {

    @Resource
    BaseOtherAppService baseService;

    @Operation(summary = "新增/修改")
    @PostMapping("/insertOrUpdate")
    @PreAuthorize("hasAuthority('baseOtherApp:insertOrUpdate')")
    public R<String> insertOrUpdate(@RequestBody @Valid BaseOtherAppInsertOrUpdateDTO dto) {
        return R.okMsg(baseService.insertOrUpdate(dto));
    }

    @Operation(summary = "分页排序查询")
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('baseOtherApp:page')")
    public R<Page<BaseOtherAppDO>> myPage(@RequestBody @Valid BaseOtherAppPageDTO dto) {
        return R.okData(baseService.myPage(dto));
    }

    @Operation(summary = "通过主键id，查看详情")
    @PostMapping("/infoById")
    @PreAuthorize("hasAuthority('baseOtherApp:infoById')")
    public R<BaseOtherAppDO> infoById(@RequestBody @Valid NotNullId notNullId) {
        return R.okData(baseService.infoById(notNullId));
    }

    @Operation(summary = "批量删除")
    @PostMapping("/deleteByIdSet")
    @PreAuthorize("hasAuthority('baseOtherApp:deleteByIdSet')")
    public R<String> deleteByIdSet(@RequestBody @Valid NotEmptyIdSet notEmptyIdSet) {
        return R.okMsg(baseService.deleteByIdSet(notEmptyIdSet));
    }

    @Operation(summary = "通过主键id，获取第三方应用名")
    @PostMapping("/getNameById")
    @PreAuthorize("hasAuthority('baseOtherApp:page')")
    public R<String> getNameById(@RequestBody @Valid NotNullId notNullId) {
        return R.okData(baseService.getNameById(notNullId));
    }

}
