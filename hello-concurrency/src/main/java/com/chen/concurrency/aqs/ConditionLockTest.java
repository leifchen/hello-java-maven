package com.chen.concurrency.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock.newCondition
 * <p>
 * @Author LeifChen
 * @Date 2020-10-12
 */
@Slf4j
public class ConditionLockTest {

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        new Thread(() -> {
            try {
                lock.lock();
                log.info("wait signal");
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                log.info("get signal");
                lock.unlock();
            }
        }).start();
        new Thread(() -> {
            try {
                lock.lock();
                log.info("get lock");
                Thread.sleep(2000);
                condition.signalAll();
                log.info("send signal");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }).start();
    }
}
