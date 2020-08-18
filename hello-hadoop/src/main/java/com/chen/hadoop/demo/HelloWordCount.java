package com.chen.hadoop.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Map;
import java.util.Set;

/**
 * 词频统计
 * <p>
 * @Author LeifChen
 * @Date 2020-08-15
 */
@Slf4j
public class HelloWordCount {

    public static final String HADOOP_URI = "hdfs://hadoop100:9000";

    public static void main(String[] args) throws Exception {
        System.setProperty("hadoop.home.dir", "E:\\hadoop-2.6.0-cdh5.15.1");
        Configuration configuration = new Configuration();
        configuration.set("dfs.replication", "1");
        FileSystem fs = FileSystem.get(new URI(HADOOP_URI), configuration, "hadoop");

        Path input = new Path("/hdfsapi/test/hello.txt");
        RemoteIterator<LocatedFileStatus> iterator = fs.listFiles(input, false);

        Mapper mapper = new HelloWordCountMapper();
        Context context = new Context();

        while (iterator.hasNext()) {
            LocatedFileStatus file = iterator.next();
            FSDataInputStream in = fs.open(file.getPath());
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    mapper.map(line, context);
                }
            }
            in.close();
        }

        Map<Object, Object> contextMap = context.getCacheMap();

        Path output = new Path("/hdfsapi/output");
        Path resultPath = new Path("wordCount.out");
        try (FSDataOutputStream out = fs.create(new Path(output, resultPath))) {
            Set<Map.Entry<Object, Object>> entries = contextMap.entrySet();
            for (Map.Entry<Object, Object> entry : entries) {
                out.write((entry.getKey().toString() + "\t" + entry.getValue() + "\n").getBytes());
            }
        }
        fs.close();

        log.info("HDFS 统计词频结果存放： {}", output.toString() + "/" + resultPath);
    }
}
