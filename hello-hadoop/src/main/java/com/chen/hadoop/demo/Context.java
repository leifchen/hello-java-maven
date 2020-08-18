package com.chen.hadoop.demo;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 上下文
 * <p>
 * @Author LeifChen
 * @Date 2020-08-15
 */
public class Context {

    @Getter
    private Map<Object, Object> cacheMap = new HashMap<>();

    /**
     * 写数据到缓存中
     * @param key   单词
     * @param value 次数
     */
    public void put(Object key, Object value) {
        cacheMap.put(key, value);
    }

    /**
     * 从缓存中读取值
     * @param key 单词
     * @return 单词对应的词频
     */
    public Object get(Object key) {
        return cacheMap.get(key);
    }
}
