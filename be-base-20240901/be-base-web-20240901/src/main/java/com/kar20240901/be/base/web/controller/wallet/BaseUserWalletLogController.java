package com.kar20240901.be.base.web.controller.wallet;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.wallet.BaseUserWalletLogDO;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserWalletLogPageDTO;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserWalletLogUserSelfPageDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.wallet.BaseUserWalletLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "基础-用户钱包-操作日志")
@RestController
@RequestMapping("/base/userWalletLog")
public class BaseUserWalletLogController {

    @Resource
    BaseUserWalletLogService baseService;

    @Operation(summary = "分页排序查询")
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('baseUserWalletLog:page')")
    public R<Page<BaseUserWalletLogDO>> myPage(@RequestBody @Valid BaseUserWalletLogPageDTO dto) {
        return R.okData(baseService.myPage(dto));
    }

    @Operation(summary = "分页排序查询-用户自我")
    @PostMapping("/page/userSelf")
    public R<Page<BaseUserWalletLogDO>> myPageUserSelf(@RequestBody @Valid BaseUserWalletLogUserSelfPageDTO dto) {
        return R.okData(baseService.myPageUserSelf(dto));
    }

}
