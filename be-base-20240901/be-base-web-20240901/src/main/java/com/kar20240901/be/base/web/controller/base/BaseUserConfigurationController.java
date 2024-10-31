package com.kar20240901.be.base.web.controller.base;

import com.kar20240901.be.base.web.model.domain.base.BaseUserConfigurationDO;
import com.kar20240901.be.base.web.model.dto.base.BaseUserConfigurationInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.base.BaseUserConfigurationService;
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
@RequestMapping(value = "/base/userConfiguration")
@Tag(name = "基础-用户-配置")
public class BaseUserConfigurationController {

    @Resource
    BaseUserConfigurationService baseService;

    @Operation(summary = "新增/修改")
    @PostMapping("/insertOrUpdate")
    @PreAuthorize("hasAuthority('baseUserConfiguration:insertOrUpdate')")
    public R<String> insertOrUpdate(@RequestBody @Valid BaseUserConfigurationInsertOrUpdateDTO dto) {
        return R.okMsg(baseService.insertOrUpdate(dto));
    }

    @Operation(summary = "通过主键id，查看详情")
    @PostMapping("/infoById")
    @PreAuthorize("hasAuthority('baseUserConfiguration:infoById')")
    public R<BaseUserConfigurationDO> infoById() {
        return R.okData(baseService.infoById());
    }

}
