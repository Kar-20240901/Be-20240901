package com.kar20240901.be.base.web.util.file;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.configuration.log.LogFilter;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.base.BaseUserInfoMapper;
import com.kar20240901.be.base.web.mapper.file.BaseFileStorageConfigurationMapper;
import com.kar20240901.be.base.web.model.bo.file.BaseFileUploadBO;
import com.kar20240901.be.base.web.model.configuration.file.IBaseFileRemove;
import com.kar20240901.be.base.web.model.configuration.file.IBaseFileStorage;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.constant.base.TempFileTempPathConstant;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoId;
import com.kar20240901.be.base.web.model.domain.base.TempUserInfoDO;
import com.kar20240901.be.base.web.model.domain.file.BaseFileAuthDO;
import com.kar20240901.be.base.web.model.domain.file.BaseFileDO;
import com.kar20240901.be.base.web.model.domain.file.BaseFileStorageConfigurationDO;
import com.kar20240901.be.base.web.model.domain.file.BaseFileTransferDO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileUploadChunkPreDTO;
import com.kar20240901.be.base.web.model.enums.file.BaseFileTransferStatusEnum;
import com.kar20240901.be.base.web.model.enums.file.BaseFileTransferTypeEnum;
import com.kar20240901.be.base.web.model.enums.file.BaseFileTypeEnum;
import com.kar20240901.be.base.web.model.enums.file.BaseFileUploadTypeEnum;
import com.kar20240901.be.base.web.model.interfaces.file.IBaseFileStorageType;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.file.BaseFileUploadChunkPreVO;
import com.kar20240901.be.base.web.service.file.BaseFileAuthService;
import com.kar20240901.be.base.web.service.file.BaseFileService;
import com.kar20240901.be.base.web.service.file.BaseFileTransferChunkService;
import com.kar20240901.be.base.web.service.file.BaseFileTransferService;
import com.kar20240901.be.base.web.util.base.CallBack;
import com.kar20240901.be.base.web.util.base.IdGeneratorUtil;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import com.kar20240901.be.base.web.util.base.MyStrUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.base.SeparatorUtil;
import com.kar20240901.be.base.web.util.base.TransactionUtil;
import com.kar20240901.be.base.web.util.base.VoidFunc3;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件工具类
 */
@Component
public class BaseFileUtil {

    private static BaseFileService baseFileService;
    private static BaseFileAuthService baseFileAuthService;

    private static BaseUserInfoMapper baseUserInfoMapper;

    private static final Map<Integer, IBaseFileStorage> BASE_FILE_STORAGE_MAP = MapUtil.newHashMap();

    private static List<IBaseFileRemove> iBaseFileRemoveList;

    private static BaseFileStorageConfigurationMapper baseFileStorageConfigurationMapper;

    public BaseFileUtil(BaseFileService baseFileService, BaseFileAuthService baseFileAuthService,
        BaseUserInfoMapper baseUserInfoMapper,
        @Autowired(required = false) @Nullable List<IBaseFileStorage> iBaseFileStorageList,
        @Autowired(required = false) @Nullable List<IBaseFileRemove> iBaseFileRemoveList,
        BaseFileStorageConfigurationMapper baseFileStorageConfigurationMapper) {

        BaseFileUtil.baseFileService = baseFileService;
        BaseFileUtil.baseFileAuthService = baseFileAuthService;

        BaseFileUtil.baseUserInfoMapper = baseUserInfoMapper;

        if (CollUtil.isNotEmpty(iBaseFileStorageList)) {

            for (IBaseFileStorage item : iBaseFileStorageList) {

                BASE_FILE_STORAGE_MAP.put(item.getBaseFileStorageType().getCode(), item);

            }

        }

        BaseFileUtil.iBaseFileRemoveList = iBaseFileRemoveList;
        BaseFileUtil.baseFileStorageConfigurationMapper = baseFileStorageConfigurationMapper;

    }

    private static BaseFileTransferService baseFileTransferService;

    @Resource
    public void setBaseFileTransferService(BaseFileTransferService baseFileTransferService) {
        BaseFileUtil.baseFileTransferService = baseFileTransferService;
    }

    private static BaseFileTransferChunkService baseFileTransferChunkService;

    @Resource
    public void setBaseFileTransferChunkService(BaseFileTransferChunkService baseFileTransferChunkService) {
        BaseFileUtil.baseFileTransferChunkService = baseFileTransferChunkService;
    }

    /**
     * 上传文件：公有和私有 备注：objectName 相同的，会被覆盖掉
     *
     * @return 保存到数据库的，文件主键 id
     */
    @SneakyThrows
    @Nullable
    public static Long upload(BaseFileUploadBO bo) {

        // 上传文件时的检查
        String fileType = BaseFileUploadTypeEnum.uploadCheckWillError(bo.getFile(), bo.getUploadType());

        Long resultBaseFileId = null;

        // 如果是：头像
        if (BaseFileUploadTypeEnum.AVATAR.equals(bo.getUploadType())) {

            // 通用：上传处理
            resultBaseFileId = uploadCommonHandle(bo, fileType, null,

                (baseFileDO) -> {

                    ChainWrappers.lambdaUpdateChain(baseUserInfoMapper).eq(TempUserInfoDO::getId, bo.getUserId())
                        .set(TempUserInfoDO::getAvatarFileId, baseFileDO.getId()).update();

                }, null, bo.getUserId().toString(), true);

        } else if (BaseFileUploadTypeEnum.FILE_SYSTEM.equals(bo.getUploadType())) {

            // 如果是：文件系统
            // 通用：上传处理
            resultBaseFileId = uploadCommonHandle(bo, fileType, null, (baseFileDO) -> {

                BaseFileAuthDO baseFileAuthDO = new BaseFileAuthDO();

                baseFileAuthDO.setFileId(baseFileDO.getId());
                baseFileAuthDO.setUserId(bo.getUserId());
                baseFileAuthDO.setReadFlag(true);
                baseFileAuthDO.setWriteFlag(true);
                baseFileAuthDO.setEnableFlag(true);

                baseFileAuthDO.setRemark("");

                baseFileAuthService.save(baseFileAuthDO);

            }, null, bo.getUserId().toString(), true);

        }

        return resultBaseFileId;

    }

    /**
     * 通用：上传处理
     *
     * @return 文件主键 id
     */
    @NotNull
    public static Long uploadCommonHandle(BaseFileUploadBO bo, String fileType,
        @Nullable IBaseFileStorageType iBaseFileStorageType, @Nullable Consumer<BaseFileDO> consumer,
        @Nullable BaseFileStorageConfigurationDO baseFileStorageConfigurationDO, @Nullable String otherFolderName,
        boolean storageUploadFlag) {

        // 获取：存储方式的配置
        baseFileStorageConfigurationDO =
            getBaseFileStorageConfigurationDO(iBaseFileStorageType, baseFileStorageConfigurationDO);

        Integer storageType = baseFileStorageConfigurationDO.getType();

        IBaseFileStorage iBaseFileStorage = BASE_FILE_STORAGE_MAP.get(storageType);

        if (iBaseFileStorage == null) {

            R.error("操作失败：文件存储方式未找到", storageType);

        }

        String folderName = bo.getUploadType().getFolderName();

        String originalFilename = bo.getFile().getOriginalFilename(); // 旧的文件名

        String newFileName = IdUtil.simpleUUID() + "." + fileType; // 新的文件名

        if (StrUtil.isNotBlank(otherFolderName)) {

            folderName = folderName + "/" + otherFolderName;

        }

        String objectName = folderName + "/" + newFileName;

        String bucketName;

        if (bo.getUploadType().isPublicFlag()) {

            bucketName = baseFileStorageConfigurationDO.getBucketPublicName();

        } else {

            bucketName = baseFileStorageConfigurationDO.getBucketPrivateName();

        }

        if (StrUtil.isBlank(bucketName)) {

            R.errorMsg("操作失败：bucketName为空，请联系管理员");

        }

        if (storageUploadFlag) {

            // 执行：文件上传
            iBaseFileStorage.upload(bucketName, objectName, bo.getFile(), baseFileStorageConfigurationDO);

        }

        String finalBucketName = bucketName;

        BaseFileStorageConfigurationDO finalBaseFileStorageConfigurationDO = baseFileStorageConfigurationDO;

        return TransactionUtil.exec(() -> {

            // 通用保存：文件信息到数据库
            BaseFileDO baseFileDO =
                saveCommonBaseFile(bo, fileType, originalFilename, newFileName, objectName, finalBucketName,
                    finalBaseFileStorageConfigurationDO);

            if (consumer != null) {

                consumer.accept(baseFileDO);

            }

            return baseFileDO.getId();

        });

    }

    /**
     * 获取：存储方式的配置
     */
    @NotNull
    private static BaseFileStorageConfigurationDO getBaseFileStorageConfigurationDO(
        @Nullable IBaseFileStorageType iBaseFileStorageType,
        @Nullable BaseFileStorageConfigurationDO baseFileStorageConfigurationDO) {

        if (baseFileStorageConfigurationDO != null) {

            return baseFileStorageConfigurationDO;

        }

        // 执行获取
        baseFileStorageConfigurationDO = execGetFileStorageConfigurationDO(iBaseFileStorageType);

        if (baseFileStorageConfigurationDO == null) {

            R.errorMsg("操作失败：暂未配置文件存储方式，请联系管理员");

        }

        return baseFileStorageConfigurationDO;

    }

    /**
     * 执行获取
     */
    private static BaseFileStorageConfigurationDO execGetFileStorageConfigurationDO(
        @Nullable IBaseFileStorageType iBaseFileStorageType) {

        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO = null;

        if (iBaseFileStorageType == null) {

            // 获取：默认的存储方式
            baseFileStorageConfigurationDO = ChainWrappers.lambdaQueryChain(baseFileStorageConfigurationMapper)
                .eq(TempEntityNoId::getEnableFlag, true).eq(BaseFileStorageConfigurationDO::getDefaultFlag, true).one();

        } else {

            // 根据传入的类型，选择一个存储方式
            List<BaseFileStorageConfigurationDO> baseFileStorageConfigurationDOList =
                ChainWrappers.lambdaQueryChain(baseFileStorageConfigurationMapper)
                    .eq(TempEntityNoId::getEnableFlag, true)
                    .eq(BaseFileStorageConfigurationDO::getType, iBaseFileStorageType.getCode()).list();

            if (CollUtil.isNotEmpty(baseFileStorageConfigurationDOList)) {

                // 随机选择一个存储方式
                baseFileStorageConfigurationDO = RandomUtil.randomEle(baseFileStorageConfigurationDOList);

            }

        }

        return baseFileStorageConfigurationDO;

    }

    /**
     * 通用保存：文件信息到数据库
     */
    @NotNull
    public static BaseFileDO saveCommonBaseFile(BaseFileUploadBO bo, String fileType, String originalFilename,
        String newFileName, String objectName, String bucketName,
        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO) {

        BaseFileDO baseFileDO = new BaseFileDO();

        baseFileDO.setBelongId(bo.getUserId());

        baseFileDO.setBucketName(bucketName);

        baseFileDO.setUri(objectName);

        baseFileDO.setOriginFileName(MyEntityUtil.getNotNullStr(originalFilename));

        baseFileDO.setNewFileName(newFileName);

        baseFileDO.setFileExtName(fileType);

        baseFileDO.setExtraJson(MyEntityUtil.getNotNullStr(bo.getExtraJson()));

        baseFileDO.setUploadType(bo.getUploadType().getCode());

        baseFileDO.setStorageConfigurationId(baseFileStorageConfigurationDO.getId());

        baseFileDO.setStorageType(baseFileStorageConfigurationDO.getType());

        Long pid = MyEntityUtil.getNotNullPid(bo.getPid());

        baseFileDO.setPid(pid);

        baseFileDO.setType(BaseFileTypeEnum.FILE);

        baseFileDO.setShowFileName(MyEntityUtil.getNotNullStr(originalFilename));

        baseFileDO.setPublicFlag(bo.getUploadType().isPublicFlag());

        baseFileDO.setFileSize(bo.getFile().getSize());

        baseFileDO.setRefId(MyEntityUtil.getNotNullLong(bo.getRefId()));

        baseFileDO.setEnableFlag(true);

        baseFileDO.setRemark(MyEntityUtil.getNotNullStr(bo.getRemark()));

        String pidPathStr = getPidPathStr(pid);

        baseFileDO.setPidPathStr(pidPathStr);

        baseFileService.save(baseFileDO);

        return baseFileDO;

    }

    /**
     * 获取：父节点 id字符串
     */
    public static String getPidPathStr(@Nullable Long pid) {

        pid = MyEntityUtil.getNotNullPid(pid);

        String pidPathStr;

        if (pid.equals(TempConstant.TOP_PID)) {

            pidPathStr = SeparatorUtil.verticalLine(pid);

        } else {

            BaseFileDO parentBaseFileDo =
                baseFileService.lambdaQuery().select(BaseFileDO::getPidPathStr).eq(TempEntity::getId, pid).one();

            if (parentBaseFileDo == null) {

                R.error("操作失败：文件夹已被删除", pid);

            }

            pidPathStr = parentBaseFileDo.getPidPathStr() + SeparatorUtil.verticalLine(pid);

        }

        return pidPathStr;

    }

    /**
     * 上传分片文件-准备工作：公有和私有
     */
    public static BaseFileUploadChunkPreVO uploadChunkPre(BaseFileUploadChunkPreDTO dto, BaseFileUploadBO bo) {

        Long fileSize = dto.getFileSize();

        int chunkSize = LogFilter.baseProperties.getFileChunkSize();

        // 分片总个数
        int chunkTotal =
            BigDecimal.valueOf(fileSize).divide(BigDecimal.valueOf(chunkSize), RoundingMode.CEILING).intValue();

        String fileType = FileNameUtil.extName(dto.getFileName());

        fileType = BaseFileUploadTypeEnum.doCheckFileType(BaseFileUploadTypeEnum.FILE_SYSTEM, fileType);

        if (fileType == null) {
            R.errorMsg("操作失败：暂不支持此文件类型【" + dto.getFileName() + "】，请正确上传文件");
        }

        // 文件传输主键id
        Long transferId = IdGeneratorUtil.nextId();

        Long fileId = uploadCommonHandle(bo, fileType, null, (baseFileDO) -> {

            BaseFileTransferDO baseFileTransferDO = new BaseFileTransferDO();

            baseFileTransferDO.setId(transferId);
            baseFileTransferDO.setUserId(bo.getUserId());
            baseFileTransferDO.setFileId(baseFileDO.getId());
            baseFileTransferDO.setType(BaseFileTransferTypeEnum.UPLOAD);
            baseFileTransferDO.setNewFileName(baseFileDO.getNewFileName());
            baseFileTransferDO.setShowFileName(baseFileDO.getShowFileName());
            baseFileTransferDO.setFileSize(baseFileDO.getFileSize());
            baseFileTransferDO.setStatus(BaseFileTransferStatusEnum.TRANSFER_IN);
            baseFileTransferDO.setFileSign(dto.getFileSign());
            baseFileTransferDO.setChunkSize(chunkSize);
            baseFileTransferDO.setChunkTotal(chunkTotal);
            baseFileTransferDO.setEnableFlag(true);
            baseFileTransferDO.setRemark("");

            baseFileTransferService.save(baseFileTransferDO);

        }, null, bo.getUserId().toString(), false);

        BaseFileUploadChunkPreVO baseFileUploadChunkPreVO = new BaseFileUploadChunkPreVO();

        baseFileUploadChunkPreVO.setFileId(fileId);
        baseFileUploadChunkPreVO.setChunkSize(chunkSize);
        baseFileUploadChunkPreVO.setChunkTotal(chunkTotal);
        baseFileUploadChunkPreVO.setTransferId(transferId);

        return baseFileUploadChunkPreVO;

    }

    /**
     * 下载文件：私有
     */
    @SneakyThrows
    @Nullable
    public static InputStream privateDownload(long fileId, @Nullable CallBack<String> fileNameCallBack) {

        BaseFileDO baseFileDO = getPrivateDownloadBaseFile(fileId);

        if (BaseFileTypeEnum.FOLDER.equals(baseFileDO.getType())) {
            R.errorMsg("操作失败：暂不支持下载文件夹");
        }

        if (BooleanUtil.isFalse(baseFileDO.getPublicFlag())) { // 如果：不是公开下载

            Long currentUserId = MyUserUtil.getCurrentUserId();

            // 检查：是否是该文件的拥有者
            if (!currentUserId.equals(baseFileDO.getBelongId()) && !MyUserUtil.getCurrentUserAdminFlag(currentUserId)) {

                // 检查：是否有可读权限
                boolean exists = baseFileAuthService.lambdaQuery().eq(BaseFileAuthDO::getFileId, fileId)
                    .eq(BaseFileAuthDO::getUserId, currentUserId).eq(BaseFileAuthDO::getReadFlag, true)
                    .eq(TempEntityNoId::getEnableFlag, true).exists();

                if (BooleanUtil.isFalse(exists)) {
                    R.error(TempBizCodeEnum.INSUFFICIENT_PERMISSIONS);
                }

            }

        }

        IBaseFileStorage iBaseFileStorage = BASE_FILE_STORAGE_MAP.get(baseFileDO.getStorageType());

        if (iBaseFileStorage == null) {

            R.error("操作失败：文件存储位置不存在", baseFileDO.getStorageType());

        }

        Long storageConfigurationId = baseFileDO.getStorageConfigurationId();

        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO =
            ChainWrappers.lambdaQueryChain(baseFileStorageConfigurationMapper)
                .eq(TempEntity::getId, storageConfigurationId).eq(TempEntityNoId::getEnableFlag, true).one();

        if (baseFileStorageConfigurationDO == null) {

            R.error("操作失败：文件存储配置不存在", storageConfigurationId);

        }

        if (fileNameCallBack != null) {

            fileNameCallBack.setValue(baseFileDO.getOriginFileName());

        }

        return iBaseFileStorage.download(baseFileDO.getBucketName(), baseFileDO.getUri(),
            baseFileStorageConfigurationDO);

    }

    @NotNull
    private static BaseFileDO getPrivateDownloadBaseFile(long fileId) {

        BaseFileDO baseFileDO = getBaseFileBaseLambdaQuery().eq(TempEntity::getId, fileId).one();

        if (baseFileDO == null) {
            R.errorMsg("操作失败：文件不存在");
        }

        return baseFileDO;

    }

    private static LambdaQueryChainWrapper<BaseFileDO> getBaseFileBaseLambdaQuery() {

        return baseFileService.lambdaQuery()
            .select(BaseFileDO::getBucketName, BaseFileDO::getNewFileName, BaseFileDO::getPublicFlag,
                BaseFileDO::getStorageType, BaseFileDO::getType, TempEntity::getId, BaseFileDO::getUri,
                BaseFileDO::getStorageConfigurationId, BaseFileDO::getBelongId, BaseFileDO::getOriginFileName)
            .eq(TempEntityNoId::getEnableFlag, true);

    }

    /**
     * 获取：公开文件的 url
     */
    @NotNull
    public static Map<Long, String> getPublicUrl(Set<Long> fileIdSet) {

        // 先移除：所有 -1的文件 id
        fileIdSet.removeAll(CollUtil.newHashSet(-1L));

        if (CollUtil.isEmpty(fileIdSet)) {
            return MapUtil.newHashMap();
        }

        List<BaseFileDO> baseFileDOList =
            getBaseFileBaseLambdaQuery().in(TempEntity::getId, fileIdSet).eq(BaseFileDO::getPublicFlag, true).list();

        Map<Long, String> result = new HashMap<>(baseFileDOList.size());

        Set<Long> baseFileStorageConfigurationIdSet =
            baseFileDOList.stream().map(BaseFileDO::getStorageConfigurationId).collect(Collectors.toSet());

        if (CollUtil.isEmpty(baseFileStorageConfigurationIdSet)) {
            return MapUtil.newHashMap();
        }

        List<BaseFileStorageConfigurationDO> baseFileStorageConfigurationDOList =
            ChainWrappers.lambdaQueryChain(baseFileStorageConfigurationMapper)
                .in(TempEntity::getId, baseFileStorageConfigurationIdSet).eq(TempEntityNoId::getEnableFlag, true)
                .list();

        if (CollUtil.isEmpty(baseFileStorageConfigurationDOList)) {
            return MapUtil.newHashMap();
        }

        // 通过：id进行分组
        Map<Long, BaseFileStorageConfigurationDO> baseFileStorageConfigurationIdMap =
            baseFileStorageConfigurationDOList.stream().collect(Collectors.toMap(TempEntity::getId, it -> it));

        for (BaseFileDO item : baseFileDOList) {

            BaseFileStorageConfigurationDO baseFileStorageConfigurationDO =
                baseFileStorageConfigurationIdMap.get(item.getStorageConfigurationId());

            if (baseFileStorageConfigurationDO == null) {
                continue;
            }

            IBaseFileStorage iBaseFileStorage = BASE_FILE_STORAGE_MAP.get(baseFileStorageConfigurationDO.getType());

            if (iBaseFileStorage != null) {

                result.put(item.getId(),
                    iBaseFileStorage.getUrl(item.getUri(), item.getBucketName(), baseFileStorageConfigurationDO));

            }

        }

        return result;

    }

    /**
     * 批量删除文件：公有和私有
     *
     * @param checkBelongFlag 是否检查：文件拥有者才可以删除
     */
    @SneakyThrows
    public static void removeByFileIdSet(Set<Long> fileIdSet, boolean checkBelongFlag) {

        if (CollUtil.isEmpty(fileIdSet)) {
            return;
        }

        List<BaseFileDO> baseFileDoList;

        LambdaQueryChainWrapper<BaseFileDO> lambdaQueryChainWrapper =
            getRemoveLambdaQueryChainWrapper().in(TempEntity::getId, fileIdSet);

        if (checkBelongFlag) {

            Long currentUserId = MyUserUtil.getCurrentUserId();

            if (MyUserUtil.getCurrentUserAdminFlag(currentUserId)) {

                // admin 用户可以删除
                baseFileDoList = lambdaQueryChainWrapper.list();

            } else {

                // 只有：文件拥有者才可以删除
                baseFileDoList = lambdaQueryChainWrapper.eq(BaseFileDO::getBelongId, currentUserId).list();

            }

        } else {

            // 直接删除
            baseFileDoList = lambdaQueryChainWrapper.list();

        }

        if (baseFileDoList.size() == 0) {
            R.errorMsg("操作失败：文件拥有者才可以删除");
        }

        List<BaseFileDO> folderList = new ArrayList<>();

        List<BaseFileDO> fileList = new ArrayList<>();

        Set<Long> removeFileIdSet = new HashSet<>(baseFileDoList.size());

        for (BaseFileDO item : baseFileDoList) {

            removeFileIdSet.add(item.getId());

            if (BaseFileTypeEnum.FOLDER.equals(item.getType())) {

                folderList.add(item);

            } else {

                fileList.add(item);

            }

        }

        // 递归查找：folder下面的文件和文件夹
        for (BaseFileDO folder : folderList) {

            deepFindFolderAndFile(folder, removeFileIdSet, fileList);

        }

        // 移除：文件存储服务器里面的文件
        removeBaseFileStorage(fileList);

        // 移除：所有文件
        TransactionUtil.exec(() -> {

            baseFileService.removeBatchByIds(removeFileIdSet);

            baseFileAuthService.lambdaUpdate().in(BaseFileAuthDO::getFileId, removeFileIdSet).remove();

            if (CollUtil.isNotEmpty(iBaseFileRemoveList) && CollUtil.isNotEmpty(removeFileIdSet)) {

                for (IBaseFileRemove item : iBaseFileRemoveList) {

                    item.handle(removeFileIdSet); // 额外处理：当文件进行移除时

                }

            }

        });

    }

    /**
     * 获取：删除文件时的 LambdaQueryChainWrapper
     */
    private static LambdaQueryChainWrapper<BaseFileDO> getRemoveLambdaQueryChainWrapper() {

        return baseFileService.lambdaQuery()
            .select(BaseFileDO::getBucketName, BaseFileDO::getUri, BaseFileDO::getType, TempEntity::getId,
                BaseFileDO::getStorageConfigurationId);

    }

    /**
     * 递归查找：folder下面的文件和文件夹
     */
    private static void deepFindFolderAndFile(BaseFileDO folder, Set<Long> removeFileIdSet, List<BaseFileDO> fileList) {

        List<BaseFileDO> baseFileDoList =
            getRemoveLambdaQueryChainWrapper().eq(BaseFileDO::getPid, folder.getId()).list();

        if (CollUtil.isEmpty(baseFileDoList)) {
            return;
        }

        for (BaseFileDO item : baseFileDoList) {

            boolean contains = removeFileIdSet.contains(item.getId());

            if (!contains) {

                removeFileIdSet.add(item.getId());

            }

            if (BaseFileTypeEnum.FOLDER.equals(item.getType())) {

                deepFindFolderAndFile(item, removeFileIdSet, fileList);

            } else {

                if (!contains) {

                    fileList.add(item);

                }

            }

        }

    }

    /**
     * 移除：文件存储服务器里面的文件
     */
    private static void removeBaseFileStorage(List<BaseFileDO> baseFileDoList) {

        // 移除：文件存储系统里面的文件
        handleBaseFileStorage(baseFileDoList, (iBaseFileStorage, map, baseFileStorageConfigurationDO) -> {

            // 根据：桶名，进行分类
            Map<String, Set<String>> bucketGroupMap = map.getValue().stream().collect(
                Collectors.groupingBy(BaseFileDO::getBucketName,
                    Collectors.mapping(BaseFileDO::getUri, Collectors.toSet())));

            for (Map.Entry<String, Set<String>> item : bucketGroupMap.entrySet()) {

                iBaseFileStorage.remove(item.getKey(), item.getValue(), baseFileStorageConfigurationDO);

            }

        });

    }

    /**
     * 复制：文件存储服务器里面的文件
     */
    public static void copyBaseFileStorage(List<BaseFileDO> baseFileDoList) {

        // 移除：文件存储系统里面的文件
        handleBaseFileStorage(baseFileDoList, (iBaseFileStorage, map, baseFileStorageConfigurationDO) -> {

            for (BaseFileDO item : map.getValue()) {

                if (StrUtil.isBlank(item.getOldBucketName())) {
                    continue;
                }

                if (StrUtil.isBlank(item.getOldUri())) {
                    continue;
                }

                if (StrUtil.isBlank(item.getBucketName())) {
                    continue;
                }

                if (StrUtil.isBlank(item.getUri())) {
                    continue;
                }

                iBaseFileStorage.copy(item.getOldBucketName(), item.getOldUri(), item.getBucketName(), item.getUri(),
                    baseFileStorageConfigurationDO);

            }

        });

    }

    /**
     * 处理：文件存储服务器里面的文件
     */
    private static void handleBaseFileStorage(List<BaseFileDO> baseFileDoList,
        VoidFunc3<IBaseFileStorage, Entry<Long, List<BaseFileDO>>, BaseFileStorageConfigurationDO> voidFunc3) {

        if (CollUtil.isEmpty(baseFileDoList)) {
            return;
        }

        // 根据：存储类型 id分类
        Map<Long, List<BaseFileDO>> storageTypeGroupMap =
            baseFileDoList.stream().collect(Collectors.groupingBy(BaseFileDO::getStorageConfigurationId));

        List<BaseFileStorageConfigurationDO> baseFileStorageConfigurationDoList =
            ChainWrappers.lambdaQueryChain(baseFileStorageConfigurationMapper)
                .in(TempEntity::getId, storageTypeGroupMap.keySet()).list();

        if (CollUtil.isEmpty(baseFileStorageConfigurationDoList)) {
            return;
        }

        // 通过：id进行分组
        Map<Long, BaseFileStorageConfigurationDO> baseFileStorageConfigurationIdMap =
            baseFileStorageConfigurationDoList.stream().collect(Collectors.toMap(TempEntity::getId, it -> it));

        for (Map.Entry<Long, List<BaseFileDO>> item : storageTypeGroupMap.entrySet()) {

            BaseFileStorageConfigurationDO baseFileStorageConfigurationDO =
                baseFileStorageConfigurationIdMap.get(item.getKey());

            if (baseFileStorageConfigurationDO == null) {
                continue;
            }

            IBaseFileStorage iBaseFileStorage = BASE_FILE_STORAGE_MAP.get(baseFileStorageConfigurationDO.getType());

            if (iBaseFileStorage == null) {
                continue;
            }

            voidFunc3.call(iBaseFileStorage, item, baseFileStorageConfigurationDO);

        }

    }

    /**
     * 获取：临时文件的 fileId，byteArr 和 fileTemp 只会处理一个
     *
     * @param fileType 传递 byteArr时，需指定 fileType
     */
    @SneakyThrows
    public static Long getTempFileId(String remark, Long userId, @Nullable String fileType, byte @Nullable [] byteArr,
        @Nullable File fileTemp) {

        File file;

        String fileName;

        if (byteArr != null && byteArr.length != 0) {

            fileName = IdUtil.simpleUUID() + "." + fileType;

            file = FileUtil.touch(TempFileTempPathConstant.FILE_TEMP_PATH + fileName);

            FileUtil.writeBytes(byteArr, file);

        } else {

            file = fileTemp;

            fileName = fileTemp.getName();

            fileType = FileUtil.extName(fileName);

        }

        try {

            MultipartFile multipartFile = MultipartFileUtil.getByFile(file);

            BaseFileUploadBO bo = new BaseFileUploadBO();

            bo.setFile(multipartFile);

            bo.setUploadType(BaseFileUploadTypeEnum.TEMP_FILE);

            bo.setRemark(MyStrUtil.maxLength(remark, 300));

            bo.setExtraJson("");

            bo.setUserId(userId);

            // 通用：上传处理
            return BaseFileUtil.uploadCommonHandle(bo, fileType, null, null, null, bo.getUserId().toString(), true);

        } finally {

            FileUtil.del(file); // 删除文件

        }

    }

    /**
     * 获取：临时文件的 fileUrl，byteArr 和 fileTemp 只会处理一个
     *
     * @param fileType 传递 byteArr时，需指定 fileType，传递 fileTemp时，不用指定 fileType
     */
    public static String getTempFileUrl(String remark, Long userId, @Nullable String fileType,
        byte @Nullable [] byteArr, @Nullable File fileTemp) {

        Long fileId = getTempFileId(remark, userId, fileType, byteArr, fileTemp);

        // 获取：文件链接
        Map<Long, String> publicUrlMap = BaseFileUtil.getPublicUrl(CollUtil.newHashSet(fileId));

        return publicUrlMap.get(fileId);

    }

}
