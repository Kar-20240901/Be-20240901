package com.kar20240901.be.base.web.controller.im;

import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupSendDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.im.BaseImApplyGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/base/imApplyGroup")
@Tag(name = "基础-im-群组-申请")
public class BaseImApplyGroupController {

    @Resource
    BaseImApplyGroupService baseService;

    @Operation(summary = "新增/修改")
    @PostMapping("/insertOrUpdate")
    public R<String> insertOrUpdate(@RequestBody @Valid BaseImApplyGroupInsertOrUpdateDTO dto) {
        return R.okData(baseService.insertOrUpdate(dto));
    }

    @Operation(summary = "发送群组申请")
    @PostMapping("/send")
    public R<String> send(@RequestBody @Valid BaseImApplyGroupSendDTO dto) {
        return R.okData(baseService.send(dto));
    }

}