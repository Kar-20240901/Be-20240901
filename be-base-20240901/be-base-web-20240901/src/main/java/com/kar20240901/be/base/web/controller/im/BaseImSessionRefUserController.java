package com.kar20240901.be.base.web.controller.im;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.im.BaseImSessionRefUserPageDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.im.BaseImSessionRefUserPageVO;
import com.kar20240901.be.base.web.service.im.BaseImSessionRefUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @PostMapping("/myPage")
    public R<Page<BaseImSessionRefUserPageVO>> myPage(@RequestBody @Valid BaseImSessionRefUserPageDTO dto) {
        return R.okData(baseService.myPage(dto));
    }

    @Operation(summary = "隐藏")
    @PostMapping("/hidden")
    public R<String> hidden(@RequestBody @Valid NotNullId dto) {
        return R.okMsg(baseService.hidden(dto));
    }

}