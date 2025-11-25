package com.kar20240901.be.base.web.util.file;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.json.JSONUtil;
import com.aliyun.oss.model.PartETag;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.configuration.log.LogFilter;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.base.BaseUserInfoMapper;
import com.kar20240901.be.base.web.mapper.file.BaseFileStorageConfigurationMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImGroupMapper;
import com.kar20240901.be.base.web.model.bo.file.BaseFileComposeBO;
import com.kar20240901.be.base.web.model.bo.file.BaseFilePrivateDownloadBO;
import com.kar20240901.be.base.web.model.bo.file.BaseFileUploadBO;
import com.kar20240901.be.base.web.model.bo.file.BaseFileUploadChunkBO;
import com.kar20240901.be.base.web.model.configuration.file.IBaseFileRemove;
import com.kar20240901.be.base.web.model.configuration.file.IBaseFileStorage;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.constant.base.TempFileTempPathConstant;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoId;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoIdSuper;
import com.kar20240901.be.base.web.model.domain.base.TempUserInfoDO;
import com.kar20240901.be.base.web.model.domain.file.BaseFileAuthDO;
import com.kar20240901.be.base.web.model.domain.file.BaseFileDO;
import com.kar20240901.be.base.web.model.domain.file.BaseFileStorageConfigurationDO;
import com.kar20240901.be.base.web.model.domain.file.BaseFileTransferChunkDO;
import com.kar20240901.be.base.web.model.domain.file.BaseFileTransferDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImGroupDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.file.BaseFileUploadFileSystemChunkDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileUploadFileSystemChunkPreDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileUploadFileSystemDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileUploadFileSystemPreDTO;
import com.kar20240901.be.base.web.model.enums.base.BaseRedisKeyEnum;
import com.kar20240901.be.base.web.model.enums.file.BaseFileStorageTypeEnum;
import com.kar20240901.be.base.web.model.enums.file.BaseFileTransferStatusEnum;
import com.kar20240901.be.base.web.model.enums.file.BaseFileTransferTypeEnum;
import com.kar20240901.be.base.web.model.enums.file.BaseFileTypeEnum;
import com.kar20240901.be.base.web.model.enums.file.BaseFileUploadTypeEnum;
import com.kar20240901.be.base.web.model.interfaces.file.IBaseFileStorageType;
import com.kar20240901.be.base.web.model.interfaces.file.IBaseFileUploadType;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.file.BaseFilePrivateDownloadVO;
import com.kar20240901.be.base.web.model.vo.file.BaseFileUploadFileSystemChunkPreVO;
import com.kar20240901.be.base.web.model.vo.file.BaseFileUploadFileSystemChunkVO;
import com.kar20240901.be.base.web.model.vo.file.BaseFileUploadFileSystemPreVO;
import com.kar20240901.be.base.web.service.file.BaseFileAuthService;
import com.kar20240901.be.base.web.service.file.BaseFileService;
import com.kar20240901.be.base.web.service.file.BaseFileTransferChunkService;
import com.kar20240901.be.base.web.service.file.BaseFileTransferService;
import com.kar20240901.be.base.web.util.base.CallBack;
import com.kar20240901.be.base.web.util.base.IdGeneratorUtil;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import com.kar20240901.be.base.web.util.base.MyStrUtil;
import com.kar20240901.be.base.web.util.base.MyTryUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.base.RedissonUtil;
import com.kar20240901.be.base.web.util.base.SeparatorUtil;
import com.kar20240901.be.base.web.util.base.TransactionUtil;
import com.kar20240901.be.base.web.util.base.VoidFunc3;
import com.kar20240901.be.base.web.util.im.BaseImGroupUtil;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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

    private static BaseImGroupMapper baseImGroupMapper;

    @Resource
    public void setBaseImGroupMapper(BaseImGroupMapper baseImGroupMapper) {
        BaseFileUtil.baseImGroupMapper = baseImGroupMapper;
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

                }, null, bo.getUserId().toString(), true, true, null, null);

        } else if (BaseFileUploadTypeEnum.IM_GROUP_AVATAR.equals(bo.getUploadType())) {

            // 如果是：im群组头像
            // 检查：是否有权限
            BaseImGroupUtil.checkGroupAuth(bo.getRefId(), false);

            // 通用：上传处理
            resultBaseFileId = uploadCommonHandle(bo, fileType, null,

                (baseFileDO) -> {

                    ChainWrappers.lambdaUpdateChain(baseImGroupMapper).eq(BaseImGroupDO::getId, bo.getRefId())
                        .set(BaseImGroupDO::getAvatarFileId, baseFileDO.getId()).update();

                }, null, bo.getUserId().toString(), true, true, null, null);

        }

        return resultBaseFileId;

    }

    /**
     * 存储传输信息
     */
    private static void saveBaseFileTransferForUpload(BaseFileUploadBO bo, BaseFileDO baseFileDO, Long transferId) {

        BaseFileTransferDO baseFileTransferDO = new BaseFileTransferDO();

        baseFileTransferDO.setId(transferId);
        baseFileTransferDO.setUserId(bo.getUserId());
        baseFileTransferDO.setFileId(baseFileDO.getId());
        baseFileTransferDO.setType(BaseFileTransferTypeEnum.UPLOAD);
        baseFileTransferDO.setNewFileName(baseFileDO.getNewFileName());
        baseFileTransferDO.setShowFileName(baseFileDO.getShowFileName());
        baseFileTransferDO.setFileSize(baseFileDO.getFileSize());
        baseFileTransferDO.setStatus(BaseFileTransferStatusEnum.TRANSFER_IN);
        baseFileTransferDO.setFileSign("");
        baseFileTransferDO.setChunkSize(0);
        baseFileTransferDO.setChunkTotal(0);

        baseFileTransferDO.setBucketName(baseFileDO.getBucketName());
        baseFileTransferDO.setStorageConfigurationId(baseFileDO.getStorageConfigurationId());
        baseFileTransferDO.setStorageType(baseFileDO.getStorageType());
        baseFileTransferDO.setUri(baseFileDO.getUri());

        baseFileTransferDO.setEnableFlag(true);
        baseFileTransferDO.setRemark("");

        baseFileTransferService.save(baseFileTransferDO);

    }

    /**
     * 设置文件权限
     */
    public static void saveFileAuth(Long userId, BaseFileDO baseFileDO) {

        BaseFileAuthDO baseFileAuthDO = new BaseFileAuthDO();

        baseFileAuthDO.setFileId(baseFileDO.getId());
        baseFileAuthDO.setUserId(userId);
        baseFileAuthDO.setReadFlag(true);
        baseFileAuthDO.setWriteFlag(true);
        baseFileAuthDO.setEnableFlag(true);

        baseFileAuthDO.setRemark("");

        baseFileAuthService.save(baseFileAuthDO);

    }

    /**
     * 设置文件权限
     */
    public static void saveBatchFileAuth(Long userId, Collection<Long> fileIdColl) {

        List<BaseFileAuthDO> baseFileAuthDoList = new ArrayList<>();

        for (Long item : fileIdColl) {

            BaseFileAuthDO baseFileAuthDO = new BaseFileAuthDO();

            baseFileAuthDO.setFileId(item);
            baseFileAuthDO.setUserId(userId);
            baseFileAuthDO.setReadFlag(true);
            baseFileAuthDO.setWriteFlag(true);
            baseFileAuthDO.setEnableFlag(true);

            baseFileAuthDO.setRemark("");

            baseFileAuthDoList.add(baseFileAuthDO);

        }

        baseFileAuthService.saveBatch(baseFileAuthDoList);

    }

    /**
     * 通用：上传处理
     *
     * @return 文件主键 id
     */
    @NotNull
    public static Long uploadCommonHandle(BaseFileUploadBO bo, String fileType,
        @Nullable IBaseFileStorageType iBaseFileStorageType, @Nullable Consumer<BaseFileDO> consumer,
        @Nullable BaseFileStorageConfigurationDO baseFileStorageConfigurationDoTemp, @Nullable String otherFolderName,
        boolean storageUploadFlag, boolean saveBaseFileFlag, String newFileNameTemp, CallBack<String> uriCallBack) {

        // 获取：存储方式的配置
        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO =
            getBaseFileStorageConfigurationDO(iBaseFileStorageType, baseFileStorageConfigurationDoTemp);

        // 获取：存储接口对象
        IBaseFileStorage iBaseFileStorage = getIbaseFileStorage(baseFileStorageConfigurationDO);

        String originalFilename = bo.getFile().getOriginalFilename(); // 旧的文件名

        String folderName = bo.getUploadType().getFolderName();

        // 获取：新的文件名
        String newFileName = getNewFileName(fileType, newFileNameTemp);

        // 获取：uri
        String objectName = getObjectName(otherFolderName, folderName, newFileName);

        if (uriCallBack != null) {

            uriCallBack.setValue(objectName);

        }

        // 获取：桶名
        String bucketName = getBucketName(bo.getUploadType(), baseFileStorageConfigurationDO);

        if (storageUploadFlag) {

            // 执行：文件上传
            iBaseFileStorage.upload(bucketName, objectName, bo.getFile(), baseFileStorageConfigurationDO);

        }

        if (!saveBaseFileFlag) {

            return TempConstant.NEGATIVE_ONE;

        }

        return TransactionUtil.exec(() -> {

            // 获取：BaseFileDO对象
            BaseFileDO baseFileDO = getBaseFileDO(bo, fileType, originalFilename, newFileName, objectName, bucketName,
                baseFileStorageConfigurationDO);

            if (consumer != null) {

                consumer.accept(baseFileDO);

            }

            baseFileService.save(baseFileDO);

            return baseFileDO.getId();

        });

    }

    /**
     * 获取：桶名
     */
    @NotNull
    public static String getBucketName(IBaseFileUploadType uploadType,
        @NotNull BaseFileStorageConfigurationDO baseFileStorageConfigurationDO) {
        String bucketName;

        if (uploadType.isPublicFlag()) {

            bucketName = baseFileStorageConfigurationDO.getBucketPublicName();

        } else {

            bucketName = baseFileStorageConfigurationDO.getBucketPrivateName();

        }

        if (StrUtil.isBlank(bucketName)) {

            R.errorMsg("操作失败：桶名为空，请联系管理员");

        }

        return bucketName;

    }

    /**
     * 获取：objectName
     */
    @NotNull
    public static String getObjectName(@Nullable String otherFolderName, String folderName, String newFileName) {

        if (StrUtil.isNotBlank(otherFolderName)) {

            folderName = folderName + "/" + otherFolderName;

        }

        return folderName + "/" + newFileName;

    }

    /**
     * 获取：新的文件名
     */
    @NotNull
    public static String getNewFileName(String fileType, String newFileNameTemp) {

        String newFileName;

        if (StrUtil.isBlank(newFileNameTemp)) {

            newFileName = IdUtil.simpleUUID() + "." + fileType; // 新的文件名

        } else {

            newFileName = newFileNameTemp;

        }

        return newFileName;

    }

    /**
     * 获取：IBaseFileStorage
     */
    @NotNull
    public static IBaseFileStorage getIbaseFileStorage(BaseFileStorageConfigurationDO baseFileStorageConfigurationDO) {

        Integer storageType = baseFileStorageConfigurationDO.getType();

        IBaseFileStorage iBaseFileStorage = BASE_FILE_STORAGE_MAP.get(storageType);

        if (iBaseFileStorage == null) {

            R.error("操作失败：文件存储方式未找到", storageType);

        }

        return iBaseFileStorage;

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
     * 获取：BaseFileDO对象
     */
    private static @NotNull BaseFileDO getBaseFileDO(BaseFileUploadBO bo, String fileType, String originalFilename,
        String newFileName, String objectName, String bucketName,
        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO) {

        Long fileId = IdGeneratorUtil.nextId();

        BaseFileDO baseFileDO = new BaseFileDO();

        baseFileDO.setId(fileId);

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

        // 获取：pidPathStr
        String pidPathStr = getPidPathStr(pid);

        baseFileDO.setPidPathStr(pidPathStr);

        baseFileDO.setUploadFlag(false);

        baseFileDO.setCreateId(bo.getUserId());

        baseFileDO.setUpdateId(bo.getUserId());

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
     * 文件系统上传文件-准备工作：公有和私有
     */
    public static BaseFileUploadFileSystemPreVO uploadFileSystemPre(BaseFileUploadFileSystemPreDTO dto,
        BaseFileUploadBO bo) {

        String fileType = FileNameUtil.extName(dto.getFileName());

        fileType = BaseFileUploadTypeEnum.doCheckFileType(BaseFileUploadTypeEnum.FILE_SYSTEM, fileType);

        if (fileType == null) {
            R.errorMsg("操作失败：暂不支持此文件类型【" + dto.getFileName() + "】，请正确上传文件");
        }

        // 文件传输主键id
        Long transferId = IdGeneratorUtil.nextId();

        Long fileId = uploadCommonHandle(bo, fileType, null, (baseFileDO) -> {

            baseFileDO.setUploadFlag(true); // 设置为：上传中

            // 存储传输信息
            saveBaseFileTransferForUpload(bo, baseFileDO, transferId);

            // 设置文件权限
            saveFileAuth(bo.getUserId(), baseFileDO);

        }, null, bo.getUserId().toString(), false, true, null, null);

        BaseFileUploadFileSystemPreVO baseFileUploadFileSystemPreVO = new BaseFileUploadFileSystemPreVO();

        baseFileUploadFileSystemPreVO.setFileId(fileId);
        baseFileUploadFileSystemPreVO.setTransferId(transferId);

        return baseFileUploadFileSystemPreVO;

    }

    /**
     * 文件系统上传分片文件-准备工作：公有和私有
     */
    public static BaseFileUploadFileSystemChunkPreVO uploadFileSystemChunkPre(BaseFileUploadFileSystemChunkPreDTO dto,
        BaseFileUploadBO bo) {

        Long fileSize = dto.getFileSize();

        Integer chunkSize = LogFilter.baseProperties.getFileChunkSize();

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

            baseFileDO.setUploadFlag(true); // 设置为：上传中

            // 存储传输信息
            saveBaseFileTransferForUploadChunkPre(dto, bo, baseFileDO, transferId, chunkSize, chunkTotal);

            // 设置文件权限
            saveFileAuth(bo.getUserId(), baseFileDO);

        }, null, bo.getUserId().toString(), false, true, null, null);

        BaseFileUploadFileSystemChunkPreVO baseFileUploadFileSystemChunkPreVO =
            new BaseFileUploadFileSystemChunkPreVO();

        baseFileUploadFileSystemChunkPreVO.setFileId(fileId);
        baseFileUploadFileSystemChunkPreVO.setChunkSize(chunkSize);
        baseFileUploadFileSystemChunkPreVO.setChunkTotal(chunkTotal);
        baseFileUploadFileSystemChunkPreVO.setTransferId(transferId);

        return baseFileUploadFileSystemChunkPreVO;

    }

    /**
     * 存储传输信息
     */
    private static void saveBaseFileTransferForUploadChunkPre(BaseFileUploadFileSystemChunkPreDTO dto,
        BaseFileUploadBO bo, BaseFileDO baseFileDO, Long transferId, Integer chunkSize, int chunkTotal) {

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

        baseFileTransferDO.setBucketName(baseFileDO.getBucketName());
        baseFileTransferDO.setStorageConfigurationId(baseFileDO.getStorageConfigurationId());
        baseFileTransferDO.setStorageType(baseFileDO.getStorageType());
        baseFileTransferDO.setUri(baseFileDO.getUri());

        baseFileTransferDO.setEnableFlag(true);
        baseFileTransferDO.setRemark("");

        baseFileTransferService.save(baseFileTransferDO);

    }

    /**
     * 文件系统上传文件：公有和私有
     */
    public static Long uploadFileSystem(BaseFileUploadFileSystemDTO dto, Long userId) {

        BaseFileUploadTypeEnum uploadType = BaseFileUploadTypeEnum.FILE_SYSTEM;

        Date date = new Date();

        return RedissonUtil.doLock(BaseRedisKeyEnum.PRE_FILE_TRANSFER_CHUNK_NUM + ":" + dto.getTransferId(), () -> {

            BaseFileTransferDO baseFileTransferDO =
                baseFileTransferService.infoById(new NotNullId(dto.getTransferId()));

            if (baseFileTransferDO == null) {
                R.error("操作失败：传输信息不存在，请重新上传", dto.getTransferId());
            }

            if (BaseFileTransferStatusEnum.COMPOSE_COMPLETE.equals(baseFileTransferDO.getStatus())) {
                R.error("操作失败：传输已完成，请重新上传", dto.getTransferId());
            }

            BaseFileDO baseFileDO =
                baseFileService.lambdaQuery().eq(TempEntity::getId, baseFileTransferDO.getFileId()).one();

            if (baseFileDO == null) {

                baseFileTransferService.deleteByIdSet(new NotEmptyIdSet(CollUtil.newHashSet(dto.getTransferId())));

                R.error("操作失败：请重新上传", dto.getTransferId());

            }

            // 获取：uuid文件名
            String newFileName = baseFileDO.getNewFileName();

            // 获取：存储方式的配置
            BaseFileStorageConfigurationDO baseFileStorageConfigurationDO =
                getBaseFileStorageConfigurationDO(null, null);

            // 获取：存储接口对象
            IBaseFileStorage iBaseFileStorage = getIbaseFileStorage(baseFileStorageConfigurationDO);

            // 获取：uri
            String objectName = getObjectName(userId.toString(), uploadType.getFolderName(), newFileName);

            // 获取：桶名
            String bucketName = getBucketName(uploadType, baseFileStorageConfigurationDO);

            // 执行：文件上传
            iBaseFileStorage.upload(bucketName, objectName, dto.getFile(), baseFileStorageConfigurationDO);

            // 更新传输状态
            baseFileTransferService.lambdaUpdate().eq(BaseFileTransferDO::getId, dto.getTransferId())
                .set(BaseFileTransferDO::getStatus, BaseFileTransferStatusEnum.COMPOSE_COMPLETE)
                .set(TempEntityNoIdSuper::getUpdateTime, date).update();

            // 更新文件状态
            baseFileService.lambdaUpdate().eq(TempEntity::getId, baseFileTransferDO.getFileId())
                .set(BaseFileDO::getUploadFlag, false).update();

            return baseFileTransferDO.getFileId();

        });

    }

    /**
     * 文件系统上传分片文件：公有和私有
     */
    public static String uploadFileSystemChunk(BaseFileUploadFileSystemChunkDTO dto, Long userId) {

        BaseFileUploadTypeEnum uploadType = BaseFileUploadTypeEnum.FILE_SYSTEM;

        BaseFileTransferDO baseFileTransferDO = baseFileTransferService.infoById(new NotNullId(dto.getTransferId()));

        if (baseFileTransferDO == null) {
            R.error("操作失败：传输信息不存在，请重新上传", dto.getTransferId());
        }

        boolean overChunkSizeFlag = dto.getFile().getSize() > baseFileTransferDO.getChunkSize();

        if (overChunkSizeFlag) {
            R.errorMsg("操作失败：分片大小超过：【" + DataSizeUtil.format(baseFileTransferDO.getChunkSize()) + "】");
        }

        boolean overChunkTotalFlag = dto.getChunkNum() > baseFileTransferDO.getChunkTotal();

        if (overChunkTotalFlag) {
            R.errorMsg("操作失败：分片编号超过：【" + baseFileTransferDO.getChunkTotal() + "】");
        }

        BaseFileDO baseFileDO =
            baseFileService.lambdaQuery().eq(TempEntity::getId, baseFileTransferDO.getFileId()).one();

        if (baseFileDO == null) {

            baseFileTransferService.deleteByIdSet(new NotEmptyIdSet(CollUtil.newHashSet(dto.getTransferId())));

            R.error("操作失败：请重新上传", baseFileTransferDO.getFileId());

        }

        return RedissonUtil.doLock(
            BaseRedisKeyEnum.PRE_FILE_TRANSFER_CHUNK_NUM + ":" + baseFileTransferDO.getId() + ":" + dto.getChunkNum(),
            () -> {

                boolean exists = baseFileTransferChunkService.lambdaQuery()
                    .eq(BaseFileTransferChunkDO::getTransferId, baseFileTransferDO.getId())
                    .eq(BaseFileTransferChunkDO::getChunkNum, dto.getChunkNum()).exists();

                if (exists) {
                    R.errorMsg("操作失败：该分片编号已经上传过：【" + dto.getChunkNum() + "】");
                }

                String fileType = "chunk." + dto.getChunkNum();

                String chunkFileName = baseFileTransferDO.getNewFileName() + "." + fileType;

                // 获取：存储方式的配置
                BaseFileStorageConfigurationDO baseFileStorageConfigurationDO =
                    getBaseFileStorageConfigurationDO(null, null);

                // 获取：存储接口对象
                IBaseFileStorage iBaseFileStorage = getIbaseFileStorage(baseFileStorageConfigurationDO);

                // 获取：uri
                String objectName = getObjectName(userId.toString(), uploadType.getFolderName(), chunkFileName);

                // 获取：桶名
                String bucketName = getBucketName(uploadType, baseFileStorageConfigurationDO);

                BaseFileUploadChunkBO baseFileUploadChunkBO = new BaseFileUploadChunkBO();

                baseFileUploadChunkBO.setUploadId(dto.getTransferId().toString());
                baseFileUploadChunkBO.setPartNumber(dto.getChunkNum());

                // 执行：文件上传
                BaseFileUploadFileSystemChunkVO baseFileUploadFileSystemChunkVO =
                    iBaseFileStorage.uploadChunk(bucketName, objectName, dto.getFile(), baseFileStorageConfigurationDO,
                        baseFileUploadChunkBO);

                // 获取：BaseFileTransferChunkDO对象
                BaseFileTransferChunkDO baseFileTransferChunkDO =
                    getBaseFileTransferChunkDO(dto, baseFileTransferDO, objectName, chunkFileName,
                        baseFileUploadFileSystemChunkVO);

                baseFileTransferChunkService.save(baseFileTransferChunkDO);

                return TempBizCodeEnum.OK;

            });

    }

    /**
     * 获取：BaseFileTransferChunkDO对象
     */
    @NotNull
    private static BaseFileTransferChunkDO getBaseFileTransferChunkDO(BaseFileUploadFileSystemChunkDTO dto,
        BaseFileTransferDO baseFileTransferDO, String objectName, String chunkFileName,
        BaseFileUploadFileSystemChunkVO baseFileUploadFileSystemChunkVO) {

        Integer chunkNum = dto.getChunkNum();

        Integer chunkSize = baseFileTransferDO.getChunkSize();

        long fileSize = dto.getFile().getSize();

        long chunkBeginNum = (long)chunkSize * (chunkNum - 1);

        long chunkEndNum = chunkBeginNum + fileSize - 1;

        BaseFileTransferChunkDO baseFileTransferChunkDO = new BaseFileTransferChunkDO();

        baseFileTransferChunkDO.setTransferId(baseFileTransferDO.getId());
        baseFileTransferChunkDO.setUserId(baseFileTransferDO.getUserId());
        baseFileTransferChunkDO.setFileId(baseFileTransferDO.getFileId());
        baseFileTransferChunkDO.setChunkBeginNum(chunkBeginNum);
        baseFileTransferChunkDO.setChunkEndNum(chunkEndNum);
        baseFileTransferChunkDO.setChunkSize(
            baseFileTransferChunkDO.getChunkEndNum() - baseFileTransferChunkDO.getChunkBeginNum() + 1);
        baseFileTransferChunkDO.setChunkNum(chunkNum);
        baseFileTransferChunkDO.setCurrentSize(fileSize);
        baseFileTransferChunkDO.setUri(objectName);
        baseFileTransferChunkDO.setShowFileName(chunkFileName);
        baseFileTransferChunkDO.setRefData("");
        baseFileTransferChunkDO.setEnableFlag(true);
        baseFileTransferChunkDO.setRemark("");

        if (BaseFileStorageTypeEnum.ALI_YUN.getCode() == baseFileTransferDO.getStorageType()) {

            baseFileTransferChunkDO.setRefData(JSONUtil.toJsonStr(baseFileUploadFileSystemChunkVO.getPartEtag()));

        }

        return baseFileTransferChunkDO;

    }

    /**
     * 文件系统上传分片文件-合并：公有和私有
     */
    public static String uploadFileSystemChunkCompose(Long transferId) {

        return RedissonUtil.doLock(BaseRedisKeyEnum.PRE_FILE_TRANSFER + ":" + transferId, () -> {

            BaseFileTransferDO baseFileTransferDO = baseFileTransferService.infoById(new NotNullId(transferId));

            if (baseFileTransferDO == null) {
                R.error("操作失败：传输不存在，请刷新再试", transferId);
            }

            BaseFileTransferStatusEnum status = baseFileTransferDO.getStatus();

            ArrayList<BaseFileTransferStatusEnum> allowStatusList =
                CollUtil.newArrayList(BaseFileTransferStatusEnum.TRANSFER_IN,
                    BaseFileTransferStatusEnum.TRANSFER_COMPLETE);

            if (!allowStatusList.contains(status)) {
                R.error("操作失败：传输状态不符合合并要求，请稍后再试", transferId);
            }

            boolean exists =
                baseFileService.lambdaQuery().eq(TempEntity::getId, baseFileTransferDO.getFileId()).exists();

            if (!exists) {
                R.error("操作失败：文件不存在，请重新上传", transferId);
            }

            Date date = new Date();

            // 更新为：传输完成
            updateTransferComplete(transferId, status, baseFileTransferDO, date);

            BaseFileStorageConfigurationDO baseFileStorageConfigurationDO =
                ChainWrappers.lambdaQueryChain(baseFileStorageConfigurationMapper)
                    .eq(TempEntity::getId, baseFileTransferDO.getStorageConfigurationId()).one();

            if (baseFileStorageConfigurationDO == null) {
                R.error("操作失败：存储配置不存在，请重新上传", baseFileTransferDO.getStorageConfigurationId());
            }

            // 获取：存储接口对象
            IBaseFileStorage iBaseFileStorage = getIbaseFileStorage(baseFileStorageConfigurationDO);

            // 查询出分片列表
            List<BaseFileTransferChunkDO> baseFileTransferChunkDOList =
                baseFileTransferChunkService.lambdaQuery().eq(BaseFileTransferChunkDO::getTransferId, transferId)
                    .orderByAsc(BaseFileTransferChunkDO::getChunkNum).list();

            // 获取：BaseFileComposeBO对象
            BaseFileComposeBO baseFileComposeBO =
                getBaseFileComposeBO(transferId, baseFileTransferDO, baseFileTransferChunkDOList);

            // 更新传输状态
            baseFileTransferService.lambdaUpdate().eq(BaseFileTransferDO::getId, transferId)
                .set(BaseFileTransferDO::getStatus, BaseFileTransferStatusEnum.COMPOSE_IN)
                .set(TempEntityNoIdSuper::getUpdateTime, date).update();

            MyTryUtil.tryCatch(() -> {

                // 开始合并
                iBaseFileStorage.compose(baseFileTransferDO.getBucketName(), baseFileComposeBO,
                    baseFileStorageConfigurationDO, baseFileTransferDO.getUri());

            }, e -> {

                // 更新传输状态
                baseFileTransferService.lambdaUpdate().eq(BaseFileTransferDO::getId, transferId)
                    .set(BaseFileTransferDO::getStatus, BaseFileTransferStatusEnum.TRANSFER_COMPLETE)
                    .set(TempEntityNoIdSuper::getUpdateTime, date).update();

                R.error("操作失败：合并异常", transferId);

            });

            // 验证：文件签名
            checkFileSign(transferId, iBaseFileStorage, baseFileTransferDO, baseFileStorageConfigurationDO, date);

            // 删除分片文件
            iBaseFileStorage.remove(baseFileTransferDO.getBucketName(),
                new HashSet<>(baseFileComposeBO.getObjectNameList()), baseFileStorageConfigurationDO);

            // 更新传输状态
            baseFileTransferService.lambdaUpdate().eq(BaseFileTransferDO::getId, transferId)
                .set(BaseFileTransferDO::getStatus, BaseFileTransferStatusEnum.COMPOSE_COMPLETE)
                .set(TempEntityNoIdSuper::getUpdateTime, date).update();

            // 更新文件状态
            baseFileService.lambdaUpdate().eq(TempEntity::getId, baseFileTransferDO.getFileId())
                .set(BaseFileDO::getUploadFlag, false).update();

            return TempBizCodeEnum.OK;

        });

    }

    /**
     * 验证：文件签名
     */
    public static void checkFileSign(Long transferId, IBaseFileStorage iBaseFileStorage,
        BaseFileTransferDO baseFileTransferDO, BaseFileStorageConfigurationDO baseFileStorageConfigurationDO,
        Date date) {

        // 验证文件签名
        InputStream inputStream =
            iBaseFileStorage.download(baseFileTransferDO.getBucketName(), baseFileTransferDO.getUri(),
                baseFileStorageConfigurationDO, null);

        byte[] bytes = IoUtil.readBytes(inputStream);

        String fileSignCheck = MD5.create().digestHex(bytes);

        IoUtil.close(inputStream);

        if (!fileSignCheck.equals(baseFileTransferDO.getFileSign())) {

            // 更新传输状态
            baseFileTransferService.lambdaUpdate().eq(BaseFileTransferDO::getId, transferId)
                .set(BaseFileTransferDO::getStatus, BaseFileTransferStatusEnum.TRANSFER_CANCEL)
                .set(TempEntityNoIdSuper::getUpdateTime, date).update();

            R.error("操作失败：文件签名不匹配，请重新上传", transferId);

        }

    }

    /**
     * 获取：BaseFileComposeBO对象
     */
    private static BaseFileComposeBO getBaseFileComposeBO(Long transferId, BaseFileTransferDO baseFileTransferDO,
        List<BaseFileTransferChunkDO> baseFileTransferChunkDOList) {

        BaseFileComposeBO baseFileComposeBO = new BaseFileComposeBO();

        baseFileComposeBO.setUploadId(transferId.toString());

        List<PartETag> partEtagList = new ArrayList<>();

        List<String> objectNameList = new ArrayList<>();

        boolean aliYunFlag = BaseFileStorageTypeEnum.ALI_YUN.getCode() == baseFileTransferDO.getStorageType();

        for (BaseFileTransferChunkDO item : baseFileTransferChunkDOList) {

            objectNameList.add(item.getUri());

            if (aliYunFlag) {

                partEtagList.add(JSONUtil.toBean(item.getRefData(), PartETag.class));

            }

        }

        baseFileComposeBO.setPartEtagList(partEtagList);

        baseFileComposeBO.setObjectNameList(objectNameList);

        return baseFileComposeBO;

    }

    /**
     * 更新为：传输完成
     */
    public static void updateTransferComplete(Long transferId, BaseFileTransferStatusEnum status,
        BaseFileTransferDO baseFileTransferDO, Date date) {

        if (!BaseFileTransferStatusEnum.TRANSFER_IN.equals(status)) {
            return;
        }

        Long count =
            baseFileTransferChunkService.lambdaQuery().eq(BaseFileTransferChunkDO::getTransferId, transferId).count();

        Integer chunkTotal = baseFileTransferDO.getChunkTotal();

        if (chunkTotal != count.intValue()) {
            R.error("操作失败：文件还未传输完成，请稍后再试", transferId);
        }

        baseFileTransferService.lambdaUpdate().eq(BaseFileTransferDO::getId, transferId)
            .set(BaseFileTransferDO::getStatus, BaseFileTransferStatusEnum.TRANSFER_COMPLETE)
            .set(TempEntityNoIdSuper::getUpdateTime, date).update();

    }

    /**
     * 检查权限
     *
     * @param checkType 1 读权限 2 写权限（默认）
     */
    public static void checkAuth(@Nullable Collection<Long> fileIdColl, @Nullable Long userId,
        @Nullable Integer checkType) {

        if (CollUtil.isEmpty(fileIdColl)) {
            return;
        }

        CollUtil.removeNull(fileIdColl);

        CollUtil.removeAny(fileIdColl, TempConstant.TOP_PID);

        if (CollUtil.isEmpty(fileIdColl)) {
            return;
        }

        if (userId == null) {

            userId = MyUserUtil.getCurrentUserId();

        }

        if (MyUserUtil.getCurrentUserAdminFlag()) {
            return;
        }

        LambdaQueryChainWrapper<BaseFileAuthDO> lambdaQueryChainWrapper =
            baseFileAuthService.lambdaQuery().eq(BaseFileAuthDO::getUserId, userId)
                .in(BaseFileAuthDO::getFileId, fileIdColl).eq(TempEntityNoId::getEnableFlag, true);

        checkType = MyEntityUtil.getNotNullInt(checkType, 2);

        if (checkType == 1) {

            lambdaQueryChainWrapper.eq(BaseFileAuthDO::getReadFlag, true);

        } else {

            lambdaQueryChainWrapper.eq(BaseFileAuthDO::getWriteFlag, true);

        }

        boolean exists = lambdaQueryChainWrapper.exists();

        if (!exists) {

            R.error(TempBizCodeEnum.INSUFFICIENT_PERMISSIONS);

        }

    }

    /**
     * 下载文件：私有
     */
    @SneakyThrows
    public static BaseFilePrivateDownloadVO privateDownload(BaseFilePrivateDownloadBO baseFilePrivateDownloadBO) {

        Long fileId = baseFilePrivateDownloadBO.getFileId();

        // 获取：BaseFileDO对象
        BaseFileDO baseFileDO = getPrivateDownloadBaseFile(fileId);

        if (BaseFileTypeEnum.FOLDER.equals(baseFileDO.getType())) {
            R.errorMsg("操作失败：暂不支持下载文件夹");
        }

        if (BooleanUtil.isFalse(baseFileDO.getPublicFlag())) { // 如果：不是公开下载

            // 检查权限
            BaseFileUtil.checkAuth(CollUtil.newArrayList(fileId), null, 1);

        }

        IBaseFileStorage iBaseFileStorage = BASE_FILE_STORAGE_MAP.get(baseFileDO.getStorageType());

        if (iBaseFileStorage == null) {

            R.error("操作失败：文件存储接口不存在", baseFileDO.getStorageType());

        }

        Long storageConfigurationId = baseFileDO.getStorageConfigurationId();

        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO =
            ChainWrappers.lambdaQueryChain(baseFileStorageConfigurationMapper)
                .eq(TempEntity::getId, storageConfigurationId).eq(TempEntityNoId::getEnableFlag, true).one();

        if (baseFileStorageConfigurationDO == null) {

            R.error("操作失败：文件存储配置不存在", storageConfigurationId);

        }

        // 设置：文件对象
        baseFilePrivateDownloadBO.setBaseFileDO(baseFileDO);

        // 执行下载
        InputStream inputStream =
            iBaseFileStorage.download(baseFileDO.getBucketName(), baseFileDO.getUri(), baseFileStorageConfigurationDO,
                baseFilePrivateDownloadBO);

        BaseFilePrivateDownloadVO baseFilePrivateDownloadVO = new BaseFilePrivateDownloadVO();

        baseFilePrivateDownloadVO.setInputStream(inputStream);

        baseFilePrivateDownloadVO.setFileName(baseFileDO.getShowFileName());

        // 处理：BaseFilePrivateDownloadVO对象
        handleBaseFilePrivateDownloadVO(baseFilePrivateDownloadBO, baseFileDO, baseFilePrivateDownloadVO);

        return baseFilePrivateDownloadVO;

    }

    /**
     * 处理：BaseFilePrivateDownloadVO对象
     */
    private static void handleBaseFilePrivateDownloadVO(BaseFilePrivateDownloadBO baseFilePrivateDownloadBO,
        BaseFileDO baseFileDO, BaseFilePrivateDownloadVO baseFilePrivateDownloadVO) {

        if (baseFilePrivateDownloadBO.getPre() != null && baseFilePrivateDownloadBO.getSuf() != null) {

            StrBuilder strBuilder = StrBuilder.create();

            strBuilder.append("bytes ").append(baseFilePrivateDownloadBO.getPre()).append("-")
                .append(baseFilePrivateDownloadBO.getSuf()).append("/").append(baseFileDO.getFileSize());

            baseFilePrivateDownloadVO.setContentRangeHeader(strBuilder.toString());

        } else if (baseFilePrivateDownloadBO.getPre() != null) {

            StrBuilder strBuilder = StrBuilder.create();

            strBuilder.append("bytes ").append(baseFilePrivateDownloadBO.getPre()).append("-")
                .append(baseFileDO.getFileSize()).append("/").append(baseFileDO.getFileSize());

            baseFilePrivateDownloadVO.setContentRangeHeader(strBuilder.toString());

        } else if (baseFilePrivateDownloadBO.getSuf() != null) {

            StrBuilder strBuilder = StrBuilder.create();

            strBuilder.append("bytes ").append(baseFileDO.getFileSize() - baseFilePrivateDownloadBO.getSuf())
                .append("-").append(baseFileDO.getFileSize()).append("/").append(baseFileDO.getFileSize());

            baseFilePrivateDownloadVO.setContentRangeHeader(strBuilder.toString());

        }

    }

    /**
     * 获取：BaseFileDO对象
     */
    @NotNull
    private static BaseFileDO getPrivateDownloadBaseFile(long fileId) {

        // 获取：查询对象
        BaseFileDO baseFileDO = getBaseFileBaseLambdaQuery().eq(TempEntity::getId, fileId).one();

        if (baseFileDO == null) {
            R.errorMsg("操作失败：文件不存在");
        }

        return baseFileDO;

    }

    /**
     * 获取：查询对象
     */
    private static LambdaQueryChainWrapper<BaseFileDO> getBaseFileBaseLambdaQuery() {

        return baseFileService.lambdaQuery().eq(TempEntityNoId::getEnableFlag, true);

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

            if (MyUserUtil.getCurrentUserAdminFlag()) {

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

        // 深度查找：folder下面的文件和文件夹
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
     * 深度查找：folder下面的文件和文件夹
     */
    private static void deepFindFolderAndFile(BaseFileDO folder, Set<Long> removeFileIdSet, List<BaseFileDO> fileList) {

        List<BaseFileDO> baseFileDoList = getRemoveLambdaQueryChainWrapper().like(BaseFileDO::getPidPathStr,
            SeparatorUtil.verticalLine(folder.getId())).list();

        if (CollUtil.isEmpty(baseFileDoList)) {
            return;
        }

        for (BaseFileDO item : baseFileDoList) {

            boolean contains = removeFileIdSet.contains(item.getId());

            if (!contains) {

                removeFileIdSet.add(item.getId());

            }

            if (BaseFileTypeEnum.FILE.equals(item.getType())) {

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
     * @param fileType    传递 byteArr时，需指定 fileType
     * @param delFileFlag 是否删除文件
     */
    @SneakyThrows
    public static Long getTempFileId(String remark, Long userId, @Nullable String fileType, byte @Nullable [] byteArr,
        @Nullable File fileTemp, boolean delFileFlag) {

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
            return BaseFileUtil.uploadCommonHandle(bo, fileType, null, null, null, bo.getUserId().toString(), true,
                true, null, null);

        } finally {

            if (delFileFlag) {

                FileUtil.del(file); // 删除文件

            }

        }

    }

    /**
     * 获取：临时文件的 fileUrl，byteArr 和 fileTemp 只会处理一个
     *
     * @param fileType 传递 byteArr时，需指定 fileType，传递 fileTemp时，不用指定 fileType
     */
    public static String getTempFileUrl(String remark, Long userId, @Nullable String fileType,
        byte @Nullable [] byteArr, @Nullable File fileTemp) {

        Long fileId = getTempFileId(remark, userId, fileType, byteArr, fileTemp, true);

        // 获取：文件链接
        Map<Long, String> publicUrlMap = BaseFileUtil.getPublicUrl(CollUtil.newHashSet(fileId));

        return publicUrlMap.get(fileId);

    }

}
