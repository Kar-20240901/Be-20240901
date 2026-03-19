package com.kar20240901.be.base.web.service.bulletin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.bulletin.BaseBulletinDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.bulletin.BaseBulletinInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.bulletin.BaseBulletinPageDTO;
import com.kar20240901.be.base.web.model.dto.bulletin.BaseBulletinUserSelfPageDTO;

public interface BaseBulletinService extends IService<BaseBulletinDO> {

    String insertOrUpdate(BaseBulletinInsertOrUpdateDTO dto);

    Page<BaseBulletinDO> myPage(BaseBulletinPageDTO dto);

    BaseBulletinDO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

    String publish(NotNullId notNullId);

    String revoke(NotNullId notNullId);

    Page<BaseBulletinDO> userSelfPage(BaseBulletinUserSelfPageDTO dto);

    BaseBulletinDO userSelfInfoById(NotNullId notNullId);

    Long userSelfCount();

    String userSelfUpdateReadTime();

}
