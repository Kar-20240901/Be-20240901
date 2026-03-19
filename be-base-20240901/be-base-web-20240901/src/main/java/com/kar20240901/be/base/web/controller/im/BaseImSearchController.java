package com.kar20240901.be.base.web.controller.im;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.im.BaseImSearchBaseDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSearchHistoryPageDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.im.BaseImSearchBaseVO;
import com.kar20240901.be.base.web.model.vo.im.BaseImSearchHistoryVO;
import com.kar20240901.be.base.web.service.im.BaseImSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/base/imSearch")
@Tag(name = "基础-im-搜索")
public class BaseImSearchController {

    @Resource
    BaseImSearchService baseService;

    @Operation(summary = "搜索历史-分页排序查询")
    @PostMapping("/history/page")
    public R<Page<BaseImSearchHistoryVO>> searchHistoryPage(@RequestBody @Valid BaseImSearchHistoryPageDTO dto) {
        return R.okData(baseService.searchHistoryPage(dto));
    }

    @Operation(summary = "搜索历史-删除")
    @PostMapping("/history/delete")
    public R<String> searchHistoryDelete(@RequestBody @Valid NotNullId dto) {
        return R.okMsg(baseService.searchHistoryDelete(dto));
    }

    @Operation(summary = "搜索历史-删除所有")
    @PostMapping("/history/deleteAll")
    public R<String> searchHistoryDeleteAll() {
        return R.okMsg(baseService.searchHistoryDeleteAll());
    }

    @Operation(summary = "搜索：联系人，群聊，聊天记录")
    @PostMapping("/base")
    public R<BaseImSearchBaseVO> searchBase(@RequestBody @Valid BaseImSearchBaseDTO dto) {
        return R.okData(baseService.searchBase(dto));
    }

}
