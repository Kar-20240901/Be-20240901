package com.kar20240901.be.base.web.util.file;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CompleteMultipartUploadRequest;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.PartETag;
import com.aliyun.oss.model.UploadPartRequest;
import com.aliyun.oss.model.UploadPartResult;
import com.kar20240901.be.base.web.model.bo.file.BaseFileComposeBO;
import com.kar20240901.be.base.web.model.bo.file.BaseFileUploadChunkBO;
import com.kar20240901.be.base.web.model.domain.file.BaseFileStorageConfigurationDO;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.SneakyThrows;
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
        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO) {

        InputStream inputStream = file.getInputStream();

        OSS oss = new OSSClientBuilder().build(baseFileStorageConfigurationDO.getUploadEndpoint(),
            baseFileStorageConfigurationDO.getAccessKey(), baseFileStorageConfigurationDO.getSecretKey());

        oss.putObject(bucketName, objectName, inputStream);

        IoUtil.close(inputStream);

    }

    /**
     * 分片上传文件 备注：objectName 相同会被覆盖掉
     */
    @SneakyThrows
    public static PartETag uploadChunk(String bucketName, String objectName, MultipartFile file,
        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO, BaseFileUploadChunkBO baseFileUploadChunkBO) {

        InputStream inputStream = file.getInputStream();

        OSS oss = new OSSClientBuilder().build(baseFileStorageConfigurationDO.getUploadEndpoint(),
            baseFileStorageConfigurationDO.getAccessKey(), baseFileStorageConfigurationDO.getSecretKey());

        UploadPartRequest uploadPartRequest = new UploadPartRequest();

        uploadPartRequest.setBucketName(bucketName);

        uploadPartRequest.setKey(objectName);

        uploadPartRequest.setUploadId(baseFileUploadChunkBO.getUploadId());

        uploadPartRequest.setPartNumber(baseFileUploadChunkBO.getPartNumber());

        uploadPartRequest.setInputStream(inputStream);

        UploadPartResult uploadPartResult = oss.uploadPart(uploadPartRequest);

        return uploadPartResult.getPartETag();

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

        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, objectName);

        return oss.getObject(getObjectRequest).getObjectContent();

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

    /**
     * 合并文件
     */
    @SneakyThrows
    public static void compose(String bucketName, BaseFileComposeBO baseFileComposeBO,
        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO, String newObjectName) {

        List<PartETag> partEtagList = baseFileComposeBO.getPartEtagList();

        if (CollUtil.isEmpty(partEtagList)) {
            return;
        }

        String uploadId = baseFileComposeBO.getUploadId();

        if (StrUtil.isBlank(uploadId)) {
            return;
        }

        OSS oss = new OSSClientBuilder().build(baseFileStorageConfigurationDO.getUploadEndpoint(),
            baseFileStorageConfigurationDO.getAccessKey(), baseFileStorageConfigurationDO.getSecretKey());

        CompleteMultipartUploadRequest completeMultipartUploadRequest =
            new CompleteMultipartUploadRequest(bucketName, newObjectName, uploadId, partEtagList);

        oss.completeMultipartUpload(completeMultipartUploadRequest);

    }

}
