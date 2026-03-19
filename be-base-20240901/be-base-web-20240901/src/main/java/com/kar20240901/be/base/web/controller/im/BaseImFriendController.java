package com.kar20240901.be.base.web.controller.im;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.ScrollListDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImFriendPageDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.im.BaseImFriendPageVO;
import com.kar20240901.be.base.web.service.im.BaseImFriendService;
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
@RequestMapping(value = "/base/imFriend")
@Tag(name = "基础-im-好友-管理")
public class BaseImFriendController {

    @Resource
    BaseImFriendService baseService;

    @Operation(summary = "分页排序查询")
    @PostMapping("/page")
    public R<Page<BaseImFriendPageVO>> myPage(@RequestBody @Valid BaseImFriendPageDTO dto) {
        return R.okData(baseService.myPage(dto));
    }

    @Operation(summary = "滚动加载")
    @PostMapping("/scroll")
    public R<List<BaseImFriendPageVO>> scroll(@RequestBody @Valid ScrollListDTO dto) {
        return R.okData(baseService.scroll(dto));
    }

    @Operation(summary = "删除好友")
    @PostMapping("/removeFriend")
    public R<String> removeFriend(@RequestBody @Valid NotEmptyIdSet dto) {
        return R.okMsg(baseService.removeFriend(dto));
    }

}