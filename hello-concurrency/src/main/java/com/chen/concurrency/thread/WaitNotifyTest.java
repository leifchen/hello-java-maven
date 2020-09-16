package com.chen.concurrency.thread;

/**
 * 线程的等待与通知
 * <p>
 * @Author LeifChen
 * @Date 2020-09-15
 */
public class WaitNotifyTest {

    private static Object obj = new Object();

    static class Task1 implements Runnable {
        @Override
        public void run() {
            synchronized (obj) {
                System.out.println(Thread.currentThread().getName() + "获取锁开始执行");
                try {
                    System.out.println(Thread.currentThread().getName() + "调用wait等待并释放锁");
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "重新获取到锁结束执行");
            }
        }
    }

    static class Task2 implements Runnable {
        @Override
        public void run() {
            synchronized (obj) {
                System.out.println(Thread.currentThread().getName() + "调用notify通知");
                obj.notify();
            }
        }
    }

    public static void main(String[] args) {
        Thread thread1 = new Thread(new Task1());
        Thread thread2 = new Thread(new Task2());

        thread1.start();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread2.start();
    }
}
