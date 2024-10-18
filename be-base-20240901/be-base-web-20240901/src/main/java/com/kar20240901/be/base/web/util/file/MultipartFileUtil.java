package com.kar20240901.be.base.web.util.file;

import cn.hutool.core.io.FileUtil;
import java.io.File;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileUtil {

    public static MultipartFile getByFile(File file) {

        return new MultipartFile() {

            @Override
            public String getName() {
                return file.getName();
            }

            @Override
            public String getOriginalFilename() {
                return file.getName();
            }

            @Override
            public String getContentType() {
                return "application/octet-stream";
            }

            @Override
            public boolean isEmpty() {
                return false;
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

            @Override
            public void transferTo(File dest) throws IllegalStateException {

            }

        };

    }

}
