package com.kar20240901.be.base.web.service.file.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.file.SysFileMapper;
import com.kar20240901.be.base.web.model.annotation.base.MyTransactional;
import com.kar20240901.be.base.web.model.bo.file.SysFileUploadBO;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoId;
import com.kar20240901.be.base.web.model.domain.file.SysFileDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.file.SysFilePageDTO;
import com.kar20240901.be.base.web.model.dto.file.SysFilePageSelfDTO;
import com.kar20240901.be.base.web.model.dto.file.SysFileUploadDTO;
import com.kar20240901.be.base.web.model.vo.base.LongObjectMapVO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.file.SysFileService;
import com.kar20240901.be.base.web.util.base.CallBack;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.base.ResponseUtil;
import com.kar20240901.be.base.web.util.file.SysFileUtil;
import java.io.InputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFileDO> implements SysFileService {

    /**
     * 上传文件：公有和私有
     */
    @Override
    public Long upload(SysFileUploadDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        SysFileUploadBO sysFileUploadBO = new SysFileUploadBO();

        sysFileUploadBO.setFile(dto.getFile());
        sysFileUploadBO.setUploadType(dto.getUploadType());
        sysFileUploadBO.setRemark(dto.getRemark());
        sysFileUploadBO.setExtraJson(dto.getExtraJson());

        sysFileUploadBO.setRefId(dto.getRefId());

        sysFileUploadBO.setUserId(currentUserId);

        // 执行：上传
        return SysFileUtil.upload(sysFileUploadBO);

    }

    /**
     * 下载文件：私有
     */
    @SneakyThrows
    @Override
    public void privateDownload(NotNullId notNullId, HttpServletResponse response) {

        CallBack<String> fileNameCallBack = new CallBack<>();

        InputStream inputStream = SysFileUtil.privateDownload(notNullId.getId(), fileNameCallBack);

        if (inputStream == null) {
            R.errorMsg("操作失败：文件流获取失败");
        }

        ResponseUtil.getOutputStream(response, fileNameCallBack.getValue());

        // 推送
        ResponseUtil.flush(response, inputStream);

    }

    /**
     * 批量删除文件：公有和私有
     */
    @Override
    @MyTransactional
    public String removeByFileIdSet(NotEmptyIdSet notEmptyIdSet, boolean checkBelongFlag) {

        if (CollUtil.isEmpty(notEmptyIdSet.getIdSet())) {
            return TempBizCodeEnum.OK;
        }

        // 执行
        SysFileUtil.removeByFileIdSet(notEmptyIdSet.getIdSet(), checkBelongFlag);

        return TempBizCodeEnum.OK;

    }

    /**
     * 批量获取：公开文件的 url
     */
    @Override
    public LongObjectMapVO<String> getPublicUrl(NotEmptyIdSet notEmptyIdSet) {

        return new LongObjectMapVO<>(SysFileUtil.getPublicUrl(notEmptyIdSet.getIdSet()));

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<SysFileDO> myPage(SysFilePageDTO dto) {

        return lambdaQuery() //
            .like(StrUtil.isNotBlank(dto.getOriginFileName()), SysFileDO::getOriginFileName, dto.getOriginFileName()) //
            .like(StrUtil.isNotBlank(dto.getRemark()), TempEntityNoId::getRemark, dto.getRemark()) //
            .eq(dto.getBelongId() != null, SysFileDO::getBelongId, dto.getBelongId()) //
            .eq(dto.getUploadType() != null, SysFileDO::getUploadType, dto.getUploadType()) //
            .eq(dto.getStorageType() != null, SysFileDO::getStorageType, dto.getStorageType()) //
            .eq(dto.getPublicFlag() != null, SysFileDO::getPublicFlag, dto.getPublicFlag()) //
            .eq(dto.getEnableFlag() != null, TempEntity::getEnableFlag, dto.getEnableFlag()) //
            .eq(dto.getRefId() != null, SysFileDO::getRefId, dto.getRefId()) //
            .select(TempEntity::getId, TempEntityNoId::getEnableFlag, TempEntityNoId::getRemark,
                TempEntityNoId::getCreateId, TempEntityNoId::getCreateTime, TempEntityNoId::getUpdateId,
                TempEntityNoId::getUpdateTime, SysFileDO::getOriginFileName, SysFileDO::getBelongId,
                SysFileDO::getUploadType, SysFileDO::getStorageType, SysFileDO::getPublicFlag, SysFileDO::getFileSize,
                SysFileDO::getExtraJson, SysFileDO::getRefId, SysFileDO::getRefFileId)
            .orderByDesc(TempEntity::getUpdateTime).page(dto.pageOrder());

    }

    /**
     * 分页排序查询-自我
     */
    @Override
    public Page<SysFileDO> myPageSelf(SysFilePageSelfDTO dto) {

        SysFilePageDTO sysFilePageDTO = BeanUtil.copyProperties(dto, SysFilePageDTO.class);

        Long currentUserId = MyUserUtil.getCurrentUserId();

        sysFilePageDTO.setBelongId(currentUserId); // 设置为：当前用户

        // 执行
        return myPage(sysFilePageDTO);

    }

}
