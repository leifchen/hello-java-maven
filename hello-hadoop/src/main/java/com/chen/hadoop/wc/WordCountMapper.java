package com.chen.hadoop.wc;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * 词频统计Mapper
 * <p>
 * @Author LeifChen
 * @Date 2020-08-18
 */
public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] words = value.toString().split("\t");
        for (String word : words) {
            context.write(new Text(word.toLowerCase()), new IntWritable(1));
        }
    }
}
