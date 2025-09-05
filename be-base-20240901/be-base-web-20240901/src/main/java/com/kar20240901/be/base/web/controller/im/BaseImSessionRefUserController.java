package com.kar20240901.be.base.web.controller.im;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.base.ScrollListDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSessionRefUserAddNotDisturbDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSessionRefUserDeleteNotDisturbDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSessionRefUserPageDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.im.BaseImSessionRefUserPageVO;
import com.kar20240901.be.base.web.model.vo.im.BaseImSessionRefUserQueryLastContentVO;
import com.kar20240901.be.base.web.model.vo.im.BaseImSessionRefUserUpdateAvatarAndNicknameVO;
import com.kar20240901.be.base.web.service.im.BaseImSessionRefUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/base/imSessionRefUser")
@Tag(name = "基础-im-会话-关联-用户")
public class BaseImSessionRefUserController {

    @Resource
    BaseImSessionRefUserService baseService;

    @Operation(summary = "分页排序查询")
    @PostMapping("/page")
    public R<Page<BaseImSessionRefUserPageVO>> myPage(@RequestBody @Valid BaseImSessionRefUserPageDTO dto) {
        return R.okData(baseService.myPage(dto));
    }

    @Operation(summary = "滚动加载")
    @PostMapping("/scroll")
    public R<List<BaseImSessionRefUserPageVO>> scroll(@RequestBody @Valid ScrollListDTO dto) {
        return R.okData(baseService.scroll(dto));
    }

    @Operation(summary = "查询最新消息和未读消息数量")
    @PostMapping("/queryLastContentMap")
    public R<Map<Long, BaseImSessionRefUserQueryLastContentVO>> queryLastContentMap(
        @RequestBody @Valid NotEmptyIdSet dto) {
        return R.okData(baseService.queryLastContentMap(dto));
    }

    @Operation(summary = "隐藏")
    @PostMapping("/hidden")
    public R<String> hidden(@RequestBody @Valid NotNullId dto) {
        return R.okMsg(baseService.hidden(dto));
    }

    @Operation(summary = "更新最后一次打开会话的时间")
    @PostMapping("/updateLastOpenTs")
    public R<String> updateLastOpenTs(@RequestBody @Valid NotNullId dto) {
        return R.okMsg(baseService.updateLastOpenTs(dto));
    }

    @Operation(summary = "更新头像和昵称")
    @PostMapping("/updateAvatarAndNickname")
    public R<List<BaseImSessionRefUserUpdateAvatarAndNicknameVO>> updateAvatarAndNickname(
        @RequestBody @Valid NotEmptyIdSet dto) {
        return R.okData(baseService.updateAvatarAndNickname(dto));
    }

    @Operation(summary = "新增免打扰")
    @PostMapping("/addNotDisturb")
    public R<String> addNotDisturb(@RequestBody @Valid BaseImSessionRefUserAddNotDisturbDTO dto) {
        return R.okMsg(baseService.addNotDisturb(dto));
    }

    @Operation(summary = "删除免打扰")
    @PostMapping("/deleteNotDisturb")
    public R<String> deleteNotDisturb(@RequestBody @Valid BaseImSessionRefUserDeleteNotDisturbDTO dto) {
        return R.okMsg(baseService.deleteNotDisturb(dto));
    }

}