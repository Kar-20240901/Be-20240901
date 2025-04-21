package com.kar20240901.be.base.web.service.im.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.im.BaseImFriendMapper;
import com.kar20240901.be.base.web.model.annotation.base.MyTransactional;
import com.kar20240901.be.base.web.model.domain.im.BaseImFriendDO;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.service.im.BaseImFriendService;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class BaseImFriendServiceImpl extends ServiceImpl<BaseImFriendMapper, BaseImFriendDO>
    implements BaseImFriendService {

    /**
     * 添加好友
     *
     * @param userId1 和 userId2 可以随便传
     */
    @MyTransactional
    @Override
    public void addFriend(Long userId1, Long userId2, Long sessionId) {

        Date date = new Date();

        BaseImFriendDO baseImFriendDo1 = new BaseImFriendDO();

        baseImFriendDo1.setCreateTime(date);
        baseImFriendDo1.setBelongId(userId1);
        baseImFriendDo1.setFriendId(userId2);
        baseImFriendDo1.setSessionId(sessionId);

        save(baseImFriendDo1);

        BaseImFriendDO baseImFriendDo2 = new BaseImFriendDO();

        baseImFriendDo2.setCreateTime(date);
        baseImFriendDo2.setBelongId(userId2);
        baseImFriendDo2.setFriendId(userId1);
        baseImFriendDo2.setSessionId(sessionId);

        save(baseImFriendDo2);

    }

    /**
     * 删除好友
     */
    @Override
    public String remove(NotNullId dto) {

        return TempBizCodeEnum.OK;

    }

}
