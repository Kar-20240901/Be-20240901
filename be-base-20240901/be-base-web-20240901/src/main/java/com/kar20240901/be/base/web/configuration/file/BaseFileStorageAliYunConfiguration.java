package com.kar20240901.be.base.web.configuration.file;

import com.kar20240901.be.base.web.model.configuration.file.IBaseFileStorage;
import com.kar20240901.be.base.web.model.domain.file.BaseFileStorageConfigurationDO;
import com.kar20240901.be.base.web.model.enums.file.BaseFileStorageTypeEnum;
import com.kar20240901.be.base.web.util.file.BaseFileAliYunUtil;
import java.io.InputStream;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

/**
 * 阿里云 oss文件存储相关配置类
 */
@Configuration
public class BaseFileStorageAliYunConfiguration implements IBaseFileStorage {

    @Override
    public BaseFileStorageTypeEnum getBaseFileStorageType() {
        return BaseFileStorageTypeEnum.ALI_YUN;
    }

    @Override
    public void upload(String bucketName, String objectName, MultipartFile file,
        @NotNull BaseFileStorageConfigurationDO baseFileStorageConfigurationDO) {
        BaseFileAliYunUtil.upload(bucketName, objectName, file, baseFileStorageConfigurationDO);
    }

    @Override
    public InputStream download(String bucketName, String objectName,
        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO) {
        return BaseFileAliYunUtil.download(bucketName, objectName, baseFileStorageConfigurationDO);
    }

    @Override
    public void copy(String sourceBucketName, String sourceObjectName, String toBucketName, String toObjectName,
        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO) {
        BaseFileAliYunUtil.copy(sourceBucketName, sourceObjectName, toBucketName, toObjectName,
            baseFileStorageConfigurationDO);
    }

    @Override
    public void remove(String bucketName, Set<String> objectNameSet,
        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO) {
        BaseFileAliYunUtil.remove(bucketName, objectNameSet, baseFileStorageConfigurationDO);
    }

    @Override
    public String getUrl(String uri, String bucketName, BaseFileStorageConfigurationDO baseFileStorageConfigurationDO) {
        return baseFileStorageConfigurationDO.getPublicDownloadEndpoint() + "/" + uri;
    }

}
