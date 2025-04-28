package com.kar20240901.be.base.web.mapper.im;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionRefUserDO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSessionRefUserPageDTO;
import com.kar20240901.be.base.web.model.vo.im.BaseImSessionRefUserPageVO;
import com.kar20240901.be.base.web.model.vo.im.BaseImSessionRefUserQueryLastContentVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BaseImSessionRefUserMapper extends BaseMapper<BaseImSessionRefUserDO> {

    Page<BaseImSessionRefUserPageVO> myPage(@Param("page") Page<BaseImSessionRefUserPageVO> page,
        @Param("dto") BaseImSessionRefUserPageDTO dto, @Param("currentUserId") Long currentUserId);

    List<BaseImSessionRefUserQueryLastContentVO> queryUnReadCount(@Param("sessionIdList") List<Long> sessionIdList,
        @Param("currentUserId") Long currentUserId);

}
