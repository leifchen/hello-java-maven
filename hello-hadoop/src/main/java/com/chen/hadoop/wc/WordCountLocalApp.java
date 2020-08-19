package com.chen.hadoop.wc;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 词频统计 - 本地运行
 * <p>
 * @Author LeifChen
 * @Date 2020-08-18
 */
@Slf4j
public class WordCountLocalApp {

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

        Job job = Job.getInstance(configuration);

        // 设置Job对应的主类
        job.setJarByClass(WordCountLocalApp.class);

        // 设置Job对应的Mapper和Reducer处理类
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        // 设置Job对应的Combiner处理类
        job.setCombinerClass(WordCountReducer.class);

        // 设置Job对应的Mapper输出key和value的类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        Path inputPath = new Path("E:\\input");
        Path outputPath = new Path("E:\\output");

        // 设置Job对应的作业输入和输出路径
        FileInputFormat.setInputPaths(job, inputPath);
        FileOutputFormat.setOutputPath(job, outputPath);

        boolean result = job.waitForCompletion(true);

        System.exit(result ? 0 : -1);
    }
}
