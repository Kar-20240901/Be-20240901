package com.kar20240901.be.base.web.util.base;

import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;
import com.kar20240901.be.base.web.model.interfaces.base.IBizCode;
import com.kar20240901.be.base.web.model.vo.base.R;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

public class ResponseUtil {

    @SneakyThrows
    public static void out(HttpServletResponse response, IBizCode iBizCode) {

        response.setContentType("application/json;charset=utf-8");

        response.setStatus(HttpServletResponse.SC_OK);

        ServletOutputStream servletOutputStream = response.getOutputStream();

        R<?> r = R.errorOrigin(iBizCode);

        servletOutputStream.write(JSONUtil.toJsonStr(r).getBytes()); // json字符串，输出给前端
        servletOutputStream.flush();
        servletOutputStream.close();

    }

    /**
     * @param convertFlag 是否转换
     */
    @SneakyThrows
    public static void out(HttpServletResponse response, String msg, boolean convertFlag) {

        out(response, msg, HttpServletResponse.SC_OK, convertFlag);

    }

    @SneakyThrows
    public static void out(HttpServletResponse response, String msg, int status, boolean convertFlag) {

        response.setContentType("application/json;charset=utf-8");

        ServletOutputStream servletOutputStream = response.getOutputStream();

        response.setStatus(status);

        String writeMsg = msg;

        if (convertFlag) {

            R<?> r = R.errorMsgOrigin(msg);

            writeMsg = JSONUtil.toJsonStr(r);

        }

        servletOutputStream.write(writeMsg.getBytes()); // json字符串，输出给前端
        servletOutputStream.flush();
        servletOutputStream.close();

    }

    /**
     * 获取 excel下载的 OutputStream
     */
    @SneakyThrows
    public static OutputStream getExcelOutputStream(HttpServletResponse response, String fileName) {

        // 备注：.xls是：application/vnd.ms-excel
        // 备注：.xlsx是：application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");

        response.setHeader("Content-Disposition",
            "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");

        return response.getOutputStream();

    }

    /**
     * 获取 word下载的 OutputStream
     */
    @SneakyThrows
    public static OutputStream getWordOutputStream(HttpServletResponse response, String fileName) {

        // 备注：.doc是：application/msword
        // 备注：.docx是：application/vnd.openxmlformats-officedocument.wordprocessingml.document
        response.setContentType(
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document;charset=utf-8");

        response.setHeader("Content-Disposition",
            "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".docx");

        return response.getOutputStream();

    }

    /**
     * 获取 文件下载的 OutputStream
     */
    @SneakyThrows
    public static OutputStream getOutputStream(HttpServletResponse response, String fileName) {

        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

        return response.getOutputStream();

    }

    /**
     * 把流复制给 response，然后返回给调用者
     */
    @SneakyThrows
    public static void flush(HttpServletResponse response, InputStream inputStream) {

        ServletOutputStream outputStream = response.getOutputStream();

        IoUtil.copy(inputStream, outputStream);

        outputStream.flush();

        IoUtil.close(inputStream);

        IoUtil.close(outputStream);

    }

}
