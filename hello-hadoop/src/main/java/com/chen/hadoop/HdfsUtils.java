package com.chen.hadoop;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;

/**
 * HDFS工具类
 * <p>
 * @Author LeifChen
 * @Date 2020-08-09
 */
@Slf4j
public class HdfsUtils {

    public static final String HADOOP_URI = "hdfs://hadoop100:9000";

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.get(new URI(HADOOP_URI), configuration, "hadoop");

        Path path = new Path("/hdfsapi/test");
        boolean result = fileSystem.mkdirs(path);
        System.out.println(result);
    }
}
