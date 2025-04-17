package com.kar20240901.be.base.web.controller.im;

import com.kar20240901.be.base.web.service.im.BaseImBlockService;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/base/imBlock")
@Tag(name = "基础-im-拉黑-管理")
public class BaseImBlockController {

    @Resource
    BaseImBlockService baseService;

}