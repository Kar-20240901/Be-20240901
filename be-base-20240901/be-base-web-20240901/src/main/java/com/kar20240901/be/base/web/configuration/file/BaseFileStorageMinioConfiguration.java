package com.kar20240901.be.base.web.configuration.file;

import com.kar20240901.be.base.web.model.bo.file.BaseFileComposeBO;
import com.kar20240901.be.base.web.model.bo.file.BaseFileUploadChunkBO;
import com.kar20240901.be.base.web.model.configuration.file.IBaseFileStorage;
import com.kar20240901.be.base.web.model.domain.file.BaseFileStorageConfigurationDO;
import com.kar20240901.be.base.web.model.enums.file.BaseFileStorageTypeEnum;
import com.kar20240901.be.base.web.util.file.BaseFileMinioUtil;
import java.io.InputStream;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

/**
 * minio文件存储相关配置类
 */
@Configuration
public class BaseFileStorageMinioConfiguration implements IBaseFileStorage {

    @Override
    public BaseFileStorageTypeEnum getBaseFileStorageType() {
        return BaseFileStorageTypeEnum.MINIO;
    }

    @Override
    public void upload(String bucketName, String objectName, MultipartFile file,
        @NotNull BaseFileStorageConfigurationDO baseFileStorageConfigurationDO) {
        BaseFileMinioUtil.upload(bucketName, objectName, file, baseFileStorageConfigurationDO);
    }

    @Override
    public void uploadChunk(String bucketName, String objectName, MultipartFile file,
        @NotNull BaseFileStorageConfigurationDO baseFileStorageConfigurationDO,
        BaseFileUploadChunkBO baseFileUploadChunkBO) {
        BaseFileMinioUtil.upload(bucketName, objectName, file, baseFileStorageConfigurationDO);
    }

    @Override
    public InputStream download(String bucketName, String objectName,
        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO) {
        return BaseFileMinioUtil.download(bucketName, objectName, baseFileStorageConfigurationDO);
    }

    @Override
    public void copy(String sourceBucketName, String sourceObjectName, String toBucketName, String toObjectName,
        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO) {
        BaseFileMinioUtil.copy(sourceBucketName, sourceObjectName, toBucketName, toObjectName,
            baseFileStorageConfigurationDO);
    }

    @Override
    public void remove(String bucketName, Set<String> objectNameSet,
        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO) {
        BaseFileMinioUtil.remove(bucketName, objectNameSet, baseFileStorageConfigurationDO);
    }

    @Override
    public String getUrl(String uri, String bucketName, BaseFileStorageConfigurationDO baseFileStorageConfigurationDO) {
        return baseFileStorageConfigurationDO.getPublicDownloadEndpoint() + "/" + bucketName + "/" + uri;
    }

    @Override
    public void compose(String bucketName, BaseFileComposeBO baseFileComposeBO,
        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO, String newObjectName) {
        BaseFileMinioUtil.compose(bucketName, baseFileComposeBO, baseFileStorageConfigurationDO, newObjectName);
    }

}
