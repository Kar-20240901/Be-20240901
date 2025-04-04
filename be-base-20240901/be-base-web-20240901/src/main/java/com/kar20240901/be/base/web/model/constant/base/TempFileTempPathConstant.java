package com.kar20240901.be.base.web.model.constant.base;

/**
 * 文件临时路径枚举类
 */
public interface TempFileTempPathConstant {

    String PRE_TEMP_PATH = "/home/myTempFile";

    // 微信上传文件时的，临时路径
    String WX_MEDIA_UPLOAD_TEMP_PATH = PRE_TEMP_PATH + "/wxMediaUpload/";

    // ffmpeg上传文件时的，临时路径
    String FFMPEG_TEMP_PATH = PRE_TEMP_PATH + "/ffmpegUpload/";

    // 临时文件上传时的，临时路径
    String FILE_TEMP_PATH = PRE_TEMP_PATH + "/fileUpload/";

}
