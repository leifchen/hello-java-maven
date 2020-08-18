package com.chen.hadoop.demo;

/**
 * 自定义词频Mapper实现类
 * <p>
 * @Author LeifChen
 * @Date 2020-08-15
 */
public class HelloWordCountMapper implements Mapper {

    @Override
    public void map(String line, Context context) {
        String[] words = line.split("\t");

        for (String word : words) {
            Object value = context.get(word);
            if (value == null) {
                context.put(word, 1);
            } else {
                int val = Integer.parseInt(value.toString());
                context.put(word, val + 1);
            }
        }
    }
}
