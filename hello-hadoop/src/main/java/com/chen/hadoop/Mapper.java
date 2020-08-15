package com.chen.hadoop;

/**
 * 自定义Mapper接口
 * <p>
 * @Author LeifChen
 * @Date 2020-08-15
 */
public interface Mapper {

    /**
     * 词频映射
     * @param line    读取到每一行数据
     * @param context 上下文/缓存
     */
    void map(String line, Context context);
}
