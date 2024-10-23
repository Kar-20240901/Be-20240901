package com.kar20240901.be.base.web.util.base;

import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.mapper.base.BaseDictMapper;
import com.kar20240901.be.base.web.model.domain.base.BaseDictDO;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoId;
import com.kar20240901.be.base.web.model.enums.base.BaseDictTypeEnum;
import com.kar20240901.be.base.web.model.vo.base.DictIntegerVO;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 系统字典 工具类
 */
@Component
@Slf4j
public class MyDictUtil {

    private static BaseDictMapper baseDictMapper;

    @Resource
    public void setBaseDictMapper(BaseDictMapper baseDictMapper) {
        MyDictUtil.baseDictMapper = baseDictMapper;
    }

    /**
     * 通过：dictKey获取字典项集合
     */
    public static List<DictIntegerVO> listByDictKey(String dictKey) {

        return ChainWrappers.lambdaQueryChain(baseDictMapper).eq(BaseDictDO::getType, BaseDictTypeEnum.DICT_ITEM)
            .eq(TempEntityNoId::getEnableFlag, true) //
            .eq(BaseDictDO::getDictKey, dictKey).select(BaseDictDO::getValue, BaseDictDO::getName) //
            .orderByDesc(BaseDictDO::getOrderNo).list() //
            .stream().map(it -> new DictIntegerVO(it.getValue(), it.getName())).collect(Collectors.toList());

    }

}
