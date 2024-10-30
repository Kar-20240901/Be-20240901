package com.kar20240901.be.base.web.controller.socket;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.socket.BaseSocketDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.socket.BaseSocketPageDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.socket.BaseSocketService;
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
@RequestMapping(value = "/base/socket")
@Tag(name = "基础-socket-管理")
public class BaseSocketController {

    @Resource
    BaseSocketService baseService;

    @Operation(summary = "分页排序查询")
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('baseSocket:page')")
    public R<Page<BaseSocketDO>> myPage(@RequestBody @Valid BaseSocketPageDTO dto) {
        return R.okData(baseService.myPage(dto));
    }

    @Operation(summary = "批量：禁用socket")
    @PostMapping("/disableByIdSet")
    @PreAuthorize("hasAuthority('baseSocket:insertOrUpdate')")
    public R<String> disableByIdSet(@RequestBody @Valid NotEmptyIdSet notEmptyIdSet) {
        return R.okMsg(baseService.disableByIdSet(notEmptyIdSet));
    }

    @Operation(summary = "批量：启用socket")
    @PostMapping("/enableByIdSet")
    @PreAuthorize("hasAuthority('baseSocket:insertOrUpdate')")
    public R<String> enableByIdSet(@RequestBody @Valid NotEmptyIdSet notEmptyIdSet) {
        return R.okMsg(baseService.enableByIdSet(notEmptyIdSet));
    }

    @Operation(summary = "批量：删除socket")
    @PostMapping("/deleteByIdSet")
    @PreAuthorize("hasAuthority('baseSocket:insertOrUpdate')")
    public R<String> deleteByIdSet(@RequestBody @Valid NotEmptyIdSet notEmptyIdSet) {
        return R.okMsg(baseService.deleteByIdSet(notEmptyIdSet));
    }

}
