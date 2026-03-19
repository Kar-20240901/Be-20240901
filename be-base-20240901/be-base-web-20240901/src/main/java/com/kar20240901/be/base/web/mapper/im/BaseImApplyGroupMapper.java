package com.kar20240901.be.base.web.mapper.im;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.im.BaseImApplyGroupDO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupPageGroupDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupPageSelfDTO;
import com.kar20240901.be.base.web.model.vo.im.BaseImApplyGroupPageGroupVO;
import com.kar20240901.be.base.web.model.vo.im.BaseImApplyGroupPageSelfVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BaseImApplyGroupMapper extends BaseMapper<BaseImApplyGroupDO> {

    Page<BaseImApplyGroupPageSelfVO> myPageSelf(@Param("page") Page<BaseImApplyGroupPageSelfVO> page,
        @Param("dto") BaseImApplyGroupPageSelfDTO dto, @Param("currentUserId") Long currentUserId);

    Page<BaseImApplyGroupPageGroupVO> myPageGroup(@Param("page") Page<BaseImApplyGroupPageGroupVO> page,
        @Param("dto") BaseImApplyGroupPageGroupDTO dto);

}
