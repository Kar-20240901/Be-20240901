package com.kar20240901.be.base.web.controller.im;

import com.kar20240901.be.base.web.model.dto.im.BaseImSessionContentInsertTxtDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.im.BaseImSessionContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/base/imSessionContent")
@Tag(name = "基础-im-会话-内容")
public class BaseImSessionContentController {

    @Resource
    BaseImSessionContentService baseService;

    @Operation(summary = "新增文字消息")
    @PostMapping("/insertTxt")
    public R<String> insertTxt(@RequestBody @Valid BaseImSessionContentInsertTxtDTO dto) {
        return R.okData(baseService.insertTxt(dto));
    }

}