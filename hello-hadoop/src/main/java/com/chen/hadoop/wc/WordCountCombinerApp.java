package com.chen.hadoop.wc;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.net.URI;

/**
 * 词频统计
 * <p>
 * @Author LeifChen
 * @Date 2020-08-18
 */
@Slf4j
public class WordCountCombinerApp {

    private static final String HADOOP_URI = "hdfs://hadoop100:9000";

    static {
        try {
            // 建议采用绝对地址，bin目录下的hadoop.dll文件路径
            System.load("E:\\hadoop-2.6.0-cdh5.15.1\\bin\\hadoop.dll");
        } catch (UnsatisfiedLinkError e) {
            log.error("Native code library failed to load.\n", e);
            System.exit(-1);
        }
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("hadoop.home.dir", "E:\\hadoop-2.6.0-cdh5.15.1");
        System.setProperty("HADOOP_USER_NAME", "hadoop");

        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", HADOOP_URI);

        Job job = Job.getInstance(configuration);

        // 设置Job对应的主类
        job.setJarByClass(WordCountCombinerApp.class);

        // 设置Job对应的Mapper和Reducer处理类
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        // 设置Job对应的Combiner处理类
        job.setCombinerClass(WordCountReducer.class);

        // 设置Job对应的Mapper输出key和value的类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        Path inputPath = new Path("/wordcount/input");
        FileSystem fs = FileSystem.get(new URI(HADOOP_URI), configuration, "hadoop");
        Path outputPath = new Path("/wordcount/output");
        if (fs.exists(outputPath)) {
            fs.delete(outputPath, true);
        }

        // 设置Job对应的作业输入和输出路径
        FileInputFormat.setInputPaths(job, inputPath);
        FileOutputFormat.setOutputPath(job, outputPath);

        boolean result = job.waitForCompletion(true);

        System.exit(result ? 0 : -1);
    }
}
