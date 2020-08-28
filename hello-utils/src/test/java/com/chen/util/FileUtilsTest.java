package com.chen.util;

import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * 文件工具类测试
 * <p>
 * @Author LeifChen
 * @Date 2020-08-28
 */
public class FileUtilsTest {

    private static final String AD = "XXX_AD";
    private static final String PATH = "D:\\学习\\Vedio\\Elasticsearch核心技术与实战\\第7章 数据建模";

    @Test
    public void rename() {
        // 文件或其文件夹所在路径
        List<File> files = FileUtils.scanFile(PATH);
        for (File file : files) {
            boolean res = FileUtils.rename(file, AD);
            assertTrue(res);
        }
    }
}