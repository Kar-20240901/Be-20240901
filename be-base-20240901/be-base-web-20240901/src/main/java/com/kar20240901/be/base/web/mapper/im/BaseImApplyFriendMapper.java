package com.kar20240901.be.base.web.mapper.im;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.im.BaseImApplyFriendDO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyFriendPageDTO;
import com.kar20240901.be.base.web.model.vo.im.BaseImApplyFriendPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BaseImApplyFriendMapper extends BaseMapper<BaseImApplyFriendDO> {

    Page<BaseImApplyFriendPageVO> myPage(@Param("page") Page<BaseImApplyFriendPageVO> page,
        @Param("dto") BaseImApplyFriendPageDTO dto, @Param("currentUserId") Long currentUserId);

}
