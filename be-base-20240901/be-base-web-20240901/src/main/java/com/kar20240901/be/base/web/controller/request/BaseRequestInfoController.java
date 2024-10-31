package com.kar20240901.be.base.web.controller.request;

import com.kar20240901.be.base.web.model.domain.request.BaseRequestInfoDO;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.request.BaseRequestInfoService;
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
@RequestMapping("/base/requestInfo")
@Tag(name = "基础-请求详情-管理")
public class BaseRequestInfoController {

    @Resource
    BaseRequestInfoService baseService;

    @Operation(summary = "通过主键id，查看详情")
    @PostMapping("/infoById")
    @PreAuthorize("hasAuthority('baseRequestInfo:infoById')")
    public R<BaseRequestInfoDO> infoById(@RequestBody @Valid NotNullId notNullId) {
        return R.okData(baseService.infoById(notNullId));
    }

}
