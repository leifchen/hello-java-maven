package com.chen.cache;

import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.xml.XmlConfiguration;

import java.net.URL;

/**
 * Ehcache Demo
 * <p>
 * @Author LeifChen
 * @Date 2019-07-30
 */
@Slf4j
public class EhcacheDemo {

    /**
     * 基于代码配置
     */
    private static void javaConfig() {
        // 创建 CacheManager 并初始化
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);

        // 创建缓存
        Cache<Long, String> myCache = cacheManager.createCache("myCache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                        ResourcePoolsBuilder.heap(100)).build());

        // 将数据存放到缓存
        myCache.put(1L, "Hello Ehcache!");

        // 获取缓存
        log.info("get cache: {}", myCache.get(1L));

        // 修改缓存
        myCache.replace(1L, "Java Config");
        log.info("update cache: {}", myCache.get(1L));

        // 删除缓存
        myCache.remove(1L);
        log.info("delete cache: {}", myCache.get(1L));

        // 关闭缓存
        cacheManager.close();
    }

    /**
     * 基于XML文件配置
     */
    private static void xmlConfig() {
        // 创建 CacheManager 并初始化
        URL myUrl = EhcacheDemo.class.getResource("/ehcache.xml");
        Configuration xmlConfig = new XmlConfiguration(myUrl);
        CacheManager cacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
        cacheManager.init();

        // 创建缓存
        Cache<Long, String> myCache = cacheManager.getCache("myCache", Long.class, String.class);

        // 将数据存放到缓存
        myCache.put(1L, "Hello Ehcache!");

        // 获取缓存
        log.info("get cache: {}", myCache.get(1L));

        // 修改缓存
        myCache.replace(1L, "XML Config");
        log.info("update cache: {}", myCache.get(1L));

        // 删除缓存
        myCache.remove(1L);
        log.info("delete cache: {}", myCache.get(1L));

        // 关闭缓存
        cacheManager.close();
    }

    public static void main(String[] args) {
        log.info("基于代码配置");
        javaConfig();

        log.info("基于XML文件配置");
        xmlConfig();
    }
}
