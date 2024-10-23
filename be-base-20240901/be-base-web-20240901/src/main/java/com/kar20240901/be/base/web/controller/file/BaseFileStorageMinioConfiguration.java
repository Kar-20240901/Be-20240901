package com.kar20240901.be.base.web.controller.file;

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
    public InputStream download(String bucketName, String objectName,
        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO) {
        return BaseFileMinioUtil.download(bucketName, objectName, baseFileStorageConfigurationDO);
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

}
