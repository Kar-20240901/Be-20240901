package com.kar20240901.be.base.web.util.sms;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.kar20240901.be.base.web.model.bo.sms.BaseSmsSendBO;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoId;
import com.kar20240901.be.base.web.model.domain.sms.BaseSmsConfigurationDO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.sms.BaseSmsConfigurationService;
import java.util.List;
import javax.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class BaseSmsHelper {

    private static BaseSmsConfigurationService baseSmsConfigurationService;

    @Resource
    public void setSysSmsConfigurationService(BaseSmsConfigurationService baseSmsConfigurationService) {
        BaseSmsHelper.baseSmsConfigurationService = baseSmsConfigurationService;
    }

    /**
     * 获取：BaseSmsSendBO对象
     */
    public static BaseSmsSendBO getBaseSmsSendBO(String sendContent, String phoneNumber) {

        BaseSmsSendBO baseSmsSendBO = new BaseSmsSendBO();

        baseSmsSendBO.setSendContent(sendContent);
        baseSmsSendBO.setPhoneNumber(phoneNumber);

        return baseSmsSendBO;

    }

    /**
     * 获取：SysSmsSendBO对象
     */
    public static BaseSmsSendBO getBaseSmsSendBO(String sendContent, String phoneNumber, Long id) {

        if (id == null) {
            R.errorMsg("系统错误：短信配置 id为空");
        }

        BaseSmsConfigurationDO baseSmsConfigurationDO =
            baseSmsConfigurationService.lambdaQuery().eq(TempEntity::getId, id).one();

        if (baseSmsConfigurationDO == null) {
            R.error("未找到短信配置", id);
        }

        BaseSmsSendBO baseSmsSendBO = new BaseSmsSendBO();

        baseSmsSendBO.setBaseSmsConfigurationDO(baseSmsConfigurationDO);

        baseSmsSendBO.setSendContent(sendContent);
        baseSmsSendBO.setPhoneNumber(phoneNumber);

        return baseSmsSendBO;

    }

    /**
     * 获取：SysSmsConfigurationDO对象
     */
    @NotNull
    public static BaseSmsConfigurationDO getBaseSmsConfigurationDO(Integer sysSmsType) {

        List<BaseSmsConfigurationDO> baseSmsConfigurationDOList =
            baseSmsConfigurationService.lambdaQuery().eq(TempEntityNoId::getEnableFlag, true)
                .eq(BaseSmsConfigurationDO::getType, sysSmsType).list();

        BaseSmsConfigurationDO baseSmsConfigurationDO = null;

        if (CollUtil.isEmpty(baseSmsConfigurationDOList)) {

            R.error("操作失败：暂未配置短信", sysSmsType);

        } else {

            // 随机取一个
            baseSmsConfigurationDO = RandomUtil.randomEle(baseSmsConfigurationDOList);

        }

        return baseSmsConfigurationDO;

    }

    /**
     * 获取：默认短信
     */
    @NotNull
    public static BaseSmsConfigurationDO getDefaultBaseSmsConfigurationDO() {

        BaseSmsConfigurationDO baseSmsConfigurationDO =
            baseSmsConfigurationService.lambdaQuery().eq(BaseSmsConfigurationDO::getDefaultFlag, true)
                .eq(TempEntityNoId::getEnableFlag, true).one();

        if (baseSmsConfigurationDO == null) {

            R.errorMsg("操作失败：未配置默认短信方式，请联系管理员");

        }

        return baseSmsConfigurationDO;

    }

    /**
     * 处理：短信方式
     */
    public static void handleSysSmsConfigurationDO(BaseSmsSendBO baseSmsSendBO) {

        BaseSmsConfigurationDO baseSmsConfigurationDO = baseSmsSendBO.getBaseSmsConfigurationDO();

        if (baseSmsConfigurationDO == null) {

            if (baseSmsSendBO.getSmsType() == null) { // 如果是：默认短信

                baseSmsConfigurationDO = getDefaultBaseSmsConfigurationDO();

            } else {

                baseSmsConfigurationDO = getBaseSmsConfigurationDO(baseSmsSendBO.getSmsType());

            }

        }

        baseSmsSendBO.setSmsType(baseSmsConfigurationDO.getType());
        baseSmsSendBO.setBaseSmsConfigurationDO(baseSmsConfigurationDO);

    }

}
