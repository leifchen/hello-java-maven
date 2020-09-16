package com.chen.concurrency.thread;

/**
 * 两个线程交替打印0~100的奇偶数，使用 wait & notify 实现
 * <p>
 * @Author LeifChen
 * @Date 2020-09-16
 */
public class EvenOdd {

    private static int count = 0;
    private static Object obj = new Object();

    public static void main(String[] args) {
        TurningRunner runner = new TurningRunner();
        new Thread(runner, "偶数").start();
        new Thread(runner, "奇数").start();
    }

    static class TurningRunner implements Runnable {
        @Override
        public void run() {
            while (count <= 100) {
                synchronized (obj) {
                    System.out.println(Thread.currentThread().getName() + ":" + count++);
                    obj.notify();
                    if (count <= 100) {
                        try {
                            obj.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
