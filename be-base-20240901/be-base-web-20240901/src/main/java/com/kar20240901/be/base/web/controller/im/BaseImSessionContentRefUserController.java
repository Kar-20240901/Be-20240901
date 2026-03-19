package com.kar20240901.be.base.web.controller.im;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.ScrollListDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSessionContentRefUserPageDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.im.BaseImSessionContentRefUserPageVO;
import com.kar20240901.be.base.web.service.im.BaseImSessionContentRefUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/base/imSessionContentRefUser")
@Tag(name = "基础-im-会话-内容-关联-用户")
public class BaseImSessionContentRefUserController {

    @Resource
    BaseImSessionContentRefUserService baseService;

    @Operation(summary = "分页排序查询")
    @PostMapping("/myPage")
    public R<Page<BaseImSessionContentRefUserPageVO>> myPage(
        @RequestBody @Valid BaseImSessionContentRefUserPageDTO dto) {
        return R.okData(baseService.myPage(dto));
    }

    @Operation(summary = "滚动加载")
    @PostMapping("/scroll")
    public R<List<BaseImSessionContentRefUserPageVO>> scroll(@RequestBody @Valid ScrollListDTO dto) {
        return R.okData(baseService.scroll(dto));
    }

    @Operation(summary = "清空聊天记录")
    @PostMapping("/deleteSessionContentRefUser")
    public R<String> deleteSessionContentRefUser(@RequestBody @Valid NotEmptyIdSet dto) {
        return R.okMsg(baseService.deleteSessionContentRefUser(dto));
    }

    @Operation(summary = "隐藏消息内容")
    @PostMapping("/hideSessionContentRefUser")
    public R<String> hideSessionContentRefUser(@RequestBody @Valid NotEmptyIdSet dto) {
        return R.okMsg(baseService.hideSessionContentRefUser(dto));
    }

}
