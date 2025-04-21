package com.kar20240901.be.base.web.controller.im;

import com.kar20240901.be.base.web.model.dto.im.BaseImGroupRemoveDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.im.BaseImGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(summary = "踢出群员")
    @PostMapping("/removeUser")
    public R<String> removeUser(@RequestBody @Valid BaseImGroupRemoveDTO dto) {
        return R.okMsg(baseService.removeUser(dto));
    }

}