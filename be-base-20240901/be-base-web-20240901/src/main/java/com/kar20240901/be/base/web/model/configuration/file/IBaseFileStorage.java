package com.kar20240901.be.base.web.model.configuration.file;

import com.kar20240901.be.base.web.model.bo.file.BaseFileComposeBO;
import com.kar20240901.be.base.web.model.bo.file.BaseFileUploadChunkBO;
import com.kar20240901.be.base.web.model.domain.file.BaseFileStorageConfigurationDO;
import com.kar20240901.be.base.web.model.interfaces.file.IBaseFileStorageType;
import com.kar20240901.be.base.web.model.vo.file.BaseFileUploadChunkVO;
import java.io.InputStream;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

public interface IBaseFileStorage {

    /**
     * 存放文件的服务器类型
     */
    IBaseFileStorageType getBaseFileStorageType();

    /**
     * 上传文件 备注：objectName 相同会被覆盖掉
     */
    void upload(String bucketName, String objectName, MultipartFile file,
        @NotNull BaseFileStorageConfigurationDO baseFileStorageConfigurationDO);

    /**
     * 分片上传文件 备注：objectName 相同会被覆盖掉
     */
    BaseFileUploadChunkVO uploadChunk(String bucketName, String objectName, MultipartFile file,
        @NotNull BaseFileStorageConfigurationDO baseFileStorageConfigurationDO,
        BaseFileUploadChunkBO baseFileUploadChunkBO);

    /**
     * 下载文件
     */
    InputStream download(String bucketName, String objectName,
        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO);

    /**
     * 复制文件
     */
    void copy(String sourceBucketName, String sourceObjectName, String toBucketName, String toObjectName,
        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO);

    /**
     * 批量删除文件
     */
    void remove(String bucketName, Set<String> objectNameSet,
        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO);

    /**
     * 获取：文件预览地址
     *
     * @param uri        例如：avatar/uuid.xxx，备注：不要在最前面加 /
     * @param bucketName 桶名，例如：be-public-bucket，备注：不要在最前面加 /
     */
    String getUrl(String uri, String bucketName, BaseFileStorageConfigurationDO baseFileStorageConfigurationDO);

    /**
     * 合并文件
     */
    void compose(String bucketName, BaseFileComposeBO baseFileComposeBO,
        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO, String newObjectName);

}
