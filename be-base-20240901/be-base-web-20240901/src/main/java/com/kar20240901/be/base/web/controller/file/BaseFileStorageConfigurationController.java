package com.kar20240901.be.base.web.controller.file;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.file.BaseFileStorageConfigurationDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.file.BaseFileStorageConfigurationInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileStorageConfigurationPageDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.file.BaseFileStorageConfigurationService;
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
@RequestMapping(value = "/base/fileStorageConfiguration")
@Tag(name = "基础-文件-存储配置-管理")
public class BaseFileStorageConfigurationController {

    @Resource
    BaseFileStorageConfigurationService baseService;

    @Operation(summary = "新增/修改")
    @PostMapping("/insertOrUpdate")
    @PreAuthorize("hasAuthority('baseFileStorageConfiguration:insertOrUpdate')")
    public R<String> insertOrUpdate(@RequestBody @Valid BaseFileStorageConfigurationInsertOrUpdateDTO dto) {
        return R.okMsg(baseService.insertOrUpdate(dto));
    }

    @Operation(summary = "分页排序查询")
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('baseFileStorageConfiguration:page')")
    public R<Page<BaseFileStorageConfigurationDO>> myPage(@RequestBody @Valid BaseFileStorageConfigurationPageDTO dto) {
        return R.okData(baseService.myPage(dto));
    }

    @Operation(summary = "通过主键id，查看详情")
    @PostMapping("/infoById")
    @PreAuthorize("hasAuthority('baseFileStorageConfiguration:infoById')")
    public R<BaseFileStorageConfigurationDO> infoById(@RequestBody @Valid NotNullId notNullId) {
        return R.okData(baseService.infoById(notNullId));
    }

    @Operation(summary = "批量删除")
    @PostMapping("/deleteByIdSet")
    @PreAuthorize("hasAuthority('baseFileStorageConfiguration:deleteByIdSet')")
    public R<String> deleteByIdSet(@RequestBody @Valid NotEmptyIdSet notEmptyIdSet) {
        return R.okMsg(baseService.deleteByIdSet(notEmptyIdSet));
    }

}
