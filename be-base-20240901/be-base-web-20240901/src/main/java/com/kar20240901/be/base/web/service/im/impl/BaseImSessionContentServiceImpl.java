package com.kar20240901.be.base.web.service.im.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.im.BaseImSessionContentMapper;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionContentDO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSessionContentInsertTxtDTO;
import com.kar20240901.be.base.web.service.im.BaseImSessionContentService;
import org.springframework.stereotype.Service;

@Service
public class BaseImSessionContentServiceImpl extends ServiceImpl<BaseImSessionContentMapper, BaseImSessionContentDO>
    implements BaseImSessionContentService {

    /**
     * 新增文字消息
     */
    @Override
    public String insertTxt(BaseImSessionContentInsertTxtDTO dto) {

        return TempBizCodeEnum.OK;

    }

}
