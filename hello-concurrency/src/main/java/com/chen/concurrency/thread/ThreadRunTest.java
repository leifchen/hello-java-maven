package com.chen.concurrency.thread;

/**
 * 线程的启动
 * <p>
 * @Author LeifChen
 * @Date 2020-09-02
 */
public class ThreadRunTest {

    public static void main(String[] args) {
        // 线程的启动方式1：run
        Runnable runnable = () -> {
            System.out.println(Thread.currentThread().getName());
        };
        runnable.run();

        // 线程的启动方式2：start
        new Thread(runnable).start();
    }
}
