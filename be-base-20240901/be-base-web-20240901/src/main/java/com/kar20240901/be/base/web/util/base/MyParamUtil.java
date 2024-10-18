package com.kar20240901.be.base.web.util.base;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.mapper.base.BaseParamMapper;
import com.kar20240901.be.base.web.model.constant.base.ParamConstant;
import com.kar20240901.be.base.web.model.domain.base.BaseParamDO;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoId;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

/**
 * 系统参数 工具类
 */
@Component
@Slf4j
public class MyParamUtil {

    // 系统内置参数 uuidSet，备注：不允许删除
    // 备注：系统内置参数的 uuid等于 id
    public static final Set<String> SYSTEM_PARAM_UUID_SET =
        CollUtil.newHashSet(ParamConstant.RSA_PRIVATE_KEY_UUID, ParamConstant.IP_REQUESTS_PER_SECOND_UUID);

    // 不允许删除的：参数主键 id
    public static final Set<String> SYSTEM_PARAM_NOT_DELETE_ID_SET =
        (Set<String>)CollUtil.addAll(new HashSet<>(SYSTEM_PARAM_UUID_SET),
            CollUtil.newHashSet(ParamConstant.DEFAULT_MANAGE_SIGN_IN_FLAG));

    private static BaseParamMapper baseParamMapper;

    public MyParamUtil(BaseParamMapper baseParamMapper) {
        MyParamUtil.baseParamMapper = baseParamMapper;
    }

    /**
     * 通过：参数的 uuid，获取 value，没有 value则返回 null 备注：请不要直接传字符串，请在：ParamConstant 类里面加一个常量
     */
    @Nullable
    public static String getValueByUuid(String paramUuid) {

        BaseParamDO baseParamDO = ChainWrappers.lambdaQueryChain(baseParamMapper).select(BaseParamDO::getValue)
            .eq(BaseParamDO::getUuid, paramUuid).eq(TempEntityNoId::getEnableFlag, true).one();

        if (baseParamDO == null) {
            return null;
        }

        return baseParamDO.getValue();

    }

}
