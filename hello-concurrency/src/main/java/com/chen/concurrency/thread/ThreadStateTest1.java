package com.chen.concurrency.thread;

/**
 * 线程的状态
 * <p>
 * @Author LeifChen
 * @Date 2020-09-15
 */
public class ThreadStateTest1 {

    public static void main(String[] args) {
        Runnable runnable = () -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(i);
            }
        };
        Thread thread = new Thread(runnable);
        // NEW
        System.out.println(thread.getState());
        thread.start();
        // RUNNABLE
        System.out.println(thread.getState());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // TERMINATED
        System.out.println(thread.getState());
    }
}
