package com.chen.concurrency.future;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Future
 * <p>
 * @Author LeifChen
 * @Date 2019-06-18
 */
@Slf4j
public class FutureTest {

    static class MyCallable implements Callable<String> {
        @Override
        public String call() throws Exception {
            log.info("do something in callable");
            Thread.sleep(3000);
            return "Done";
        }
    }

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> future = executorService.submit(new MyCallable());
        executorService.shutdown();
        log.info("do something in main");
        Thread.sleep(1000);
        log.info("isCancelled:{}", future.isCancelled());
        log.info("result:{}", future.get());
        log.info("isDone:{}", future.isDone());
    }
}
