package com.chen.concurrency.thread;

/**
 * 线程的状态
 * <p>
 * @Author LeifChen
 * @Date 2020-09-15
 */
public class ThreadStateTest2  implements Runnable{

    public static void main(String[] args) {
        ThreadStateTest2 task = new ThreadStateTest2();
        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);
        thread1.start();
        thread2.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // TIMED WAITING，因为正在执行线程休眠
        System.out.println(thread1.getState());
        // BLOCKED，thread2 等待获取锁
        System.out.println(thread2.getState());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // WAITING，因为正在执行 Object.wait()
        System.out.println(thread1.getState());
    }

    @Override
    public void run() {
        sync();
    }

    private synchronized void sync() {
        try {
            Thread.sleep(500);
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
