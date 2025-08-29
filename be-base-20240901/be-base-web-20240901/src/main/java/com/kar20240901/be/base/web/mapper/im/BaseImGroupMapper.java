package com.kar20240901.be.base.web.mapper.im;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.im.BaseImGroupDO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupPageDTO;
import com.kar20240901.be.base.web.model.vo.im.BaseImGroupPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BaseImGroupMapper extends BaseMapper<BaseImGroupDO> {

    Page<BaseImGroupPageVO> myPage(@Param("page") Page<?> page, @Param("dto") BaseImGroupPageDTO dto,
        @Param("currentUserId") Long currentUserId);

}
