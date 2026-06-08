package com.kar20240901.be.base.web.controller.server;

import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.server.BaseServerWorkInfoVO;
import com.kar20240901.be.base.web.service.server.BaseServerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/base/server")
@Tag(name = "基础-服务器-管理")
public class BaseServerController {

    @Resource
    BaseServerService baseService;

    @PostMapping("/workInfo")
    @Operation(summary = "服务器运行情况")
    public R<BaseServerWorkInfoVO> workInfo() {
        return R.okData(baseService.workInfo());
    }

}
