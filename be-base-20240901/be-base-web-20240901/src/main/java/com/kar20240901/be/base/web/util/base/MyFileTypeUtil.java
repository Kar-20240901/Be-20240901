package com.kar20240901.be.base.web.util.base;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.kar20240901.be.base.web.properties.file.FileTypeProperties;
import java.io.InputStream;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyFileTypeUtil {

    private static FileTypeProperties fileTypeProperties;

    @Resource
    public void setFileTypeProperties(FileTypeProperties fileTypeProperties) {
        MyFileTypeUtil.fileTypeProperties = fileTypeProperties;
    }

    /**
     * 获取文件类型（不含点）：读取文件头部字节，获取文件类型，如果没有匹配上，则返回 null
     */
    @Nullable
    public static String getType(InputStream inputStream, @Nullable String fileName) {

        String typeName = FileTypeUtil.getType(inputStream);

        IoUtil.close(inputStream); // 这里直接关闭流，因为这个流已经不完整了

        if (StrUtil.isBlank(fileName)) {
            return typeName;
        }

        if ("xls".equals(typeName)) {

            // xls、doc、msi的头一样，使用扩展名辅助判断
            final String extName = FileUtil.extName(fileName);

            if ("doc".equalsIgnoreCase(extName)) {
                typeName = "doc";
            } else if ("msi".equalsIgnoreCase(extName)) {
                typeName = "msi";
            }

        } else if ("zip".equals(typeName)) {

            // zip可能为docx、xlsx、pptx、jar、war、ofd等格式，扩展名辅助判断
            final String extName = FileUtil.extName(fileName);

            if ("docx".equalsIgnoreCase(extName)) {
                typeName = "docx";
            } else if ("xlsx".equalsIgnoreCase(extName)) {
                typeName = "xlsx";
            } else if ("pptx".equalsIgnoreCase(extName)) {
                typeName = "pptx";
            } else if ("jar".equalsIgnoreCase(extName)) {
                typeName = "jar";
            } else if ("war".equalsIgnoreCase(extName)) {
                typeName = "war";
            } else if ("ofd".equalsIgnoreCase(extName)) {
                typeName = "ofd";
            }

        } else if ("jar".equals(typeName)) {

            // wps编辑过的 .xlsx文件与 .jar的开头相同，通过扩展名判断
            final String extName = FileUtil.extName(fileName);

            if ("xlsx".equalsIgnoreCase(extName)) {
                typeName = "xlsx";
            } else if ("docx".equalsIgnoreCase(extName)) {
                typeName = "docx";
            }

        } else if (StrUtil.isBlank(typeName)) {

            // 使用扩展名
            final String extName = FileUtil.extName(fileName);

            if (fileTypeProperties.getAllowSet().contains(extName)) {
                return extName;
            }

        }

        return typeName;

    }

}
