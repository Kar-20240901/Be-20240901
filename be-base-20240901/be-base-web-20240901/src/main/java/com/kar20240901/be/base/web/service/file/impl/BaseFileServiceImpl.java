package com.kar20240901.be.base.web.service.file.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.file.BaseFileAuthMapper;
import com.kar20240901.be.base.web.mapper.file.BaseFileMapper;
import com.kar20240901.be.base.web.model.annotation.base.MyTransactional;
import com.kar20240901.be.base.web.model.bo.file.BaseFileUploadBO;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoId;
import com.kar20240901.be.base.web.model.domain.file.BaseFileAuthDO;
import com.kar20240901.be.base.web.model.domain.file.BaseFileDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.file.BaseFileCopySelfDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileMoveSelfDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFilePageDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFilePageSelfDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileUpdateSelfDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileUploadDTO;
import com.kar20240901.be.base.web.model.vo.base.LongObjectMapVO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.file.BaseFileService;
import com.kar20240901.be.base.web.util.base.CallBack;
import com.kar20240901.be.base.web.util.base.MyStrUtil;
import com.kar20240901.be.base.web.util.base.MyThreadUtil;
import com.kar20240901.be.base.web.util.base.MyTreeUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.base.ResponseUtil;
import com.kar20240901.be.base.web.util.file.BaseFileUtil;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class BaseFileServiceImpl extends ServiceImpl<BaseFileMapper, BaseFileDO> implements BaseFileService {

    @Resource
    BaseFileAuthMapper baseFileAuthMapper;

    /**
     * 上传文件：公有和私有
     */
    @Override
    public Long upload(BaseFileUploadDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        BaseFileUploadBO baseFileUploadBO = new BaseFileUploadBO();

        baseFileUploadBO.setFile(dto.getFile());
        baseFileUploadBO.setUploadType(dto.getUploadType());
        baseFileUploadBO.setRemark(MyStrUtil.maxLength(dto.getRemark(), 300));
        baseFileUploadBO.setExtraJson(dto.getExtraJson());

        baseFileUploadBO.setRefId(dto.getRefId());

        baseFileUploadBO.setUserId(currentUserId);

        baseFileUploadBO.setPid(dto.getPid());

        // 执行：上传
        return BaseFileUtil.upload(baseFileUploadBO);

    }

    /**
     * 下载文件：私有
     */
    @SneakyThrows
    @Override
    public void privateDownload(NotNullId notNullId, HttpServletResponse response) {

        CallBack<String> fileNameCallBack = new CallBack<>();

        InputStream inputStream = BaseFileUtil.privateDownload(notNullId.getId(), fileNameCallBack);

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
        BaseFileUtil.removeByFileIdSet(notEmptyIdSet.getIdSet(), checkBelongFlag);

        return TempBizCodeEnum.OK;

    }

    /**
     * 批量获取：公开文件的 url
     */
    @Override
    public LongObjectMapVO<String> getPublicUrl(NotEmptyIdSet notEmptyIdSet) {

        return new LongObjectMapVO<>(BaseFileUtil.getPublicUrl(notEmptyIdSet.getIdSet()));

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseFileDO> myPage(BaseFilePageDTO dto) {

        return lambdaQuery() //
            .like(StrUtil.isNotBlank(dto.getOriginFileName()), BaseFileDO::getOriginFileName,
                dto.getOriginFileName()) //
            .like(StrUtil.isNotBlank(dto.getRemark()), TempEntityNoId::getRemark, dto.getRemark()) //
            .eq(dto.getBelongId() != null, BaseFileDO::getBelongId, dto.getBelongId()) //
            .eq(dto.getUploadType() != null, BaseFileDO::getUploadType, dto.getUploadType()) //
            .eq(dto.getStorageType() != null, BaseFileDO::getStorageType, dto.getStorageType()) //
            .eq(dto.getPublicFlag() != null, BaseFileDO::getPublicFlag, dto.getPublicFlag()) //
            .eq(dto.getEnableFlag() != null, TempEntity::getEnableFlag, dto.getEnableFlag()) //
            .eq(dto.getRefId() != null, BaseFileDO::getRefId, dto.getRefId()) //
            .eq(dto.getPid() != null, BaseFileDO::getPid, dto.getPid()) //
            .select(true, getMyPageSelectList()).page(dto.createTimeDescDefaultOrderPage());

    }

    private static ArrayList<SFunction<BaseFileDO, ?>> getMyPageSelectList() {

        return CollUtil.newArrayList(TempEntity::getId, TempEntityNoId::getEnableFlag, TempEntityNoId::getCreateTime,
            BaseFileDO::getOriginFileName, BaseFileDO::getBelongId, BaseFileDO::getUploadType,
            BaseFileDO::getPublicFlag, BaseFileDO::getFileSize, BaseFileDO::getRefId);

    }

    /**
     * 分页排序查询-自我
     */
    @Override
    public Page<BaseFileDO> myPageSelf(BaseFilePageSelfDTO dto) {

        BaseFilePageDTO baseFilePageDTO = BeanUtil.copyProperties(dto, BaseFilePageDTO.class);

        Long currentUserId = MyUserUtil.getCurrentUserId();

        baseFilePageDTO.setBelongId(currentUserId); // 设置为：当前用户

        // 执行
        return myPage(baseFilePageDTO);

    }

    /**
     * 查询：树结构
     */
    @SneakyThrows
    @Override
    public List<BaseFileDO> tree(BaseFilePageDTO dto) {

        dto.setPageSize(-1); // 不分页

        CountDownLatch countDownLatch = ThreadUtil.newCountDownLatch(1);

        CallBack<List<BaseFileDO>> allListCallBack = new CallBack<>();

        MyThreadUtil.execute(() -> {

            Page<BaseFileDO> page =
                lambdaQuery().select(true, getMyPageSelectList()).page(dto.createTimeDescDefaultOrderPage());

            allListCallBack.setValue(page.getRecords());

        }, countDownLatch);

        // 根据条件进行筛选，得到符合条件的数据，然后再逆向生成整棵树，并返回这个树结构
        List<BaseFileDO> baseFileDoList = myPage(dto).getRecords();

        countDownLatch.await();

        if (baseFileDoList.size() == 0) {
            return new ArrayList<>();
        }

        if (allListCallBack.getValue().size() == 0) {
            return new ArrayList<>();
        }

        return MyTreeUtil.getFullTreeByDeepNode(baseFileDoList, allListCallBack.getValue());

    }

    /**
     * 查询：树结构-自我
     */
    @Override
    public List<BaseFileDO> treeSelf(BaseFilePageSelfDTO dto) {

        BaseFilePageDTO baseFilePageDTO = BeanUtil.copyProperties(dto, BaseFilePageDTO.class);

        Long currentUserId = MyUserUtil.getCurrentUserId();

        baseFilePageDTO.setBelongId(currentUserId); // 设置为：当前用户

        return tree(baseFilePageDTO);

    }

    /**
     * 修改：文件和文件夹-自我
     */
    @Override
    public String updateSelf(BaseFileUpdateSelfDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        lambdaUpdate().eq(TempEntity::getId, dto.getId())
            .eq(!MyUserUtil.getCurrentUserAdminFlag(currentUserId), BaseFileDO::getBelongId, currentUserId)
            .set(BaseFileDO::getShowFileName, dto.getFileName()).update();

        return TempBizCodeEnum.OK;

    }

    /**
     * 移动：文件和文件夹-自我
     */
    @Override
    public String moveSelf(BaseFileMoveSelfDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        boolean exists = ChainWrappers.lambdaQueryChain(baseFileAuthMapper).eq(BaseFileAuthDO::getUserId, currentUserId)
            .eq(!TempConstant.TOP_PID.equals(dto.getPid()), BaseFileAuthDO::getFileId, dto.getPid())
            .eq(BaseFileAuthDO::getWriteFlag, true).exists();

        if (!exists) {
            return TempBizCodeEnum.OK;
        }

        lambdaUpdate().in(TempEntity::getId, dto.getIdSet())
            .eq(!MyUserUtil.getCurrentUserAdminFlag(currentUserId), BaseFileDO::getBelongId, currentUserId)
            .set(BaseFileDO::getPid, dto.getPid()).update();

        return TempBizCodeEnum.OK;

    }

    /**
     * 复制：文件和文件夹-自我
     */
    @Override
    @MyTransactional
    public String copySelf(BaseFileCopySelfDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        boolean exists = ChainWrappers.lambdaQueryChain(baseFileAuthMapper).eq(BaseFileAuthDO::getUserId, currentUserId)
            .eq(!TempConstant.TOP_PID.equals(dto.getPid()), BaseFileAuthDO::getFileId, dto.getPid())
            .eq(BaseFileAuthDO::getWriteFlag, true).exists();

        if (!exists) {
            return TempBizCodeEnum.OK;
        }

        List<BaseFileDO> baseFileDoList = lambdaQuery().in(TempEntity::getId, dto.getIdSet())
            .eq(!MyUserUtil.getCurrentUserAdminFlag(currentUserId), BaseFileDO::getBelongId, currentUserId).list();

        if (CollUtil.isEmpty(baseFileDoList)) {
            return TempBizCodeEnum.OK;
        }

        for (BaseFileDO item : baseFileDoList) {

            item.setPid(dto.getPid());

            String oldNewFileName = item.getNewFileName();

            item.setNewFileName(IdUtil.simpleUUID() + "." + item.getFileExtName());

            String uri = item.getUri();

            item.setOldUri(uri);

            item.setOldBucketName(item.getBucketName());

            String newUri = StrUtil.replaceFirst(uri, oldNewFileName, item.getNewFileName());

            item.setUri(newUri);

            item.setId(null);
            item.setCreateId(null);
            item.setCreateTime(null);
            item.setUpdateId(null);
            item.setUpdateTime(null);

        }

        // 复制文件
        BaseFileUtil.copyBaseFileStorage(baseFileDoList);

        saveBatch(baseFileDoList);

        return TempBizCodeEnum.OK;

    }

}
