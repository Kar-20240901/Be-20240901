package com.kar20240901.be.base.web.controller.bulletin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.bulletin.BaseBulletinDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.bulletin.BaseBulletinInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.bulletin.BaseBulletinPageDTO;
import com.kar20240901.be.base.web.model.dto.bulletin.BaseBulletinUserSelfPageDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.bulletin.BaseBulletinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "基础-公告-管理")
@RestController
@RequestMapping("/base/bulletin")
public class BaseBulletinController {

    @Resource
    BaseBulletinService baseService;

    @Operation(summary = "新增/修改")
    @PostMapping("/insertOrUpdate")
    @PreAuthorize("hasAuthority('baseBulletin:insertOrUpdate')")
    public R<String> insertOrUpdate(@RequestBody @Valid BaseBulletinInsertOrUpdateDTO dto) {
        return R.okData(baseService.insertOrUpdate(dto));
    }

    @Operation(summary = "分页排序查询")
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('baseBulletin:page')")
    public R<Page<BaseBulletinDO>> myPage(@RequestBody @Valid BaseBulletinPageDTO dto) {
        return R.okData(baseService.myPage(dto));
    }

    @Operation(summary = "通过主键id，查看详情")
    @PostMapping("/infoById")
    @PreAuthorize("hasAuthority('baseBulletin:infoById')")
    public R<BaseBulletinDO> infoById(@RequestBody @Valid NotNullId notNullId) {
        return R.okData(baseService.infoById(notNullId));
    }

    @Operation(summary = "批量删除")
    @PostMapping("/deleteByIdSet")
    @PreAuthorize("hasAuthority('baseBulletin:deleteByIdSet')")
    public R<String> deleteByIdSet(@RequestBody @Valid NotEmptyIdSet notEmptyIdSet) {
        return R.okMsg(baseService.deleteByIdSet(notEmptyIdSet));
    }

    @Operation(summary = "发布")
    @PostMapping("/publish")
    @PreAuthorize("hasAuthority('baseBulletin:insertOrUpdate')")
    public R<String> publish(@RequestBody @Valid NotNullId notNullId) {
        return R.okMsg(baseService.publish(notNullId));
    }

    @Operation(summary = "撤回")
    @PostMapping("/revoke")
    @PreAuthorize("hasAuthority('baseBulletin:insertOrUpdate')")
    public R<String> revoke(@RequestBody @Valid NotNullId notNullId) {
        return R.okMsg(baseService.revoke(notNullId));
    }

    @PostMapping("/userSelfPage")
    @Operation(summary = "分页排序查询：当前用户可以查看的公告")
    public R<Page<BaseBulletinDO>> userSelfPage(@RequestBody @Valid BaseBulletinUserSelfPageDTO dto) {
        return R.okData(baseService.userSelfPage(dto));
    }

    @PostMapping("/userSelfInfoById")
    @Operation(summary = "当前用户查看公告详情")
    public R<BaseBulletinDO> userSelfInfoById(@RequestBody @Valid NotNullId notNullId) {
        return R.okData(baseService.userSelfInfoById(notNullId));
    }

    @PostMapping("/userSelfCount")
    @Operation(summary = "当前用户可以查看的公告总数")
    public R<Long> userSelfCount() {
        return R.okData(baseService.userSelfCount());
    }

    @PostMapping("/userSelfUpdateReadTime")
    @Operation(summary = "当前用户更新公告最近查看时间")
    public R<String> userSelfUpdateReadTime() {
        return R.okMsg(baseService.userSelfUpdateReadTime());
    }

}
