package com.kar20240901.be.base.web.controller.im;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.base.ScrollListDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupChangeBelongIdDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupPageDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupRemoveUserDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.im.BaseImGroupPageVO;
import com.kar20240901.be.base.web.service.im.BaseImGroupService;
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
@RequestMapping(value = "/base/imGroup")
@Tag(name = "基础-im-群组-管理")
public class BaseImGroupController {

    @Resource
    BaseImGroupService baseService;

    @Operation(summary = "新增/修改")
    @PostMapping("/insertOrUpdate")
    public R<String> insertOrUpdate(@RequestBody @Valid BaseImGroupInsertOrUpdateDTO dto) {
        return R.okData(baseService.insertOrUpdate(dto));
    }

    @Operation(summary = "分页排序查询")
    @PostMapping("/page")
    public R<Page<BaseImGroupPageVO>> myPage(@RequestBody @Valid BaseImGroupPageDTO dto) {
        return R.okData(baseService.myPage(dto));
    }

    @Operation(summary = "滚动加载")
    @PostMapping("/scroll")
    public R<List<BaseImGroupPageVO>> scroll(@RequestBody @Valid ScrollListDTO dto) {
        return R.okData(baseService.scroll(dto));
    }

    @Operation(summary = "踢出群员")
    @PostMapping("/removeUser")
    public R<String> removeUser(@RequestBody @Valid BaseImGroupRemoveUserDTO dto) {
        return R.okMsg(baseService.removeUser(dto));
    }

    @Operation(summary = "修改群主")
    @PostMapping("/changeBelongId")
    public R<String> changeBelongId(@RequestBody @Valid BaseImGroupChangeBelongIdDTO dto) {
        return R.okMsg(baseService.changeBelongId(dto));
    }

    @Operation(summary = "解散群组")
    @PostMapping("/deleteById")
    public R<String> deleteById(@RequestBody @Valid NotNullId dto) {
        return R.okData(baseService.deleteById(dto));
    }

}