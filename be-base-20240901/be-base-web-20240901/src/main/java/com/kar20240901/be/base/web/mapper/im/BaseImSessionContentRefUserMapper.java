package com.kar20240901.be.base.web.mapper.im;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionContentRefUserDO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSessionContentRefUserPageDTO;
import com.kar20240901.be.base.web.model.vo.im.BaseImSessionContentRefUserPageVO;
import com.kar20240901.be.base.web.model.vo.im.BaseImSessionRefUserQueryLastContentVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BaseImSessionContentRefUserMapper extends BaseMapper<BaseImSessionContentRefUserDO> {

    List<BaseImSessionRefUserQueryLastContentVO> queryLastContent(@Param("sessionIdList") List<Long> sessionIdList,
        @Param("currentUserId") Long currentUserId);

    Page<BaseImSessionContentRefUserPageVO> myPage(@Param("page") Page<BaseImSessionContentRefUserPageVO> page,
        @Param("dto") BaseImSessionContentRefUserPageDTO dto, @Param("currentUserId") Long currentUserId);

}
