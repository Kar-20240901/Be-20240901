package com.kar20240901.be.base.web.service.base.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.base.BaseEmailConfigurationMapper;
import com.kar20240901.be.base.web.model.domain.base.BaseEmailConfigurationDO;
import com.kar20240901.be.base.web.model.dto.base.BaseEmailConfigurationInsertOrUpdateDTO;
import com.kar20240901.be.base.web.service.base.BaseEmailConfigurationService;
import org.springframework.stereotype.Service;

@Service
public class BaseEmailConfigurationServiceImpl
    extends ServiceImpl<BaseEmailConfigurationMapper, BaseEmailConfigurationDO>
    implements BaseEmailConfigurationService {

    /**
     * 新增/修改
     */
    @Override
    public String insertOrUpdate(BaseEmailConfigurationInsertOrUpdateDTO dto) {

        BaseEmailConfigurationDO baseEmailConfigurationDO = lambdaQuery().one();

        boolean insertFlag = baseEmailConfigurationDO == null;

        if (insertFlag) {

            baseEmailConfigurationDO = new BaseEmailConfigurationDO();

            Assert.notBlank(dto.getPass(), "操作失败：第一次设置时，密码不能为空");

        }

        baseEmailConfigurationDO.setPort(dto.getPort());
        baseEmailConfigurationDO.setFromEmail(dto.getFromEmail());
        baseEmailConfigurationDO.setPass(dto.getPass());
        baseEmailConfigurationDO.setSslFlag(BooleanUtil.isTrue(dto.getSslFlag()));
        baseEmailConfigurationDO.setContentPre(dto.getContentPre());

        if (insertFlag) {

            save(baseEmailConfigurationDO);

        } else {

            updateById(baseEmailConfigurationDO);

        }

        return TempBizCodeEnum.OK;

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseEmailConfigurationDO infoById() {

        BaseEmailConfigurationDO baseEmailConfigurationDO = lambdaQuery().one();

        if (baseEmailConfigurationDO != null) {
            baseEmailConfigurationDO.setPass(null); // 不返回密码
        }

        return baseEmailConfigurationDO;

    }

}
