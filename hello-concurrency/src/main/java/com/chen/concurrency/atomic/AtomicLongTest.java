package com.chen.concurrency.atomic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * AtomicLong的并发测试
 * <p>
 * @Author LeifChen
 * @Date 2019-06-13
 */
@Slf4j
public class AtomicLongTest {

    public static void main(String[] args) {
        AtomicLong counter = new AtomicLong(0);
        ExecutorService service = Executors.newFixedThreadPool(20);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            service.submit(new Task(counter));
        }
        service.shutdown();
        while (!service.isTerminated()) {
        }
        long end = System.currentTimeMillis();
        log.debug("couter：{}", counter.get());
        log.info("AtomicLong耗时：{}ms", end - start);
    }

    private static class Task implements Runnable {
        private AtomicLong counter;

        public Task(AtomicLong counter) {
            this.counter = counter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                counter.incrementAndGet();
            }
        }
    }
}
