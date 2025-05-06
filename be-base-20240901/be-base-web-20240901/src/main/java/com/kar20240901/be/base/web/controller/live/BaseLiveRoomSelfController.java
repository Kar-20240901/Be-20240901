package com.kar20240901.be.base.web.controller.live;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.im.BaseLiveRoomDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.live.BaseLiveRoomSelfInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.live.BaseLiveRoomSelfPageDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.live.BaseLiveRoomSelfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/base/liveRoomSelf")
@Tag(name = "基础-实时-房间-管理-自我")
public class BaseLiveRoomSelfController {

    @Resource
    BaseLiveRoomSelfService baseService;

    @Operation(summary = "新增/修改")
    @PostMapping("/insertOrUpdate")
    public R<String> insertOrUpdate(@RequestBody @Valid BaseLiveRoomSelfInsertOrUpdateDTO dto) {
        return R.okMsg(baseService.insertOrUpdate(dto));
    }

    @Operation(summary = "分页排序查询")
    @PostMapping("/page")
    public R<Page<BaseLiveRoomDO>> myPage(@RequestBody @Valid BaseLiveRoomSelfPageDTO dto) {
        return R.okData(baseService.myPage(dto));
    }

    @Operation(summary = "通过主键id，查看详情")
    @PostMapping("/infoById")
    public R<BaseLiveRoomDO> infoById(@RequestBody @Valid NotNullId dto) {
        return R.okData(baseService.infoById(dto));
    }

    @Operation(summary = "批量删除")
    @PostMapping("/deleteByIdSet")
    public R<String> deleteByIdSet(@RequestBody @Valid NotEmptyIdSet dto) {
        return R.okMsg(baseService.deleteByIdSet(dto));
    }

    @Operation(summary = "刷新验证码")
    @PostMapping("/refreshCode")
    public R<String> refreshCode(@RequestBody @Valid NotNullId dto) {
        return R.okMsg(baseService.refreshCode(dto));
    }

}
