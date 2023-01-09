package com.akehcloud.iecube.util;

import com.akehcloud.exception.runtime.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.function.Consumer;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 下载工具类
 *
 * @author panghaoyue
 */
public class DownloadUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadUtils.class);

    private DownloadUtils() {
    }

    public static void httpDownload(File file, String filename, HttpServletResponse response) {
        try {
            httpDownload(new FileInputStream(file), filename, response);
        } catch (FileNotFoundException e) {
            LOGGER.error("文件不存在", e);
            throw new SystemException("系统错误");
        }
    }

    public static void httpDownload(File file, HttpServletResponse response) {
        httpDownload(file, null, response);
    }

    public static void httpDownload(InputStream inputStream, String filename, HttpServletResponse response) {
        httpDownload(filename, response, out -> {
            try {
                FileCopyUtils.copy(inputStream, out);
            } catch (IOException e) {
                LOGGER.error("IO异常", e);
                throw new SystemException("系统错误");
            }
        });
    }

    public static void httpDownload(String filename, HttpServletResponse response, Consumer<ServletOutputStream> consumer) {
        if (StringUtils.hasText(filename)) {
            try {
                filename = URLEncoder.encode(filename, UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("不支持的编码类型", e);
                throw new RuntimeException("不支持的编码类型");
            }
            response.setHeader("Content-disposition", "attachment; filename*=UTF-8''" + filename);
        }
        try {
            consumer.accept(response.getOutputStream());
        } catch (IOException e) {
            LOGGER.error("IO异常", e);
            throw new SystemException("系统错误");
        }
    }

}
