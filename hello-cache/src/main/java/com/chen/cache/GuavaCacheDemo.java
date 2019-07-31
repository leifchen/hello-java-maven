package com.chen.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Guava Cache Demo
 * <p>
 * @Author LeifChen
 * @Date 2019-07-31
 */
@Slf4j
public class GuavaCacheDemo {

    private static final int TIMES = 20;

    /**
     * 使用CacheLoader创建
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void cacheLoader() throws ExecutionException, InterruptedException {
        LoadingCache<Integer, String> cache = CacheBuilder.newBuilder()
                // 设置并发级别为8，并发级别是指可以同时写缓存的线程数
                .concurrencyLevel(8)
                // 设置写缓存后8秒钟过期
                .expireAfterWrite(8, TimeUnit.SECONDS)
                // 设置缓存容器的初始容量为10
                .initialCapacity(10)
                // 设置缓存最大容量为100，超过100之后会按照LRU最近最少使用算法来移除缓存
                .maximumSize(100)
                // 设置要统计缓存的命中率
                .recordStats()
                // 设置缓存的移除通知
                .removalListener(notification -> log.info("{} was removed, cause is {}", notification.getKey(), notification.getCause()))
                .build(
                        // 指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
                        new CacheLoader<Integer, String>() {
                            @Override
                            public String load(Integer key) {
                                log.info("load data: {}", key);
                                return key + ":cache-value";
                            }
                        });

        for (int i = 0; i < TIMES; i++) {
            log.info(cache.get(1));
            TimeUnit.SECONDS.sleep(1);
        }

        log.info("cache stats: {}", cache.stats());
    }

    /**
     * 使用Callable创建
     * @throws ExecutionException
     */
    private static void callable() throws ExecutionException {
        Cache<String, String> cache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .build();

        String result = cache.get("test", () -> "test-value");
        log.info("return value: {}", result);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        cacheLoader();
        callable();
    }
}
