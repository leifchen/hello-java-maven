package com.chen.concurrency.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.FutureTask;

/**
 * FutureTask
 * <p>
 * @Author LeifChen
 * @Date 2019-06-18
 */
@Slf4j
public class FutureTaskTest {

    public static void main(String[] args) throws Exception {
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            log.info("do something in callable");
            Thread.sleep(3000);
            return "Done";
        });
        new Thread(futureTask).start();
        log.info("do something in main");
        Thread.sleep(1000);
        log.info("result:{}", futureTask.get());
    }
}
