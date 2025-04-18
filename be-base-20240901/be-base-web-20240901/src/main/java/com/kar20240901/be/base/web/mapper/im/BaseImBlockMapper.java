package com.kar20240901.be.base.web.mapper.im;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.im.BaseImBlockDO;
import com.kar20240901.be.base.web.model.dto.im.BaseImBlockGroupPageDTO;
import com.kar20240901.be.base.web.model.vo.im.BaseImBlockGroupPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BaseImBlockMapper extends BaseMapper<BaseImBlockDO> {

    Page<BaseImBlockGroupPageVO> groupPage(@Param("page") Page<Object> page, @Param("dto") BaseImBlockGroupPageDTO dto);

}
