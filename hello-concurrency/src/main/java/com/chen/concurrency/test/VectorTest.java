package com.chen.concurrency.test;

import com.chen.concurrency.annotation.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Vector的并发测试
 * <p>
 * @Author LeifChen
 * @Date 2019-06-12
 */
@Slf4j
@ThreadSafe
public class VectorTest {
    /**
     * 请求总数
     */
    private static int CLIENT_TOTAL = 5000;
    /**
     * 并发执行线程数
     */
    private static int THREAD_TOTAL = 200;
    /**
     * 计数器
     */
    private static List<Integer> list = new Vector<>();

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(THREAD_TOTAL);
        final CountDownLatch countDownLatch = new CountDownLatch(CLIENT_TOTAL);
        for (int i = 0; i < CLIENT_TOTAL; i++) {
            final int count = i;
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    add(count);
                    semaphore.release();
                } catch (InterruptedException e) {
                    log.error("exception", e);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        log.info("test:{}", list.size());
    }

    private static void add(final int count) {
        list.add(count);
    }
}
