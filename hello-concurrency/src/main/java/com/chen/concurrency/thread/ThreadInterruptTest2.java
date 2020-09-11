package com.chen.concurrency.thread;

/**
 * 线程的中断
 * <p>
 * 最佳实践2：捕获异常后在 catch 语句中调用 Thread.currentThread().interrupt() 恢复设置中断标志
 * @Author LeifChen
 * @Date 2020-09-11
 */
public class ThreadInterruptTest2 {

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> {
            while(true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Interrupted.");
                    break;
                }
                System.out.println("go");
                reInterrupt();
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
        Thread.sleep(500);
        thread.interrupt();
    }

    private static void reInterrupt() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}
