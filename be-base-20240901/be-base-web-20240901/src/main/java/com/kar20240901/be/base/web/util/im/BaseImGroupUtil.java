package com.kar20240901.be.base.web.util.im;

import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.mapper.im.BaseImGroupMapper;
import com.kar20240901.be.base.web.model.domain.im.BaseImGroupDO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class BaseImGroupUtil {

    private static BaseImGroupMapper baseImGroupMapper;

    @Resource
    public void setBaseImGroupMapper(BaseImGroupMapper baseImGroupMapper) {
        BaseImGroupUtil.baseImGroupMapper = baseImGroupMapper;
    }

    /**
     * 检查：是否有权限
     */
    public static void checkGroupAuth(Long groupId) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        boolean exists = ChainWrappers.lambdaQueryChain(baseImGroupMapper).eq(BaseImGroupDO::getBelongId, currentUserId)
            .eq(BaseImGroupDO::getId, groupId).exists();

        if (!exists) {
            R.error("操作失败：只能群主进行该操作", groupId);
        }

    }

}
