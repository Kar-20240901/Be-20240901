package com.kar20240901.be.base.web.mapper.im;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.im.BaseImFriendDO;
import com.kar20240901.be.base.web.model.dto.im.BaseImFriendPageDTO;
import com.kar20240901.be.base.web.model.vo.im.BaseImFriendPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BaseImFriendMapper extends BaseMapper<BaseImFriendDO> {

    Page<BaseImFriendPageVO> myPage(@Param("page") Page<BaseImFriendPageVO> page, @Param("dto") BaseImFriendPageDTO dto,
        @Param("currentUserId") Long currentUserId, @Param("imTypeCode") int imTypeCode);

}
