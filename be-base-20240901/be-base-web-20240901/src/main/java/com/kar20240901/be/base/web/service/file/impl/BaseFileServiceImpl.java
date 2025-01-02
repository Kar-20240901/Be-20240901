package com.kar20240901.be.base.web.service.file.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.BooleanUtil;
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
import com.kar20240901.be.base.web.model.constant.base.BaseConstant;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoId;
import com.kar20240901.be.base.web.model.domain.file.BaseFileAuthDO;
import com.kar20240901.be.base.web.model.domain.file.BaseFileDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.file.BaseFileCopySelfDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileCreateFolderSelfSelfDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileMoveSelfDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFilePageDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFilePageSelfDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileUpdateSelfDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileUploadChunkComposeDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileUploadChunkDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileUploadChunkPreDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileUploadDTO;
import com.kar20240901.be.base.web.model.enums.file.BaseFileStorageTypeEnum;
import com.kar20240901.be.base.web.model.enums.file.BaseFileTypeEnum;
import com.kar20240901.be.base.web.model.enums.file.BaseFileUploadTypeEnum;
import com.kar20240901.be.base.web.model.vo.base.LongObjectMapVO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.file.BaseFileUploadChunkPreVO;
import com.kar20240901.be.base.web.service.file.BaseFileService;
import com.kar20240901.be.base.web.util.base.CallBack;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import com.kar20240901.be.base.web.util.base.MyStrUtil;
import com.kar20240901.be.base.web.util.base.MyThreadUtil;
import com.kar20240901.be.base.web.util.base.MyTreeUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.base.ResponseUtil;
import com.kar20240901.be.base.web.util.base.SeparatorUtil;
import com.kar20240901.be.base.web.util.file.BaseFileUtil;
import com.kar20240901.be.base.web.util.file.MultipartFileUtil;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
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

        BaseFileUploadBO baseFileUploadBO = getBaseFileUploadBoByDTO(dto);

        // 执行：上传
        return BaseFileUtil.upload(baseFileUploadBO);

    }

    /**
     * 通过 BaseFileUploadDTO，获取：BaseFileUploadBO
     */
    public static BaseFileUploadBO getBaseFileUploadBoByDTO(BaseFileUploadDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        BaseFileUploadBO baseFileUploadBO = new BaseFileUploadBO();

        baseFileUploadBO.setFile(dto.getFile());
        baseFileUploadBO.setUploadType(dto.getUploadType());
        baseFileUploadBO.setRemark(MyStrUtil.maxLength(dto.getRemark(), 300));
        baseFileUploadBO.setExtraJson(dto.getExtraJson());

        baseFileUploadBO.setRefId(dto.getRefId());

        baseFileUploadBO.setUserId(currentUserId);

        baseFileUploadBO.setPid(dto.getPid());

        return baseFileUploadBO;

    }

    /**
     * 上传分片文件-准备工作：公有和私有
     */
    @Override
    public BaseFileUploadChunkPreVO uploadChunkPre(BaseFileUploadChunkPreDTO dto) {

        dto.setFile(MultipartFileUtil.getByFileNameAndFileSize(dto.getFileName(), dto.getFileSize()));

        // 获取：BaseFileUploadBO对象
        BaseFileUploadBO baseFileUploadBO = getBaseFileUploadBoByDTO(dto);

        return BaseFileUtil.uploadChunkPre(dto, baseFileUploadBO);

    }

    /**
     * 上传分片文件：公有和私有
     */
    @Override
    public String uploadChunk(BaseFileUploadChunkDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        return BaseFileUtil.uploadChunk(dto, currentUserId);

    }

    /**
     * 上传分片文件-合并：公有和私有
     */
    @Override
    public String uploadChunkCompose(BaseFileUploadChunkComposeDTO dto) {

        return TempBizCodeEnum.OK;

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
    public Page<BaseFileDO> myPage(BaseFilePageDTO dto, boolean folderSizeFlag, boolean pidPathStrFlag) {

        if (BooleanUtil.isTrue(dto.getGlobalFlag())) {

            dto.setPid(null);

        } else if (dto.getPid() == null) {

            dto.setPid(TempConstant.TOP_PID);

        }

        if (BooleanUtil.isTrue(dto.getBackUpFlag())) {

            if (dto.getPid() != null && !dto.getPid().equals(TempConstant.TOP_PID)) {

                BaseFileDO baseFileDO = lambdaQuery().eq(TempEntity::getId, dto.getPid())
                    .eq(dto.getBelongId() != null, BaseFileDO::getBelongId, dto.getBelongId())
                    .select(BaseFileDO::getPid).one();

                if (baseFileDO == null) {

                    dto.setPid(TempConstant.TOP_PID);

                } else {

                    dto.setPid(baseFileDO.getPid());

                }

            }

        }

        Page<BaseFileDO> page = lambdaQuery() //
            .like(StrUtil.isNotBlank(dto.getShowFileName()), BaseFileDO::getShowFileName, dto.getShowFileName()) //
            .like(StrUtil.isNotBlank(dto.getRemark()), TempEntityNoId::getRemark, dto.getRemark()) //
            .eq(dto.getBelongId() != null, BaseFileDO::getBelongId, dto.getBelongId()) //
            .eq(dto.getUploadType() != null, BaseFileDO::getUploadType, dto.getUploadType()) //
            .eq(dto.getStorageType() != null, BaseFileDO::getStorageType, dto.getStorageType()) //
            .eq(dto.getPublicFlag() != null, BaseFileDO::getPublicFlag, dto.getPublicFlag()) //
            .eq(dto.getEnableFlag() != null, TempEntity::getEnableFlag, dto.getEnableFlag()) //
            .eq(dto.getRefId() != null, BaseFileDO::getRefId, dto.getRefId()) //
            .eq(dto.getPid() != null, BaseFileDO::getPid, dto.getPid()) //
            .eq(dto.getType() != null, BaseFileDO::getType, dto.getType()) //
            .eq(BaseFileDO::getUploadType, BaseFileUploadTypeEnum.FILE_SYSTEM) //
            .select(true, getMyPageSelectList(folderSizeFlag, pidPathStrFlag))
            .page(dto.createTimeDescDefaultOrderPage());

        // 后续处理
        myPageSuf(page);

        return page;

    }

    /**
     * 后续处理
     */
    private void myPageSuf(Page<BaseFileDO> page) {

        List<BaseFileDO> recordList = page.getRecords();

        if (recordList.size() == 0) {
            return;
        }

        BaseFileDO baseFileDO = recordList.get(0);

        String pidPathStr = baseFileDO.getPidPathStr();

        if (StrUtil.isBlank(pidPathStr)) {
            return;
        }

        for (int i = 0; i < recordList.size(); i++) {

            recordList.get(i).setPidPathStr(null); // 只保留第一个元素的 pidPathStr

        }

        List<String> pidStrList = StrUtil.splitTrim(pidPathStr, SeparatorUtil.VERTICAL_LINE_SEPARATOR);

        if (pidStrList.size() == 1 && pidStrList.get(0).equals(TempConstant.TOP_PID.toString())) {

            baseFileDO.setPathList(CollUtil.newArrayList(BaseConstant.TOP_FOLDER_NAME));

            baseFileDO.setPidList(CollUtil.newArrayList(TempConstant.TOP_PID));

            return;

        }

        pidStrList.remove(TempConstant.TOP_PID.toString());

        List<BaseFileDO> baseFileDoList =
            lambdaQuery().select(TempEntity::getId, BaseFileDO::getShowFileName).in(TempEntity::getId, pidStrList)
                .list();

        Map<String, String> idNameMap =
            baseFileDoList.stream().collect(Collectors.toMap(it -> it.getId().toString(), BaseFileDO::getShowFileName));

        List<String> pathList = new ArrayList<>();

        List<Long> pidList = new ArrayList<>();

        pathList.add(BaseConstant.TOP_FOLDER_NAME);

        pidList.add(TempConstant.TOP_PID);

        for (String item : pidStrList) {

            pathList.add(idNameMap.getOrDefault(item, item));

            pidList.add(Long.valueOf(item));

        }

        // 只给第一个元素赋值
        baseFileDO.setPathList(pathList);

        baseFileDO.setPidList(pidList);

    }

    /**
     * 获取：需要查询的字段
     *
     * @param folderSizeFlag 是否增加：文件夹大小的查询字段
     * @param pidPathStrFlag 是否增加：文件路径字符串
     */
    private static ArrayList<SFunction<BaseFileDO, ?>> getMyPageSelectList(boolean folderSizeFlag,
        boolean pidPathStrFlag) {

        ArrayList<SFunction<BaseFileDO, ?>> arrayList =
            CollUtil.newArrayList(TempEntity::getId, TempEntityNoId::getCreateTime, BaseFileDO::getBelongId,
                BaseFileDO::getFileSize, BaseFileDO::getShowFileName, BaseFileDO::getPid, BaseFileDO::getType);

        if (folderSizeFlag) {

            arrayList.add(BaseFileDO::getFolderSize);

        }

        if (pidPathStrFlag) {

            arrayList.add(BaseFileDO::getPidPathStr);

        }

        return arrayList;

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
        return myPage(baseFilePageDTO, true, true);

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

            Page<BaseFileDO> page = lambdaQuery().select(true, getMyPageSelectList(false, false))
                .eq(BaseFileDO::getUploadType, BaseFileUploadTypeEnum.FILE_SYSTEM)
                .page(dto.createTimeDescDefaultOrderPage());

            allListCallBack.setValue(page.getRecords());

        }, countDownLatch);

        // 根据条件进行筛选，得到符合条件的数据，然后再逆向生成整棵树，并返回这个树结构
        List<BaseFileDO> baseFileDoList = myPage(dto, false, false).getRecords();

        countDownLatch.await();

        if (baseFileDoList.size() == 0) {

            return CollUtil.newArrayList(getTopBaseFileDo());

        }

        if (allListCallBack.getValue().size() == 0) {

            return CollUtil.newArrayList(getTopBaseFileDo());

        }

        List<BaseFileDO> result = MyTreeUtil.getFullTreeByDeepNode(baseFileDoList, allListCallBack.getValue());

        if (CollUtil.isEmpty(result)) {

            return CollUtil.newArrayList(getTopBaseFileDo());

        }

        if (result.get(0).getPid().equals(TempConstant.TOP_PID)) {

            BaseFileDO topBaseFileDo = getTopBaseFileDo();

            topBaseFileDo.setChildren(result);

            return CollUtil.newArrayList(topBaseFileDo);

        }

        return result;

    }

    /**
     * 获取：根文件夹
     */
    public static BaseFileDO getTopBaseFileDo() {

        BaseFileDO baseFileDo = new BaseFileDO();

        baseFileDo.setShowFileName(BaseConstant.TOP_FOLDER_NAME);

        baseFileDo.setType(BaseFileTypeEnum.FOLDER);

        baseFileDo.setId(TempConstant.TOP_PID);

        return baseFileDo;

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
     * 创建：文件夹-自我
     */
    @Override
    public String createFolderSelf(BaseFileCreateFolderSelfSelfDTO dto) {

        Long userId = MyUserUtil.getCurrentUserId();

        BaseFileDO baseFileDO = new BaseFileDO();

        baseFileDO.setBelongId(userId);

        baseFileDO.setBucketName("");

        baseFileDO.setUri("");

        baseFileDO.setOriginFileName(dto.getFolderName());

        baseFileDO.setNewFileName(IdUtil.simpleUUID());

        baseFileDO.setFileExtName("");

        baseFileDO.setExtraJson("");

        baseFileDO.setUploadType(BaseFileUploadTypeEnum.FILE_SYSTEM.getCode());

        baseFileDO.setStorageConfigurationId(TempConstant.NEGATIVE_ONE);

        baseFileDO.setStorageType(BaseFileStorageTypeEnum.EMPTY.getCode());

        Long pid = MyEntityUtil.getNotNullPid(dto.getPid());

        baseFileDO.setPid(pid);

        baseFileDO.setType(BaseFileTypeEnum.FOLDER);

        baseFileDO.setShowFileName(dto.getFolderName());

        baseFileDO.setPublicFlag(false);

        baseFileDO.setFileSize(TempConstant.ZERO);

        baseFileDO.setRefId(TempConstant.NEGATIVE_ONE);

        baseFileDO.setEnableFlag(true);

        baseFileDO.setRemark("");

        String pidPathStr = BaseFileUtil.getPidPathStr(pid);

        baseFileDO.setPidPathStr(pidPathStr);

        save(baseFileDO);

        return TempBizCodeEnum.OK;

    }

    /**
     * 修改：文件和文件夹-自我
     */
    @Override
    public String updateSelf(BaseFileUpdateSelfDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        lambdaUpdate().in(TempEntity::getId, dto.getIdSet())
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

        if (!MyUserUtil.getCurrentUserAdminFlag(currentUserId)) {

            boolean exists =
                ChainWrappers.lambdaQueryChain(baseFileAuthMapper).eq(BaseFileAuthDO::getUserId, currentUserId)
                    .eq(!TempConstant.TOP_PID.equals(dto.getPid()), BaseFileAuthDO::getFileId, dto.getPid())
                    .eq(BaseFileAuthDO::getWriteFlag, true).exists();

            if (!exists) {
                return TempBizCodeEnum.OK;
            }

        }

        String pidPathStr = BaseFileUtil.getPidPathStr(dto.getPid());

        lambdaUpdate().in(TempEntity::getId, dto.getIdSet())
            .eq(!MyUserUtil.getCurrentUserAdminFlag(currentUserId), BaseFileDO::getBelongId, currentUserId)
            .set(BaseFileDO::getPid, dto.getPid()).set(BaseFileDO::getPidPathStr, pidPathStr).update();

        return TempBizCodeEnum.OK;

    }

    /**
     * 复制：文件和文件夹-自我
     */
    @Override
    @MyTransactional
    public String copySelf(BaseFileCopySelfDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        if (!MyUserUtil.getCurrentUserAdminFlag(currentUserId)) {

            boolean exists =
                ChainWrappers.lambdaQueryChain(baseFileAuthMapper).eq(BaseFileAuthDO::getUserId, currentUserId)
                    .eq(!TempConstant.TOP_PID.equals(dto.getPid()), BaseFileAuthDO::getFileId, dto.getPid())
                    .eq(BaseFileAuthDO::getWriteFlag, true).exists();

            if (!exists) {
                return TempBizCodeEnum.OK;
            }

        }

        List<BaseFileDO> baseFileDoList = lambdaQuery().in(TempEntity::getId, dto.getIdSet())
            .eq(!MyUserUtil.getCurrentUserAdminFlag(currentUserId), BaseFileDO::getBelongId, currentUserId).list();

        if (CollUtil.isEmpty(baseFileDoList)) {
            return TempBizCodeEnum.OK;
        }

        String pidPathStr = BaseFileUtil.getPidPathStr(dto.getPid());

        for (BaseFileDO item : baseFileDoList) {

            item.setPid(dto.getPid());

            item.setPidPathStr(pidPathStr);

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
