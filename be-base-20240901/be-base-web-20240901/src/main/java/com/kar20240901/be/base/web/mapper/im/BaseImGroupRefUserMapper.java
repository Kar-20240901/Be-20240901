package com.kar20240901.be.base.web.mapper.im;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.im.BaseImGroupRefUserDO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupRefUserMutePageDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupRefUserPageDTO;
import com.kar20240901.be.base.web.model.vo.im.BaseImGroupRefUserPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BaseImGroupRefUserMapper extends BaseMapper<BaseImGroupRefUserDO> {

    Page<BaseImGroupRefUserPageVO> myPage(@Param("page") Page<BaseImGroupRefUserPageVO> page,
        @Param("dto") BaseImGroupRefUserPageDTO dto);

    Page<BaseImGroupRefUserPageVO> pageMute(@Param("page") Page<BaseImGroupRefUserPageVO> page,
        @Param("dto") BaseImGroupRefUserMutePageDTO dto);

}
