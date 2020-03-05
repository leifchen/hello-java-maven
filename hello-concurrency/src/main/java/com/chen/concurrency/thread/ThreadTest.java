package com.chen.concurrency.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 线程
 * <p>
 * @Author LeifChen
 * @Date 2020-03-04
 */
public class ThreadTest {

    public static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("thread: extends Thread");
        }
    }

    public static class RunnableTask implements Runnable {
        @Override
        public void run() {
            System.out.println("thread: implements Runnable");
        }
    }

    public static class CallableTask implements Callable<String> {
        @Override
        public String call() {
            return "thread: FutureTask";
        }
    }

    public static void main(String[] args) {
        // 线程的创建方式1：继承Thread类
        new MyThread().start();

        // 线程的创建方式2：实现Runnable接口
        new Thread(new RunnableTask()).start();

        // 线程的创建方式3：使用FutureTask
        FutureTask<String> futureTask = new FutureTask<>(new CallableTask());
        new Thread(futureTask).start();
        String result = null;
        try {
            result = futureTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(result);
    }
}
