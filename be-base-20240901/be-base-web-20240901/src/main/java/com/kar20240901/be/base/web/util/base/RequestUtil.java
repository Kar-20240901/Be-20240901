package com.kar20240901.be.base.web.util.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.func.VoidFunc0;
import cn.hutool.extra.servlet.ServletUtil;
import com.kar20240901.be.base.web.model.constant.base.SecurityConstant;
import com.kar20240901.be.base.web.model.domain.request.BaseRequestDO;
import com.kar20240901.be.base.web.model.domain.request.BaseRequestInfoDO;
import com.kar20240901.be.base.web.model.enums.base.BaseRequestCategoryEnum;
import com.kar20240901.be.base.web.service.request.BaseRequestInfoService;
import com.kar20240901.be.base.web.service.request.BaseRequestService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Slf4j
public class RequestUtil {

    public static final String[] IP_HEADER_ARR =
        {"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"};

    @Resource
    BaseRequestService baseRequestService;

    @Resource
    BaseRequestInfoService baseRequestInfoService;

    private static CopyOnWriteArrayList<BaseRequestDO> BASE_REQUEST_DO_INSERT_LIST = new CopyOnWriteArrayList<>();

    private static CopyOnWriteArrayList<BaseRequestInfoDO> BASE_REQUEST_INFO_DO_INSERT_LIST =
        new CopyOnWriteArrayList<>();

    /**
     * 添加一个：请求数据
     */
    public static void add(BaseRequestDO baseRequestDO, BaseRequestInfoDO baseRequestInfoDO) {

        BASE_REQUEST_DO_INSERT_LIST.add(baseRequestDO);

        baseRequestInfoDO.setUri(baseRequestDO.getUri());
        baseRequestInfoDO.setCostMs(baseRequestDO.getCostMs());
        baseRequestInfoDO.setSuccessFlag(baseRequestDO.getSuccessFlag());

        BASE_REQUEST_INFO_DO_INSERT_LIST.add(baseRequestInfoDO);

    }

    /**
     * 定时任务，保存数据
     */
    @PreDestroy
    @Scheduled(fixedDelay = 5000)
    public void scheduledSava() {

        List<VoidFunc0> voidFunc0List = new ArrayList<>();

        // 处理：BASE_REQUEST_DO_INSERT_LIST
        handleBaseRequestDoInsertList(voidFunc0List);

        // 处理：BASE_REQUEST_INFO_DO_INSERT_LIST
        handleBaseRequestInfoDoInsertList(voidFunc0List);

        if (CollUtil.isEmpty(voidFunc0List)) {
            return;
        }

        // 目的：防止还有程序往：tempList，里面添加数据，所以这里等待一会
        MyThreadUtil.schedule(() -> {

            for (VoidFunc0 item : voidFunc0List) {

                item.call();

            }

        }, DateUtil.offsetMillisecond(new Date(), 1500));

    }

    private void handleBaseRequestDoInsertList(List<VoidFunc0> voidFunc0List) {

        CopyOnWriteArrayList<BaseRequestDO> tempBaseRequestDoList;

        synchronized (BASE_REQUEST_DO_INSERT_LIST) {

            if (CollUtil.isEmpty(BASE_REQUEST_DO_INSERT_LIST)) {
                return;
            }

            tempBaseRequestDoList = BASE_REQUEST_DO_INSERT_LIST;
            BASE_REQUEST_DO_INSERT_LIST = new CopyOnWriteArrayList<>();

        }

        voidFunc0List.add(() -> {

            // 批量操作数据
            baseRequestService.saveBatch(tempBaseRequestDoList);

        });

    }

    private void handleBaseRequestInfoDoInsertList(List<VoidFunc0> voidFunc0List) {

        CopyOnWriteArrayList<BaseRequestInfoDO> tempBaseRequestInfoDoList;

        synchronized (BASE_REQUEST_INFO_DO_INSERT_LIST) {

            if (CollUtil.isEmpty(BASE_REQUEST_INFO_DO_INSERT_LIST)) {
                return;
            }

            tempBaseRequestInfoDoList = BASE_REQUEST_INFO_DO_INSERT_LIST;
            BASE_REQUEST_INFO_DO_INSERT_LIST = new CopyOnWriteArrayList<>();

        }

        voidFunc0List.add(() -> {

            // 批量操作数据
            baseRequestInfoService.saveBatch(tempBaseRequestInfoDoList);

        });

    }

    /**
     * 获取当前上下文的 request对象
     */
    @Nullable
    public static HttpServletRequest getRequest() {

        ServletRequestAttributes requestAttributes =
            (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();

        if (requestAttributes == null) {
            return null;
        }

        return requestAttributes.getRequest();

    }

    /**
     * 获取请求类别
     */
    @NotNull
    public static BaseRequestCategoryEnum getRequestCategoryEnum() {

        return getRequestCategoryEnum(getRequest());

    }

    /**
     * 获取请求类别
     */
    @NotNull
    public static BaseRequestCategoryEnum getRequestCategoryEnum(@Nullable HttpServletRequest httpServletRequest) {

        if (httpServletRequest == null) {
            return BaseRequestCategoryEnum.PC_BROWSER_WINDOWS;
        }

        return BaseRequestCategoryEnum.getByCode(
            Convert.toInt(httpServletRequest.getHeader(SecurityConstant.REQUEST_HEADER_CATEGORY)));

    }

    /**
     * 获取：ip
     */
    @NotNull
    public static String getIp() {

        return getIp(getRequest());

    }

    /**
     * 获取：ip
     */
    @NotNull
    public static String getIp(HttpServletRequest httpServletRequest) {

        if (httpServletRequest == null) {
            return "";
        }

        return ServletUtil.getClientIP(httpServletRequest);

    }

}
