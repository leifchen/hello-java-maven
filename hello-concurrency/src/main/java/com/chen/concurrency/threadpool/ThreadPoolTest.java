package com.chen.concurrency.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池
 * <p>
 * @Author LeifChen
 * @Date 2019-06-19
 */
@Slf4j
public class ThreadPoolTest {

    private static final int TIMES = 10;

    public static void newSingleThreadExecutor(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        for (int i = 0; i < TIMES; i++) {
            final int index = i;
            executorService.execute(() -> log.info("newSingleThreadExecutor - task:{}", index));
        }

        executorService.shutdown();
    }

    public static void newFixedThreadPool(){
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        for (int i = 0; i < TIMES; i++) {
            final int index = i;
            executorService.execute(() -> log.info("newFixedThreadPool - task:{}", index));
        }

        executorService.shutdown();
    }

    public static void newScheduledThreadPool(){
        ExecutorService executorService = Executors.newScheduledThreadPool(3);

        for (int i = 0; i < TIMES; i++) {
            final int index = i;
            executorService.execute(() -> log.info("newScheduledThreadPool - task:{}", index));
        }

        executorService.shutdown();
    }

    public static void newCachedThreadPool(){
        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < TIMES; i++) {
            final int index = i;
            executorService.execute(() -> log.info("newCachedThreadPool - task:{}", index));
        }

        executorService.shutdown();
    }

    public static void main(String[] args) {
        newSingleThreadExecutor();
        newFixedThreadPool();
        newScheduledThreadPool();
        newCachedThreadPool();
    }
}
