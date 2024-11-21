package com.kar20240901.be.base.web.util.file;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.kar20240901.be.base.web.model.domain.file.BaseFileStorageConfigurationDO;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 阿里云文件工具类
 */
@Component
public class BaseFileAliYunUtil {

    /**
     * 上传文件 备注：objectName 相同会被覆盖掉
     */
    @SneakyThrows
    public static void upload(String bucketName, String objectName, MultipartFile file,
        @NotNull BaseFileStorageConfigurationDO baseFileStorageConfigurationDO) {

        InputStream inputStream = file.getInputStream();

        OSS oss = new OSSClientBuilder().build(baseFileStorageConfigurationDO.getUploadEndpoint(),
            baseFileStorageConfigurationDO.getAccessKey(), baseFileStorageConfigurationDO.getSecretKey());

        oss.putObject(bucketName, objectName, inputStream);

        IoUtil.close(inputStream);

    }

    /**
     * 下载文件
     */
    @SneakyThrows
    @Nullable
    public static InputStream download(String bucketName, String objectName,
        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO) {

        OSS oss = new OSSClientBuilder().build(baseFileStorageConfigurationDO.getUploadEndpoint(),
            baseFileStorageConfigurationDO.getAccessKey(), baseFileStorageConfigurationDO.getSecretKey());

        return oss.getObject(bucketName, objectName).getObjectContent();

    }

    /**
     * 复制文件
     */
    public static void copy(String sourceBucketName, String sourceObjectName, String toBucketName, String toObjectName,
        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO) {

        OSS oss = new OSSClientBuilder().build(baseFileStorageConfigurationDO.getUploadEndpoint(),
            baseFileStorageConfigurationDO.getAccessKey(), baseFileStorageConfigurationDO.getSecretKey());

        oss.copyObject(sourceBucketName, sourceObjectName, toBucketName, toObjectName);

    }

    /**
     * 批量删除文件
     */
    @SneakyThrows
    public static void remove(String bucketName, Set<String> objectNameSet,
        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO) {

        if (CollUtil.isEmpty(objectNameSet)) {
            return;
        }

        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);

        deleteObjectsRequest.setKeys(new ArrayList<>(objectNameSet));

        OSS oss = new OSSClientBuilder().build(baseFileStorageConfigurationDO.getUploadEndpoint(),
            baseFileStorageConfigurationDO.getAccessKey(), baseFileStorageConfigurationDO.getSecretKey());

        oss.deleteObject(deleteObjectsRequest);

    }

}
