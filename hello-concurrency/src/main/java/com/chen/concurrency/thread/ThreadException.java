package com.chen.concurrency.thread;

/**
 * 线程的异常捕获
 * <p>
 * @Author LeifChen
 * @Date 2020-09-17
 */
public class ThreadException {

    public static void main(String[] args) {
        Runnable task = () -> {
            throw new RuntimeException();
        };

        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler("自定义线程异常捕获器"));
        new Thread(task,"thread-1").start();
        new Thread(task,"thread-2").start();
        new Thread(task,"thread-3").start();
    }
}
