package com.kar20240901.be.base.web.controller.socket;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.socket.BaseSocketRefUserDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.socket.BaseSocketRefUserPageDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.socket.BaseSocketRefUserService;
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
@RequestMapping(value = "/base/socketRefUser")
@Tag(name = "基础-socket-用户管理")
public class BaseSocketRefUserController {

    @Resource
    BaseSocketRefUserService baseService;

    @Operation(summary = "分页排序查询")
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('baseSocketRefUser:page')")
    public R<Page<BaseSocketRefUserDO>> myPage(@RequestBody @Valid BaseSocketRefUserPageDTO dto) {
        return R.okData(baseService.myPage(dto));
    }

    @Operation(summary = "批量：下线用户")
    @PostMapping("/offlineByIdSet")
    @PreAuthorize("hasAuthority('baseSocketRefUser:insertOrUpdate')")
    public R<String> offlineByIdSet(@RequestBody @Valid NotEmptyIdSet notEmptyIdSet) {
        return R.okMsg(baseService.offlineByIdSet(notEmptyIdSet));
    }

    @Operation(summary = "批量：开关控制台")
    @PostMapping("/changeConsoleFlagByIdSet")
    @PreAuthorize("hasAuthority('baseSocketRefUser:insertOrUpdate')")
    public R<String> changeConsoleFlagByIdSet(@RequestBody @Valid NotEmptyIdSet notEmptyIdSet) {
        return R.okMsg(baseService.changeConsoleFlagByIdSet(notEmptyIdSet));
    }

}
