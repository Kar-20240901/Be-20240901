package com.kar20240901.be.base.web.controller.otherapp;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.otherapp.BaseOtherAppOfficialAccountMenuDO;
import com.kar20240901.be.base.web.model.dto.base.ChangeNumberDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.otherapp.BaseOtherAppOfficialAccountMenuInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.otherapp.BaseOtherAppOfficialAccountMenuPageDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.otherapp.BaseOtherAppOfficialAccountMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/base/otherApp/officialAccount/menu")
@RestController
@Tag(name = "基础-第三方应用-公众号-菜单-管理")
public class BaseOtherAppOfficialAccountMenuController {

    @Resource
    BaseOtherAppOfficialAccountMenuService baseService;

    @Operation(summary = "新增/修改")
    @PostMapping("/insertOrUpdate")
    @PreAuthorize("hasAuthority('baseOtherAppOfficialAccountMenu:insertOrUpdate')")
    public R<String> insertOrUpdate(@RequestBody @Valid BaseOtherAppOfficialAccountMenuInsertOrUpdateDTO dto) {
        return R.okMsg(baseService.insertOrUpdate(dto));
    }

    @Operation(summary = "分页排序查询")
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('baseOtherAppOfficialAccountMenu:page')")
    public R<Page<BaseOtherAppOfficialAccountMenuDO>> myPage(
        @RequestBody @Valid BaseOtherAppOfficialAccountMenuPageDTO dto) {
        return R.okData(baseService.myPage(dto));
    }

    @Operation(summary = "查询：树结构")
    @PostMapping("/tree")
    @PreAuthorize("hasAuthority('baseOtherAppOfficialAccountMenu:page')")
    public R<List<BaseOtherAppOfficialAccountMenuDO>> tree(
        @RequestBody @Valid BaseOtherAppOfficialAccountMenuPageDTO dto) {
        return R.okData(baseService.tree(dto));
    }

    @Operation(summary = "通过主键id，查看详情")
    @PostMapping("/infoById")
    @PreAuthorize("hasAuthority('baseOtherAppOfficialAccountMenu:infoById')")
    public R<BaseOtherAppOfficialAccountMenuDO> infoById(@RequestBody @Valid NotNullId notNullId) {
        return R.okData(baseService.infoById(notNullId));
    }

    @Operation(summary = "批量删除")
    @PostMapping("/deleteByIdSet")
    @PreAuthorize("hasAuthority('baseOtherAppOfficialAccountMenu:deleteByIdSet')")
    public R<String> deleteByIdSet(@RequestBody @Valid NotEmptyIdSet notEmptyIdSet) {
        return R.okMsg(baseService.deleteByIdSet(notEmptyIdSet));
    }

    @Operation(summary = "通过主键 idSet，加减排序号")
    @PostMapping("/addOrderNo")
    @PreAuthorize("hasAuthority('baseOtherAppOfficialAccountMenu:insertOrUpdate')")
    public R<String> addOrderNo(@RequestBody @Valid ChangeNumberDTO dto) {
        return R.okMsg(baseService.addOrderNo(dto));
    }

}
