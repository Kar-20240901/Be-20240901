package com.kar20240901.be.base.web.util.file;

import cn.hutool.core.io.FileUtil;
import java.io.File;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileUtil {

    /**
     * 通过 file对象，获取：MultipartFile对象
     */
    public static MultipartFile getByFile(File file) {

        return new MyMultipartFile() {

            @Override
            public String getName() {
                return file.getName();
            }

            @Override
            public String getOriginalFilename() {
                return file.getName();
            }

            @Override
            public long getSize() {
                return FileUtil.size(file);
            }

            @Override
            public byte[] getBytes() {
                return FileUtil.readBytes(file);
            }

            @Override
            public InputStream getInputStream() {
                return FileUtil.getInputStream(file);
            }

        };

    }

    /**
     * 通过 name,size，获取：MultipartFile对象
     */
    public static MultipartFile getByFileNameAndFileSize(String name, long size) {

        return new MyMultipartFile() {

            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getOriginalFilename() {
                return name;
            }

            @Override
            public long getSize() {
                return size;
            }

        };

    }

}
