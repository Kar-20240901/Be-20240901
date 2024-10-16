package com.kar20240901.be.base.web.controller.websocket;

import com.kar20240901.be.base.web.model.dto.NotNullIdAndIntegerValue;
import com.kar20240901.be.base.web.model.vo.R;
import com.kar20240901.be.base.web.service.socket.NettyWebSocketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Set;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/netty/webSocket")
@Tag(name = "基础-netty-webSocket")
public class NettyWebSocketHttpController {

    @Resource
    NettyWebSocketService baseService;

    @PostMapping(value = "/getAllWebSocketUrl")
    @Operation(summary = "获取：所有 webSocket连接地址，格式：scheme://ip:port/path?code=xxx")
    public R<Set<String>> getAllWebSocketUrl() {
        return R.okData(baseService.getAllWebSocketUrl());
    }

    @PostMapping(value = "/getWebSocketUrlById")
    @Operation(summary = "通过主键 id，获取：webSocket连接地址，格式：scheme://ip:port/path?code=xxx")
    public R<String> getWebSocketUrlById(@RequestBody @Valid NotNullIdAndIntegerValue notNullIdAndIntegerValue) {
        return R.okData(baseService.getWebSocketUrlById(notNullIdAndIntegerValue));
    }

}
