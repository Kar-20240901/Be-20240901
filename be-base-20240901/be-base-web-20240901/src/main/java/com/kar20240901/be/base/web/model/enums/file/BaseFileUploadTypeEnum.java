package com.kar20240901.be.base.web.model.enums.file;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kar20240901.be.base.web.model.interfaces.file.IBaseFileUploadType;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.util.base.MyFileTypeUtil;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传：枚举类
 */
@AllArgsConstructor
@Getter
public enum BaseFileUploadTypeEnum implements IBaseFileUploadType {

    // 头像
    AVATAR(101, "avatar", CollUtil.newHashSet("jpeg", "png", "jpg"), 1024 * 1024 * 2, true), //

    // im群组头像
    IM_GROUP_AVATAR(102, "im-group-avatar", CollUtil.newHashSet("jpeg", "png", "jpg"), 1024 * 1024 * 2, true), //

    // excel
    EXCEL(201, "excel", CollUtil.newHashSet("xlsx"), 1024 * 1024 * 2, false), //

    // text
    TEXT(301, "text", CollUtil.newHashSet("txt"), 1024 * 1024 * 2, false), //

    // pdf
    PDF(401, "pdf", CollUtil.newHashSet("pdf"), 1024 * 1024 * 2, false), //

    // 临时文件-图片
    TEMP_IMAGE(501, "temp-image", AVATAR.getAcceptFileTypeSet(), 1024 * 1024 * 10, true), //

    // bpmn
    BPMN(601, "bpmn", CollUtil.newHashSet("xml", "bpmn"), 1024 * 1024 * 10, false), //

    // 临时文件
    TEMP_FILE(701, "temp-file", null, 1024 * 1024 * 10, true), //

    // 文件系统
    FILE_SYSTEM(801, "file-system", null, -1, false), //

    ;

    @EnumValue
    @JsonValue
    private final int code; // 类型编码
    private final String folderName; // 文件夹名，备注：前后不要加斜杠
    private final Set<String> acceptFileTypeSet; // 支持上传的文件类型（字母必须全小写），为 null则表示支持所有文件，为 空集合则表示不支持所有文件
    private final long maxFileSize; // 最大的文件大小 byte（包含），-1 则表示不限制大小 0 表示最大文件大小为0，则只能上传空文件
    private final boolean publicFlag; // 是否公开访问

    /**
     * 检查：文件类型，并返回文件类型（不含点），返回 null，则表示不支持此文件类型
     */
    @Override
    @SneakyThrows
    @Nullable
    public String checkFileType(MultipartFile file) {

        return BaseFileUploadTypeEnum.checkFileType(this, file);

    }

    /**
     * 检查：文件大小
     */
    @Override
    public void checkFileSize(MultipartFile file) {

        BaseFileUploadTypeEnum.checkFileSize(this, file);

    }

    /**
     * 检查：文件类型，并返回文件类型（不含点），返回 null，则表示不支持此文件类型
     */
    @SneakyThrows
    @Nullable
    public static String checkFileType(IBaseFileUploadType iBaseFileUploadType, MultipartFile file) {

        // 获取文件类型
        String fileType = MyFileTypeUtil.getType(file.getInputStream(), file.getOriginalFilename());

        return doCheckFileType(iBaseFileUploadType, fileType);

    }

    /**
     * 检查：文件类型，并返回文件类型（不含点），返回 null，则表示不支持此文件类型
     */
    @SneakyThrows
    @Nullable
    public static String doCheckFileType(IBaseFileUploadType iBaseFileUploadType, String fileType) {

        if (StrUtil.isBlank(fileType)) {
            return null;
        }

        // 获取：支持上传的文件类型
        Set<String> acceptFileTypeSet = iBaseFileUploadType.getAcceptFileTypeSet();

        if (acceptFileTypeSet == null) {
            return fileType;
        }

        if (acceptFileTypeSet.size() == 0) {
            return null;
        }

        if (acceptFileTypeSet.contains(fileType.toLowerCase())) { // 转换为，小写，进行匹配
            return fileType;
        }

        return null;

    }

    /**
     * 检查：文件大小
     */
    public static void checkFileSize(IBaseFileUploadType iBaseFileUploadType, MultipartFile file) {

        long maxFileSize = iBaseFileUploadType.getMaxFileSize();

        if (maxFileSize == -1) {
            return;
        }

        if (file.getSize() > maxFileSize) {
            R.errorMsg("操作失败：文件大小超过：【" + DataSizeUtil.format(maxFileSize) + "】，请重新选择");
        }

    }

    /**
     * 上传文件时的检查
     */
    @NotNull
    public static String uploadCheckWillError(MultipartFile file, IBaseFileUploadType iBaseFileUploadType) {

        Assert.notNull(file, "file 不能为空");
        Assert.notNull(iBaseFileUploadType, "uploadType 不能为空");

        iBaseFileUploadType.checkFileSize(file); // 检查：文件大小

        String originalFilename = file.getOriginalFilename();

        if (StrUtil.isBlank(originalFilename)) {
            R.errorMsg("操作失败：上传的文件名不能为空");
        }

        String fileType = iBaseFileUploadType.checkFileType(file);

        if (fileType == null) {
            R.errorMsg("操作失败：暂不支持此文件类型【" + originalFilename + "】，请正确上传文件");
        }

        return fileType;

    }

}
