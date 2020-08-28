package com.chen.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 * <p>
 * @Author LeifChen
 * @Date 2020-08-28
 */
public class FileUtils {

    private FileUtils() {
    }

    /**
     * 查找指定路径下的所有文件：递归
     * @param path 文件路径
     * @return 返回路径下的所有文件
     */
    public static List<File> scanFile(String path) {
        List<File> files = new ArrayList<>();

        File rootDir = new File(path);
        if (rootDir.isDirectory()) {
            String[] fileList = rootDir.list();
            assert fileList != null;
            for (String file : fileList) {
                files.add(new File(rootDir.getAbsolutePath(), file));
            }
        }

        return files;
    }

    /**
     * 文件重命名
     * @param file 文件
     * @return 返回是否重命名成功
     */
    public static boolean rename(File file, String match) {
        String srcFileName = file.getName();
        if (srcFileName.contains(match)) {
            String dirPath = file.getAbsolutePath();
            String dstFileName = dirPath.replace(match, "");
            return file.renameTo(new File(dstFileName));
        }

        return true;
    }
}
