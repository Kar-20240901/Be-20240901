package com.kar20240901.be.base.web.util.file;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.kar20240901.be.base.web.model.bo.file.BaseFileComposeBO;
import com.kar20240901.be.base.web.model.bo.file.BaseFilePrivateDownloadBO;
import com.kar20240901.be.base.web.model.domain.file.BaseFileStorageConfigurationDO;
import com.kar20240901.be.base.web.model.vo.file.BaseFileUploadChunkVO;
import io.minio.ComposeObjectArgs;
import io.minio.ComposeSource;
import io.minio.CopyObjectArgs;
import io.minio.CopySource;
import io.minio.GetObjectArgs;
import io.minio.GetObjectArgs.Builder;
import io.minio.MinioClient;
import io.minio.ObjectWriteArgs;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectsArgs;
import io.minio.Result;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * minio文件工具类
 */
@Component
@Slf4j
public class BaseFileMinioUtil {

    /**
     * 上传文件 备注：objectName 相同会被覆盖掉
     */
    @SneakyThrows
    public static BaseFileUploadChunkVO upload(String bucketName, String objectName, MultipartFile file,
        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO) {

        InputStream inputStream = file.getInputStream();

        MinioClient minioClient = MinioClient.builder().endpoint(baseFileStorageConfigurationDO.getUploadEndpoint())
            .credentials(baseFileStorageConfigurationDO.getAccessKey(), baseFileStorageConfigurationDO.getSecretKey())
            .build();

        minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(objectName)
            .stream(inputStream, -1, ObjectWriteArgs.MAX_PART_SIZE).build());

        IoUtil.close(inputStream);

        return new BaseFileUploadChunkVO();

    }

    /**
     * 下载文件
     */
    @SneakyThrows
    public static InputStream download(String bucketName, String objectName,
        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO,
        @Nullable BaseFilePrivateDownloadBO baseFilePrivateDownloadBO) {

        MinioClient minioClient = MinioClient.builder().endpoint(baseFileStorageConfigurationDO.getUploadEndpoint())
            .credentials(baseFileStorageConfigurationDO.getAccessKey(), baseFileStorageConfigurationDO.getSecretKey())
            .build();

        Builder builder = GetObjectArgs.builder().bucket(bucketName).object(objectName);

        if (baseFilePrivateDownloadBO != null) {

            Long pre = baseFilePrivateDownloadBO.getPre();

            Long suf = baseFilePrivateDownloadBO.getSuf();

            if (pre != null && suf != null) {

                builder.offset(pre);

                builder.length(suf);

            } else if (pre != null) {

                builder.offset(pre);

            } else if (suf != null) {

                Long fileSize = baseFilePrivateDownloadBO.getBaseFileDO().getFileSize();

                builder.offset(fileSize - suf);
                builder.length(suf);

            }

        }

        return minioClient.getObject(builder.build());

    }

    /**
     * 复制文件
     */
    @SneakyThrows
    public static void copy(String sourceBucketName, String sourceObjectName, String toBucketName, String toObjectName,
        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO) {

        MinioClient minioClient = MinioClient.builder().endpoint(baseFileStorageConfigurationDO.getUploadEndpoint())
            .credentials(baseFileStorageConfigurationDO.getAccessKey(), baseFileStorageConfigurationDO.getSecretKey())
            .build();

        CopySource copySource = CopySource.builder().bucket(sourceBucketName).object(sourceObjectName).build();

        minioClient.copyObject(
            CopyObjectArgs.builder().source(copySource).bucket(toBucketName).object(toObjectName).build());

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

        MinioClient minioClient = MinioClient.builder().endpoint(baseFileStorageConfigurationDO.getUploadEndpoint())
            .credentials(baseFileStorageConfigurationDO.getAccessKey(), baseFileStorageConfigurationDO.getSecretKey())
            .build();

        List<DeleteObject> deleteObjectList = new ArrayList<>();

        for (String item : objectNameSet) {

            deleteObjectList.add(new DeleteObject(item));

        }

        Iterable<Result<DeleteError>> resultList =
            minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(bucketName).objects(deleteObjectList).build());

        List<JSONObject> errorList = new ArrayList<>();

        for (Result<DeleteError> item : resultList) {

            DeleteError deleteError = item.get();

            errorList.add(JSONUtil.createObj().set("n", deleteError.bucketName()).set("m", deleteError.message()));

        }

        log.info("minio批量删除文件错误，bucketName：{}，reason：{}", bucketName, errorList);

    }

    /**
     * 合并文件
     */
    @SneakyThrows
    public static void compose(String bucketName, BaseFileComposeBO baseFileComposeBO,
        BaseFileStorageConfigurationDO baseFileStorageConfigurationDO, String newObjectName) {

        List<String> objectNameList = baseFileComposeBO.getObjectNameList();

        if (CollUtil.isEmpty(objectNameList)) {
            return;
        }

        MinioClient minioClient = MinioClient.builder().endpoint(baseFileStorageConfigurationDO.getUploadEndpoint())
            .credentials(baseFileStorageConfigurationDO.getAccessKey(), baseFileStorageConfigurationDO.getSecretKey())
            .build();

        List<ComposeSource> composeSourceList = new ArrayList<>();

        for (String item : objectNameList) {

            composeSourceList.add(ComposeSource.builder().bucket(bucketName).object(item).build());

        }

        minioClient.composeObject(
            ComposeObjectArgs.builder().sources(composeSourceList).bucket(bucketName).object(newObjectName).build());

    }

}
