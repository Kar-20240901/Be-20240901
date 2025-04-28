package com.kar20240901.be.base.web.service.im.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.mapper.im.BaseImSessionContentRefUserMapper;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionContentRefUserDO;
import com.kar20240901.be.base.web.model.dto.base.ScrollListDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSessionContentRefUserPageDTO;
import com.kar20240901.be.base.web.model.vo.im.BaseImSessionContentRefUserPageVO;
import com.kar20240901.be.base.web.service.im.BaseImSessionContentRefUserService;
import com.kar20240901.be.base.web.util.base.MyPageUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BaseImSessionContentRefUserServiceImpl
    extends ServiceImpl<BaseImSessionContentRefUserMapper, BaseImSessionContentRefUserDO>
    implements BaseImSessionContentRefUserService {

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseImSessionContentRefUserPageVO> myPage(BaseImSessionContentRefUserPageDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        return baseMapper.myPage(dto.pageOrder(), dto, currentUserId);

    }

    /**
     * 滚动加载
     */
    @Override
    public List<BaseImSessionContentRefUserPageVO> scroll(ScrollListDTO dto) {

        Assert.notNull(dto.getRefId());

        Long currentUserId = MyUserUtil.getCurrentUserId();

        Long contentId = dto.getId();

        boolean backwardFlag = BooleanUtil.isTrue(dto.getBackwardFlag());

        if (contentId == null) {

            if (backwardFlag) { // 最小的 id

                contentId = Long.MIN_VALUE;

            } else { // 最大的 id

                contentId = Long.MAX_VALUE;

            }

        }

        BaseImSessionContentRefUserPageDTO pageDTO = new BaseImSessionContentRefUserPageDTO();

        pageDTO.setBackwardFlag(backwardFlag);

        pageDTO.setContentId(contentId);

        pageDTO.setContent(dto.getSearchKey());

        pageDTO.setSessionId(dto.getRefId());

        return baseMapper.myPage(MyPageUtil.getScrollPage(dto.getPageSize()), pageDTO, currentUserId).getRecords();

    }

}
