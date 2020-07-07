package com.chen.cache;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Caffeine Demo
 * <p>
 * @Author LeifChen
 * @Date 2020-06-29
 */
@Slf4j
public class CaffeineDemo {

    /**
     * 1.手动加载
     */
    private static void caffeinePutCase() throws InterruptedException {
        Cache<String, String> cache = Caffeine.newBuilder()
                .expireAfterWrite(3, TimeUnit.SECONDS)
                .maximumSize(3)
                .build();

        // 测试 put/get
        String key = "hello";
        cache.put(key, "world");
        assert "world".equals(cache.getIfPresent(key));

        // 测试不存在的key
        String key2 = "func";
        cache.get(key2, s -> s + "Test");
        assert "funcTest".equals(cache.getIfPresent(key2));

        // 测试已存在key值的value覆盖
        cache.put(key, "caffeine");
        assert "caffeine".equals(cache.getIfPresent(key));

        // 测试手动失效key
        cache.invalidate(key2);
        assert null == cache.getIfPresent(key2);

        // 测试key时间过期自动失效
        TimeUnit.SECONDS.sleep(5);
        assert null == cache.getIfPresent(key);

        log.info("手动加载 finished.");
    }

    /**
     * 2.同步加载
     */
    private static void caffeineSyncCase() {
        LoadingCache<String, Object> cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .maximumSize(10)
                .build(CaffeineDemo::buildDefaultValue);

        Object graph = cache.get("hello");
        assert "hello:value".equals(graph);

        log.info("同步加载 finished.");
    }

    /**
     * 3.异步加载
     */
    private static void caffeineAsyncCase() throws ExecutionException, InterruptedException {
        AsyncLoadingCache<String, Object> cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .maximumSize(10)
                .buildAsync(CaffeineDemo::buildDefaultValue);

        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        CompletableFuture<Map<String, Object>> futureMap = cache.getAll(list);
        Map<String, Object> map = futureMap.get();
        assert "A:value".equals(map.get("A"));
        assert "B:value".equals(map.get("B"));

        log.info("异步加载 finished.");
    }

    /**
     * 构造缓存默认值
     * @param key
     * @return
     */
    private static Object buildDefaultValue(Object key) {
        log.info("缓存不存在或过期，调用了getDefaultValue方法获取缓存key的值");
        return key + ":value";
    }

    public static void main(String[] args) throws Exception {
        caffeinePutCase();
        caffeineSyncCase();
        caffeineAsyncCase();
    }
}
