package com.chen.concurrency.thread;

/**
 * 线程的中断
 * <p>
 * 最佳实践1：优先选择在方法签名中抛出 Interrupted 异常
 * @Author LeifChen
 * @Date 2020-09-11
 */
public class ThreadInterruptTest1 {

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> {
            try {
                while (true) {
                    System.out.println("go");
                    throwInMethod();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("save log...");
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
        Thread.sleep(500);
        thread.interrupt();
    }

    private static void throwInMethod() throws InterruptedException {
        Thread.sleep(1000);
    }
}
