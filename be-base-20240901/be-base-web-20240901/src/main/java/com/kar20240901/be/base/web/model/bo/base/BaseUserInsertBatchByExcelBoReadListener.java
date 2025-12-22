package com.kar20240901.be.base.web.model.bo.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.kar20240901.be.base.web.model.dto.base.BaseUserInsertOrUpdateDTO;
import com.kar20240901.be.base.web.service.base.BaseUserService;
import com.kar20240901.be.base.web.util.base.MyRsaUtil;
import com.kar20240901.be.base.web.util.base.MyTryUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * 批量注册用户-处理器
 */
public class BaseUserInsertBatchByExcelBoReadListener implements ReadListener<BaseUserInsertBatchByExcelBO> {

    private static final int BATCH_SIZE = 100;

    private final List<BaseUserInsertBatchByExcelBO> baseUserInsertBatchByExcelBOList = new ArrayList<>(BATCH_SIZE);

    private final BaseUserService baseUserService;

    public BaseUserInsertBatchByExcelBoReadListener(BaseUserService baseUserService) {
        this.baseUserService = baseUserService;
    }

    /**
     * 每条Excel数据解析时触发
     */
    @Override
    public void invoke(BaseUserInsertBatchByExcelBO data, AnalysisContext context) {

        baseUserInsertBatchByExcelBOList.add(data);

        if (baseUserInsertBatchByExcelBOList.size() >= BATCH_SIZE) {

            // 执行：用户批量注册
            userBatchInsert();

            baseUserInsertBatchByExcelBOList.clear();

        }

    }

    /**
     * Excel解析完成后触发
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

        if (CollUtil.isEmpty(baseUserInsertBatchByExcelBOList)) {
            return;
        }

        // 执行：用户批量注册
        userBatchInsert();

        baseUserInsertBatchByExcelBOList.clear();

    }

    /**
     * 执行：用户批量注册
     */
    private void userBatchInsert() {

        for (BaseUserInsertBatchByExcelBO item : baseUserInsertBatchByExcelBOList) {

            MyTryUtil.tryCatch(() -> {

                BaseUserInsertOrUpdateDTO baseUserInsertOrUpdateDTO = new BaseUserInsertOrUpdateDTO();

                baseUserInsertOrUpdateDTO.setUsername(item.getUsername());
                baseUserInsertOrUpdateDTO.setEmail(item.getEmail());
                baseUserInsertOrUpdateDTO.setPhone(item.getPhone());
                baseUserInsertOrUpdateDTO.setWxAppId(item.getWxAppId());
                baseUserInsertOrUpdateDTO.setWxOpenId(item.getWxOpenId());
                baseUserInsertOrUpdateDTO.setWxUnionId(item.getWxUnionId());

                baseUserInsertOrUpdateDTO.setNickname(item.getNickname());
                baseUserInsertOrUpdateDTO.setBio(item.getBio());
                baseUserInsertOrUpdateDTO.setEnableFlag(true);

                if (StrUtil.isNotBlank(item.getOriginPassword())) {

                    String originPassword = MyRsaUtil.rsaEncrypt(item.getOriginPassword());

                    item.setOriginPassword(originPassword);

                    String password = DigestUtil.sha256Hex((DigestUtil.sha512Hex(originPassword)));

                    password = MyRsaUtil.rsaEncrypt(password);

                    item.setPassword(password);

                }

                baseUserService.insertOrUpdate(baseUserInsertOrUpdateDTO);

            });

        }

    }

}
