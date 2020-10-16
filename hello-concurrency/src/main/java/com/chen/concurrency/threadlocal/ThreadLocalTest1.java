package com.chen.concurrency.threadlocal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * SimpleDateFormat的ThreadLocal用法
 * <p>
 * @Author LeifChen
 * @Date 2020-10-15
 */
public class ThreadLocalTest1 {

    public static ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            threadPool.submit(() -> {
                String date = new ThreadLocalTest1().date(finalI);
                System.out.println(date);
            });
        }
        threadPool.shutdown();
    }

    public String date(int seconds) {
        Date date = new Date(1000 * seconds);
        SimpleDateFormat dateFormat = DateFormatContextHolder.dateFormatThreadLocal.get();
        return dateFormat.format(date);
    }
}

class DateFormatContextHolder {
    public static ThreadLocal<SimpleDateFormat> dateFormatThreadLocal = ThreadLocal
            .withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
}