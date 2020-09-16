package com.chen.concurrency.thread;

/**
 * 线程的等待与通知
 * <p>
 * @Author LeifChen
 * @Date 2020-09-15
 */
public class WaitNotifyAllTest {

    private static Object obj = new Object();

    static class Task implements Runnable {
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

    public static void main(String[] args) {
        Task task = new Task();
        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);
        Thread thread3 = new Thread(() -> {
            synchronized (obj) {
                obj.notifyAll();
                System.out.println(Thread.currentThread().getName() + "调用notifyAll通知");
            }
        });

        thread1.start();
        thread2.start();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread3.start();
    }
}
