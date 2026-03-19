package com.kar20240901.be.base.web.controller.base;

import com.kar20240901.be.base.web.model.domain.base.BaseEmailConfigurationDO;
import com.kar20240901.be.base.web.model.dto.base.BaseEmailConfigurationInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.base.BaseEmailConfigurationService;
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
@RequestMapping(value = "/base/emailConfiguration")
@Tag(name = "基础-邮箱-配置")
public class BaseEmailConfigurationController {

    @Resource
    BaseEmailConfigurationService baseService;

    @Operation(summary = "新增/修改")
    @PostMapping("/insertOrUpdate")
    @PreAuthorize("hasAuthority('baseEmailConfiguration:insertOrUpdate')")
    public R<String> insertOrUpdate(@RequestBody @Valid BaseEmailConfigurationInsertOrUpdateDTO dto) {
        return R.okMsg(baseService.insertOrUpdate(dto));
    }

    @Operation(summary = "通过主键id，查看详情")
    @PostMapping("/infoById")
    @PreAuthorize("hasAuthority('baseEmailConfiguration:infoById')")
    public R<BaseEmailConfigurationDO> infoById() {
        return R.okData(baseService.infoById());
    }

}
