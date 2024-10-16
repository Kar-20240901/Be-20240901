package com.kar20240901.be.base.web.model.configuration.file;

import com.kar20240901.be.base.web.model.domain.file.SysFileStorageConfigurationDO;
import com.kar20240901.be.base.web.model.interfaces.file.ISysFileStorageType;
import java.io.InputStream;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

public interface ISysFileStorage {

    /**
     * 存放文件的服务器类型
     */
    ISysFileStorageType getSysFileStorageType();

    /**
     * 上传文件 备注：objectName 相同会被覆盖掉
     */
    void upload(String bucketName, String objectName, MultipartFile file,
        @NotNull SysFileStorageConfigurationDO sysFileStorageConfigurationDO);

    /**
     * 下载文件
     */
    InputStream download(String bucketName, String objectName,
        SysFileStorageConfigurationDO sysFileStorageConfigurationDO);

    /**
     * 批量删除文件
     */
    void remove(String bucketName, Set<String> objectNameSet,
        SysFileStorageConfigurationDO sysFileStorageConfigurationDO);

    /**
     * 获取：文件预览地址
     *
     * @param uri        例如：avatar/uuid.xxx，备注：不要在最前面加 /
     * @param bucketName 桶名，例如：be-public-bucket，备注：不要在最前面加 /
     */
    String getUrl(String uri, String bucketName, SysFileStorageConfigurationDO sysFileStorageConfigurationDO);

}
