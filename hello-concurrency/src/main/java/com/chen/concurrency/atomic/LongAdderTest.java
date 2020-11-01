package com.chen.concurrency.atomic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;

/**
 * LongAdder的并发测试
 * <p>
 * @Author LeifChen
 * @Date 2019-06-13
 */
@Slf4j
public class LongAdderTest {

    public static void main(String[] args) {
        LongAdder counter = new LongAdder();
        ExecutorService service = Executors.newFixedThreadPool(20);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            service.submit(new Task(counter));
        }
        service.shutdown();
        while (!service.isTerminated()) {
        }
        long end = System.currentTimeMillis();
        log.debug("couter：{}", counter.sum());
        log.info("LongAdder耗时：{}ms", end - start);
    }

    private static class Task implements Runnable {
        private LongAdder counter;

        public Task(LongAdder counter) {
            this.counter = counter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                counter.increment();
            }
        }
    }
}
