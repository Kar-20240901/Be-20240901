package com.kar20240901.be.base.web.service.file.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.file.BaseFileMapper;
import com.kar20240901.be.base.web.model.bo.file.BaseFilePrivateDownloadBO;
import com.kar20240901.be.base.web.model.bo.file.BaseFileUploadBO;
import com.kar20240901.be.base.web.model.constant.base.BaseConstant;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoId;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoIdSuper;
import com.kar20240901.be.base.web.model.domain.file.BaseFileDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.file.BaseFileCopySelfDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileCreateFolderSelfSelfDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileMoveSelfDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFilePageDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFilePageSelfDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileScrollSelfDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileUpdateSelfDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileUploadDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileUploadFileSystemChunkComposeDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileUploadFileSystemChunkDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileUploadFileSystemChunkPreDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileUploadFileSystemDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileUploadFileSystemPreDTO;
import com.kar20240901.be.base.web.model.enums.file.BaseFileStorageTypeEnum;
import com.kar20240901.be.base.web.model.enums.file.BaseFileTypeEnum;
import com.kar20240901.be.base.web.model.enums.file.BaseFileUploadTypeEnum;
import com.kar20240901.be.base.web.model.vo.base.LongObjectMapVO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.file.BaseFilePageSelfVO;
import com.kar20240901.be.base.web.model.vo.file.BaseFilePrivateDownloadVO;
import com.kar20240901.be.base.web.model.vo.file.BaseFileUploadFileSystemChunkPreVO;
import com.kar20240901.be.base.web.model.vo.file.BaseFileUploadFileSystemPreVO;
import com.kar20240901.be.base.web.service.file.BaseFileService;
import com.kar20240901.be.base.web.util.base.CallBack;
import com.kar20240901.be.base.web.util.base.IdGeneratorUtil;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import com.kar20240901.be.base.web.util.base.MyPageUtil;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BaseFileServiceImpl extends ServiceImpl<BaseFileMapper, BaseFileDO> implements BaseFileService {

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
     * 文件系统上传文件-准备工作：公有和私有
     */
    @Override
    public BaseFileUploadFileSystemPreVO uploadFileSystemPre(BaseFileUploadFileSystemPreDTO dto) {

        // 检查权限
        BaseFileUtil.checkAuth(CollUtil.newArrayList(dto.getPid()), null, 2);

        dto.setFile(MultipartFileUtil.getByFileNameAndFileSize(dto.getFileName(), dto.getFileSize()));

        // 获取：BaseFileUploadBO对象
        BaseFileUploadBO baseFileUploadBO = getBaseFileUploadBoByDTO(dto);

        return BaseFileUtil.uploadFileSystemPre(dto, baseFileUploadBO);

    }

    /**
     * 文件系统上传文件：公有和私有
     */
    @Override
    public Long uploadFileSystem(BaseFileUploadFileSystemDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        return BaseFileUtil.uploadFileSystem(dto, currentUserId);

    }

    /**
     * 文件系统上传分片文件-准备工作：公有和私有
     */
    @Override
    public BaseFileUploadFileSystemChunkPreVO uploadFileSystemChunkPre(BaseFileUploadFileSystemChunkPreDTO dto) {

        // 检查权限
        BaseFileUtil.checkAuth(CollUtil.newArrayList(dto.getPid()), null, 2);

        dto.setFile(MultipartFileUtil.getByFileNameAndFileSize(dto.getFileName(), dto.getFileSize()));

        // 获取：BaseFileUploadBO对象
        BaseFileUploadBO baseFileUploadBO = getBaseFileUploadBoByDTO(dto);

        return BaseFileUtil.uploadFileSystemChunkPre(dto, baseFileUploadBO);

    }

    /**
     * 文件系统上传分片文件：公有和私有
     */
    @Override
    public String uploadFileSystemChunk(BaseFileUploadFileSystemChunkDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        return BaseFileUtil.uploadFileSystemChunk(dto, currentUserId);

    }

    /**
     * 文件系统上传分片文件-合并：公有和私有
     */
    @Override
    public String uploadFileSystemChunkCompose(BaseFileUploadFileSystemChunkComposeDTO dto) {

        return BaseFileUtil.uploadFileSystemChunkCompose(dto.getTransferId());

    }

    /**
     * 下载文件：私有
     */
    @SneakyThrows
    @Override
    public void privateDownload(NotNullId notNullId, HttpServletResponse response, HttpServletRequest request) {

        BaseFilePrivateDownloadBO baseFilePrivateDownloadBO = new BaseFilePrivateDownloadBO();

        baseFilePrivateDownloadBO.setFileId(notNullId.getId());

        // 处理 baseFilePrivateDownloadBO对象
        handleBaseFilePrivateDownloadBO(request, baseFilePrivateDownloadBO);

        // 执行：下载
        BaseFilePrivateDownloadVO baseFilePrivateDownloadVO = BaseFileUtil.privateDownload(baseFilePrivateDownloadBO);

        InputStream inputStream = baseFilePrivateDownloadVO.getInputStream();

        if (inputStream == null) {
            R.errorMsg("操作失败：文件流获取失败");
        }

        // 支持范围下载
        ResponseUtil.acceptRangeDownload(response);

        if (StrUtil.isNotBlank(baseFilePrivateDownloadVO.getContentRangeHeader())) {

            // 设置：响应码
            ResponseUtil.rangeDownload(response);

            response.setHeader("Content-Range", baseFilePrivateDownloadVO.getContentRangeHeader());

        }

        // 设置：文件名
        ResponseUtil.getOutputStream(response, baseFilePrivateDownloadVO.getFileName());

        // 推送
        ResponseUtil.flush(response, inputStream);

    }

    /**
     * 处理 baseFilePrivateDownloadBO对象
     */
    private static void handleBaseFilePrivateDownloadBO(HttpServletRequest request,
        BaseFilePrivateDownloadBO baseFilePrivateDownloadBO) {

        String rangeStr = request.getHeader("Range");

        rangeStr = StrUtil.trim(rangeStr);

        if (StrUtil.isBlank(rangeStr)) {
            return;
        }

        String str = rangeStr.replace("bytes=", "");

        List<String> groupList = StrUtil.split(str, ",");

        // 暂时只支持下载一段
        String range = groupList.get(0);

        List<String> splitList = StrUtil.split(range, "-");

        if (splitList.size() != 2) {
            return;
        }

        String pre = splitList.get(0);

        if (NumberUtil.isNumber(pre)) {

            baseFilePrivateDownloadBO.setPre(Long.parseLong(pre));

        }

        String suf = splitList.get(1);

        if (NumberUtil.isNumber(suf)) {

            baseFilePrivateDownloadBO.setSuf(Long.parseLong(suf));

        }

    }

    /**
     * 批量删除文件：公有和私有
     */
    @Override
    @DSTransactional
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
    public BaseFilePageSelfVO myPage(BaseFilePageDTO dto, boolean folderSizeFlag, boolean pidPathStrFlag,
        boolean treeFlag) {

        if (BooleanUtil.isTrue(dto.getGlobalFlag())) {

            dto.setPid(null);

        } else if (dto.getPid() == null) {

            dto.setPid(TempConstant.TOP_PID);

        }

        BaseFilePageSelfVO baseFilePageSelfVO = new BaseFilePageSelfVO();

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

                baseFilePageSelfVO.setBackUpPid(dto.getPid());

            } else {

                baseFilePageSelfVO.setBackUpPid(TempConstant.TOP_PID);

            }

        }

        Page<BaseFileDO> page = lambdaQuery() //
            .like(StrUtil.isNotBlank(dto.getShowFileName()), BaseFileDO::getShowFileName, dto.getShowFileName()) //
            .like(StrUtil.isNotBlank(dto.getRemark()), TempEntityNoId::getRemark, dto.getRemark()) //
            .eq(dto.getBelongId() != null, BaseFileDO::getBelongId, dto.getBelongId()) //
            .eq(dto.getStorageType() != null, BaseFileDO::getStorageType, dto.getStorageType()) //
            .eq(dto.getPublicFlag() != null, BaseFileDO::getPublicFlag, dto.getPublicFlag()) //
            .eq(dto.getEnableFlag() != null, TempEntity::getEnableFlag, dto.getEnableFlag()) //
            .eq(dto.getRefId() != null, BaseFileDO::getRefId, dto.getRefId()) //
            .eq(dto.getPid() != null, BaseFileDO::getPid, dto.getPid()) //
            .eq(dto.getType() != null, BaseFileDO::getType, dto.getType()) //
            .eq(BaseFileDO::getUploadType, BaseFileUploadTypeEnum.FILE_SYSTEM) //
            .lt(BooleanUtil.isFalse(dto.getBackwardFlag()), BaseFileDO::getId, dto.getScrollId()) //
            .gt(BooleanUtil.isTrue(dto.getBackwardFlag()), BaseFileDO::getId, dto.getScrollId()) //
            .select(true, getMyPageSelectList(folderSizeFlag, treeFlag, true))
            .page(dto.createTimeDescDefaultOrderPage());

        // 后续处理
        myPageSuf(dto.getPid(), pidPathStrFlag, baseFilePageSelfVO);

        baseFilePageSelfVO.setRecords(page.getRecords());

        return baseFilePageSelfVO;

    }

    /**
     * 后续处理
     */
    private void myPageSuf(Long pid, boolean pidPathStrFlag, BaseFilePageSelfVO baseFilePageSelfVO) {

        if (!pidPathStrFlag) {
            return;
        }

        if (pid == null || TempConstant.TOP_PID.equals(pid)) {

            baseFilePageSelfVO.setPathList(CollUtil.newArrayList(BaseConstant.TOP_FOLDER_NAME));

            baseFilePageSelfVO.setPidList(CollUtil.newArrayList(TempConstant.TOP_PID));

            return;

        }

        BaseFileDO baseFileDO = lambdaQuery().eq(BaseFileDO::getId, pid)
            .select(BaseFileDO::getPidPathStr, BaseFileDO::getId, BaseFileDO::getShowFileName).one();

        if (baseFileDO == null) {

            baseFilePageSelfVO.setPathList(CollUtil.newArrayList(BaseConstant.TOP_FOLDER_NAME));

            baseFilePageSelfVO.setPidList(CollUtil.newArrayList(TempConstant.TOP_PID));

            baseFilePageSelfVO.setRecords(new ArrayList<>());

            return;

        }

        List<String> pidStrList = StrUtil.splitTrim(baseFileDO.getPidPathStr(), SeparatorUtil.VERTICAL_LINE_SEPARATOR);

        if (pidStrList.size() == 1 && pidStrList.get(0).equals(TempConstant.TOP_PID.toString())) {

            baseFilePageSelfVO.setPathList(
                CollUtil.newArrayList(BaseConstant.TOP_FOLDER_NAME, baseFileDO.getShowFileName()));

            baseFilePageSelfVO.setPidList(CollUtil.newArrayList(TempConstant.TOP_PID, baseFileDO.getId()));

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

        pathList.add(baseFileDO.getShowFileName());

        pidList.add(baseFileDO.getId());

        baseFilePageSelfVO.setPathList(pathList);

        baseFilePageSelfVO.setPidList(pidList);

    }

    /**
     * 获取：需要查询的字段
     *
     * @param folderSizeFlag 是否增加：文件夹大小的查询字段
     */
    private static ArrayList<SFunction<BaseFileDO, ?>> getMyPageSelectList(boolean folderSizeFlag, boolean treeFlag,
        boolean uploadFlag) {

        if (treeFlag) {

            return CollUtil.newArrayList(TempEntity::getId, BaseFileDO::getPid, BaseFileDO::getShowFileName);

        }

        ArrayList<SFunction<BaseFileDO, ?>> arrayList =
            CollUtil.newArrayList(TempEntity::getId, TempEntityNoId::getCreateTime, BaseFileDO::getFileSize,
                BaseFileDO::getShowFileName, BaseFileDO::getPid, BaseFileDO::getType);

        if (folderSizeFlag) {

            arrayList.add(BaseFileDO::getFolderSize);

        }

        if (uploadFlag) {

            arrayList.add(BaseFileDO::getUploadFlag);

        }

        return arrayList;

    }

    /**
     * 分页排序查询-自我
     */
    @Override
    public BaseFilePageSelfVO myPageSelf(BaseFilePageSelfDTO dto) {

        BaseFilePageDTO baseFilePageDTO = BeanUtil.copyProperties(dto, BaseFilePageDTO.class);

        Long currentUserId = MyUserUtil.getCurrentUserId();

        baseFilePageDTO.setBelongId(currentUserId); // 设置为：当前用户

        // 执行
        return myPage(baseFilePageDTO, true, true, false);

    }

    /**
     * 滚动加载-自我
     */
    @Override
    public BaseFilePageSelfVO scrollSelf(BaseFileScrollSelfDTO dto) {

        BaseFilePageDTO baseFilePageDTO = BeanUtil.copyProperties(dto, BaseFilePageDTO.class);

        Long currentUserId = MyUserUtil.getCurrentUserId();

        baseFilePageDTO.setBelongId(currentUserId); // 设置为：当前用户

        boolean backwardFlag = BooleanUtil.isTrue(dto.getBackwardFlag());

        // 获取：滚动加载时的 id
        Long scrollId = MyPageUtil.getScrollId(dto);

        baseFilePageDTO.setBackwardFlag(backwardFlag);

        baseFilePageDTO.setScrollId(scrollId);

        baseFilePageDTO.setCurrent(1);

        return myPage(baseFilePageDTO, true, true, false);

    }

    /**
     * 查询：树结构
     */
    @SneakyThrows
    @Override
    public List<BaseFileDO> tree(BaseFilePageDTO dto) {

        dto.setPageSizeAll(); // 不分页

        dto.setGlobalFlag(true); // 防止只查询顶层

        CountDownLatch countDownLatch = ThreadUtil.newCountDownLatch(1);

        CallBack<List<BaseFileDO>> allListCallBack = new CallBack<>();

        MyThreadUtil.execute(() -> {

            Page<BaseFileDO> page = lambdaQuery().select(true, getMyPageSelectList(false, true, false))
                .eq(BaseFileDO::getUploadType, BaseFileUploadTypeEnum.FILE_SYSTEM)
                .page(dto.createTimeDescDefaultOrderPage());

            allListCallBack.setValue(page.getRecords());

        }, countDownLatch);

        // 根据条件进行筛选，得到符合条件的数据，然后再逆向生成整棵树，并返回这个树结构
        List<BaseFileDO> baseFileDoList = myPage(dto, false, false, true).getRecords();

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

        Long pid = MyEntityUtil.getNotNullPid(dto.getPid());

        // 检查权限
        BaseFileUtil.checkAuth(CollUtil.newArrayList(pid), userId, 2);

        BaseFileDO baseFileDO = new BaseFileDO();

        baseFileDO.setId(IdGeneratorUtil.nextId());

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

        baseFileDO.setPid(pid);

        baseFileDO.setType(BaseFileTypeEnum.FOLDER);

        baseFileDO.setShowFileName(dto.getFolderName());

        baseFileDO.setPublicFlag(false);

        baseFileDO.setFileSize(TempConstant.ZERO);

        baseFileDO.setRefId(TempConstant.NEGATIVE_ONE);

        baseFileDO.setUploadFlag(false);

        baseFileDO.setEnableFlag(true);

        baseFileDO.setRemark("");

        String pidPathStr = BaseFileUtil.getPidPathStr(pid);

        baseFileDO.setPidPathStr(pidPathStr);

        baseFileDO.setCreateId(userId);

        baseFileDO.setUpdateId(userId);

        save(baseFileDO);

        // 设置权限
        BaseFileUtil.saveFileAuth(userId, baseFileDO);

        return TempBizCodeEnum.OK;

    }

    /**
     * 修改：文件和文件夹-自我
     */
    @Override
    public String updateSelf(BaseFileUpdateSelfDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        // 检查权限
        BaseFileUtil.checkAuth(dto.getIdSet(), currentUserId, 2);

        lambdaUpdate().in(TempEntity::getId, dto.getIdSet()).set(BaseFileDO::getShowFileName, dto.getFileName())
            .update();

        return TempBizCodeEnum.OK;

    }

    /**
     * 移动：文件和文件夹-自我
     */
    @Override
    @DSTransactional
    public String moveSelf(BaseFileMoveSelfDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        HashSet<Long> idSet = new HashSet<>(dto.getIdSet());

        idSet.add(dto.getPid());

        // 检查权限
        BaseFileUtil.checkAuth(idSet, currentUserId, 2);

        String pidPathStr = BaseFileUtil.getPidPathStr(dto.getPid());

        List<Long> pidList =
            StrUtil.splitTrim(pidPathStr, SeparatorUtil.VERTICAL_LINE_SEPARATOR).stream().map(NumberUtil::parseLong)
                .collect(Collectors.toList());

        if (dto.getIdSet().contains(dto.getPid()) || CollUtil.containsAny(pidList, dto.getIdSet())) {

            R.error(TempBizCodeEnum.ILLEGAL_REQUEST);

        }

        List<BaseFileDO> baseFileDOList =
            lambdaQuery().in(BaseFileDO::getId, dto.getIdSet()).select(BaseFileDO::getId, BaseFileDO::getPidPathStr)
                .list();

        lambdaUpdate().in(TempEntity::getId, dto.getIdSet()).set(BaseFileDO::getPid, dto.getPid())
            .set(BaseFileDO::getPidPathStr, pidPathStr).update();

        for (BaseFileDO item : baseFileDOList) {

            String pidStr = item.getPidPathStr() + SeparatorUtil.verticalLine(item.getId());

            lambdaUpdate().setSql("pid_path_str = CONCAT('" + pidPathStr + SeparatorUtil.verticalLine(item.getId())
                    + "', SUBSTRING(pid_path_str, " + (pidStr.length() + 1) + "))")
                .likeRight(BaseFileDO::getPidPathStr, pidStr).update();

        }

        return TempBizCodeEnum.OK;

    }

    /**
     * 复制：文件和文件夹-自我
     */
    @Override
    @DSTransactional
    public String copySelf(BaseFileCopySelfDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        // 检查权限
        BaseFileUtil.checkAuth(CollUtil.newArrayList(dto.getPid()), currentUserId, 2);

        // 检查权限
        BaseFileUtil.checkAuth(dto.getIdSet(), currentUserId, 1);

        List<BaseFileDO> baseFileDoList = lambdaQuery().in(TempEntity::getId, dto.getIdSet()).list();

        if (CollUtil.isEmpty(baseFileDoList)) {
            return TempBizCodeEnum.OK;
        }

        Set<Long> fileIdSet = baseFileDoList.stream().map(BaseFileDO::getId).collect(Collectors.toSet());

        List<Long> folderIdList =
            baseFileDoList.stream().filter(it -> BaseFileTypeEnum.FOLDER.equals(it.getType())).map(BaseFileDO::getId)
                .collect(Collectors.toList());

        // 深度查询的文件 id集合
        Set<Long> deepFindFileIdSet = new HashSet<>();

        // 深度查找-复制
        copyDeepFind(folderIdList, fileIdSet, baseFileDoList, deepFindFileIdSet);

        String pidPathStr = BaseFileUtil.getPidPathStr(dto.getPid());

        List<Long> fileIdList = new ArrayList<>();

        // id映射
        Map<Long, Long> idMap = MapUtil.newHashMap(baseFileDoList.size());

        for (BaseFileDO item : baseFileDoList) {

            Long oldId = item.getId();

            Long newId = IdGeneratorUtil.nextId();

            idMap.put(oldId, newId);

        }

        Set<Long> folderIdSet = new HashSet<>(folderIdList);

        for (BaseFileDO item : baseFileDoList) {

            // 处理：pid和 pidPathStr
            handlePidAndPidPathStr(dto, item, deepFindFileIdSet, idMap, pidPathStr, folderIdSet);

            String oldNewFileName = item.getNewFileName();

            if (StrUtil.isBlank(item.getFileExtName())) {

                item.setNewFileName(IdUtil.simpleUUID());

            } else {

                item.setNewFileName(IdUtil.simpleUUID() + "." + item.getFileExtName());

            }

            String uri = item.getUri();

            item.setOldUri(uri);

            item.setOldBucketName(item.getBucketName());

            String newUri = StrUtil.replaceFirst(uri, oldNewFileName, item.getNewFileName());

            item.setUri(newUri);

            Long newId = idMap.get(item.getId());

            item.setId(newId);
            item.setCreateId(null);
            item.setCreateTime(null);
            item.setUpdateId(null);
            item.setUpdateTime(null);

            fileIdList.add(newId);

        }

        // 复制文件
        BaseFileUtil.copyBaseFileStorage(baseFileDoList);

        saveBatch(baseFileDoList);

        // 设置权限
        BaseFileUtil.saveBatchFileAuth(currentUserId, fileIdList);

        return TempBizCodeEnum.OK;

    }

    /**
     * 处理：pid和 pidPathStr
     */
    private static void handlePidAndPidPathStr(BaseFileCopySelfDTO dto, BaseFileDO item, Set<Long> deepFindFileIdSet,
        Map<Long, Long> idMap, String pidPathStr, Set<Long> folderIdList) {

        if (deepFindFileIdSet.contains(item.getId())) {

            Long oldPid = item.getPid();

            Long newPid = idMap.get(oldPid);

            item.setPid(newPid);

            List<String> splitList = StrUtil.splitTrim(item.getPidPathStr(), SeparatorUtil.VERTICAL_LINE_SEPARATOR);

            StrBuilder strBuilder = StrUtil.strBuilder();

            boolean findFlag = false;

            for (String subItem : splitList) {

                long pathOldPid = NumberUtil.parseLong(subItem);

                if (folderIdList.contains(pathOldPid)) {

                    strBuilder.append(pidPathStr);

                    findFlag = true;

                }

                if (findFlag) {

                    Long pathNewPid = idMap.getOrDefault(pathOldPid, pathOldPid);

                    strBuilder.append(SeparatorUtil.verticalLine(pathNewPid));

                }

            }

            item.setPidPathStr(strBuilder.toString());

        } else {

            item.setPid(dto.getPid());

            item.setPidPathStr(pidPathStr);

        }

    }

    /**
     * 深度查找-复制
     */
    private void copyDeepFind(List<Long> folderIdList, Set<Long> fileIdSet, List<BaseFileDO> baseFileDoList,
        Set<Long> deepFindFileIdSet) {

        if (CollUtil.isEmpty(folderIdList)) {
            return;
        }

        for (Long item : folderIdList) {

            List<BaseFileDO> tempBaseFileDoList =
                lambdaQuery().like(BaseFileDO::getPidPathStr, SeparatorUtil.verticalLine(item))
                    .orderByDesc(TempEntityNoIdSuper::getCreateTime).list();

            if (CollUtil.isEmpty(tempBaseFileDoList)) {
                continue;
            }

            for (BaseFileDO subItem : tempBaseFileDoList) {

                if (!fileIdSet.contains(subItem.getId())) {

                    fileIdSet.add(subItem.getId());

                    deepFindFileIdSet.add(subItem.getId());

                    baseFileDoList.add(subItem);

                }

            }

        }

    }

}
