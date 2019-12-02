package com.chen.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * FTP工具类测试
 * <p>
 * @Author LeifChen
 * @Date 2019-11-27
 */
@Slf4j
public class FtpUtilsTest {

    private static FtpUtils ftpUtils;

    static {
        ftpUtils = FtpUtils.createFtpClient("192.168.33.101", "ftp", "123456");
        try {
            ftpUtils.connect();
        } catch (IOException e) {
            log.error("初始化连接FTP服务失败！");
        }
    }

    @Test
    public void createDir() throws Exception {
        ftpUtils.createDir("/home/tmp/2019/11/27");
    }

    @Test
    public void upload() throws Exception {
        ftpUtils.upload("E:\\", "/home/tmp/2019/11/27", "test.txt");
    }

    @Test
    public void download() throws Exception {
        ftpUtils.download("E:\\", "/home/tmp/2019/11/27", "test.txt");
    }

    @Test
    public void rename() throws Exception {
        ftpUtils.rename("/home/tmp/2019/11/27", "test.txt", "/home/tmp/20191127", "new.txt");
    }

    @Test
    public void delete() throws Exception {
        ftpUtils.delete("/home/tmp/20191127", "new.txt");
    }

    @Test
    public void listFileNames() throws Exception {
        List<String> fileNames = ftpUtils.listFileNames("/home/tmp/2019/11/27");
        for (String fileName : fileNames) {
            log.info(fileName);
        }
    }
}