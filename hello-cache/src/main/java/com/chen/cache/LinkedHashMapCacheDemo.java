package com.chen.cache;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LinkedHashMap Cache Demo
 * <p>
 * @Author LeifChen
 * @Date 2019-07-31
 */
@Slf4j
public class LinkedHashMapCacheDemo {

    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final int TIMES = 15;

    public static void main(String[] args) {
        // 最大缓存10个元素
        int cacheSize = 10;

        LinkedHashMap cache = new LinkedHashMap<String, String>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > cacheSize;
            }
        };

        for (int i = 0; i < TIMES; i++) {
            cache.put(KEY + i, VALUE + i);
        }
        log.info(cache.toString());
    }
}
