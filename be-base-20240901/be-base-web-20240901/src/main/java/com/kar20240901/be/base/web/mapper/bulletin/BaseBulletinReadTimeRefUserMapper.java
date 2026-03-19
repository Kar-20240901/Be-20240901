package com.kar20240901.be.base.web.mapper.bulletin;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kar20240901.be.base.web.model.domain.bulletin.BaseBulletinReadTimeRefUserDO;
import java.util.Date;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BaseBulletinReadTimeRefUserMapper extends BaseMapper<BaseBulletinReadTimeRefUserDO> {

    void insertOrUpdateReadTime(@Param("userId") Long userId, @Param("readTime") Date readTime);

}
