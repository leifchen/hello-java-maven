package com.chen.concurrency.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * StampedLock
 * <p>
 * @Author LeifChen
 * @Date 2020-10-12
 */
@Slf4j
public class StampedLockTest {

    private static ExecutorService service = Executors.newSingleThreadExecutor();
    private static StampedLock lock = new StampedLock();
    private static int count = 0;

    public static void main(String[] args) {
        log.info("启动");
        long start = System.currentTimeMillis();
        // readLock();
        optimisticRead();
        long end = writeLock();
        log.info("耗时：{}", end - start);
        service.shutdown();
    }

    /**
     * 悲观写
     * @return
     */
    private static long writeLock() {
        long stamp = lock.writeLock();
        count += 1;
        lock.unlockWrite(stamp);
        log.info("数据写入完成");
        return System.currentTimeMillis();
    }

    /**
     * 悲观读
     */
    private static void readLock() {
        service.submit(() -> {
            int currentCount;
            long stamp = lock.readLock();
            try {
                currentCount = count;
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } finally {
                lock.unlockRead(stamp);
            }
            log.info("readLock=={}", currentCount);
        });

        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 乐观读
     */
    private static void optimisticRead() {
        service.submit(() -> {
            long stamp = lock.tryOptimisticRead();
            int currentCount = count;
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 判断count是否进入写模式
            if (!lock.validate(stamp)) {
                // 已经进入写模式，没办法只能老老实实的获取读锁
                stamp = lock.readLock();
                try {
                    currentCount = count;
                } finally {
                    lock.unlockRead(stamp);
                }
            }
            // 走到这里，说明count还没有被写，那么可以不用加读锁，减少了读锁的开销
            log.info("optimisticRead=={}", currentCount);
        });

        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
