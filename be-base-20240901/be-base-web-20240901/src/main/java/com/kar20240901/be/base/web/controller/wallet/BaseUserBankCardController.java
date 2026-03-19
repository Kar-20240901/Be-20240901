package com.kar20240901.be.base.web.controller.wallet;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.wallet.BaseUserBankCardDO;
import com.kar20240901.be.base.web.model.dto.base.NotNullLong;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserBankCardInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserBankCardInsertOrUpdateUserSelfDTO;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserBankCardPageDTO;
import com.kar20240901.be.base.web.model.vo.base.DictStringVO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.wallet.BaseUserBankCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "基础-用户银行卡-管理")
@RestController
@RequestMapping("/base/userBankCard")
public class BaseUserBankCardController {

    @Resource
    BaseUserBankCardService baseService;

    @Operation(summary = "新增/修改")
    @PostMapping("/insertOrUpdate")
    @PreAuthorize("hasAuthority('baseUserBankCard:insertOrUpdate')")
    public R<String> insertOrUpdate(@RequestBody @Valid BaseUserBankCardInsertOrUpdateDTO dto) {
        return R.okMsg(baseService.insertOrUpdate(dto));
    }

    @Operation(summary = "新增/修改-用户")
    @PostMapping("/insertOrUpdate/userSelf")
    public R<String> insertOrUpdateUserSelf(@RequestBody @Valid BaseUserBankCardInsertOrUpdateUserSelfDTO dto) {
        return R.okMsg(baseService.insertOrUpdateUserSelf(dto));
    }

    @Operation(summary = "分页排序查询")
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('baseUserBankCard:page')")
    public R<Page<BaseUserBankCardDO>> myPage(@RequestBody @Valid BaseUserBankCardPageDTO dto) {
        return R.okData(baseService.myPage(dto));
    }

    @Operation(summary = "下拉列表-开户行名称")
    @PostMapping("/dictList/openBankName")
    public R<Page<DictStringVO>> openBankNameDictList() {
        return R.okData(baseService.openBankNameDictList());
    }

    @Operation(summary = "通过主键id，查看详情")
    @PostMapping("/infoById")
    @PreAuthorize("hasAuthority('baseUserBankCard:infoById')")
    public R<BaseUserBankCardDO> infoById(@RequestBody @Valid NotNullLong notNullLong) {
        return R.okData(baseService.infoById(notNullLong));
    }

    @Operation(summary = "通过主键id，查看详情-用户")
    @PostMapping("/infoById/userSelf")
    public R<BaseUserBankCardDO> infoByIdUserSelf() {
        return R.okData(baseService.infoByIdUserSelf());
    }

}
