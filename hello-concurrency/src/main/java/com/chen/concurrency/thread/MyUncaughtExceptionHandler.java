package com.chen.concurrency.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义 UncaughtExceptionHandler 异常捕获器
 * <p>
 * @Author LeifChen
 * @Date 2020-09-17
 */
@Slf4j
public class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private String name;

    public MyUncaughtExceptionHandler(String name) {
        this.name = name;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.warn(t.getName() + "线程发生异常", e);
        log.error(name + "捕获了异常" + t.getName() + "异常信息" + e);
    }
}
