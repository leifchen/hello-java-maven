package com.chen.concurrency.locksupport;

import java.util.concurrent.locks.LockSupport;

/**
 * LockSupport测试
 * <p>
 * @Author LeifChen
 * @Date 2020-04-22
 */
public class LockSupportTest {

    public static void main(String[] args) throws InterruptedException {
        parkNanos();
        unpark();
        interrupt();
    }

    /**
     * 测试parkNanos
     */
    private static void parkNanos() {
        System.out.println("parkNanos begin...");
        LockSupport.parkNanos(1000);
        System.out.println("parkNanos end.");
    }

    /**
     * 测试unpark
     * @throws InterruptedException
     */
    private static void unpark() throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println("child thread park begin...");
            LockSupport.park();
            System.out.println("child thread park end");
        });
        // 启动子线程
        thread.start();
        // 主线程休眠1s
        Thread.sleep(1000);
        System.out.println("main thread unpark child thread");
        // 使得线程thread获持有许可证
        LockSupport.unpark(thread);
    }

    /**
     * 测试interrupt
     * @throws InterruptedException
     */
    private static void interrupt() throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println("child thread park begin...");
            // 调用park挂起自己，只有被中断才退出
            while (!Thread.currentThread().isInterrupted()) {
                LockSupport.park();
            }
            System.out.println("child thread park end.");
        });
        // 启动子线程
        thread.start();
        // 主线程休眠1s
        Thread.sleep(1000);
        System.out.println("main thread interrupt child thread");
        // 中断子线程
        thread.interrupt();
    }
}
