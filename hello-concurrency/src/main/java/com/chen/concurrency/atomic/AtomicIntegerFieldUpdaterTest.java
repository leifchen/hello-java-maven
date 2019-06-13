package com.chen.concurrency.atomic;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * AtomicIntegerFieldUpdater
 * <p>
 * @Author LeifChen
 * @Date 2019-06-13
 */
@Slf4j
public class AtomicIntegerFieldUpdaterTest {

    private static final int EXPECT = 100;
    private static final int UPDATE = 120;

    private static AtomicIntegerFieldUpdater<AtomicIntegerFieldUpdaterTest> updater = AtomicIntegerFieldUpdater.newUpdater(AtomicIntegerFieldUpdaterTest.class, "count");

    @Getter
    private volatile int count = 100;

    public static void main(String[] args) {
        AtomicIntegerFieldUpdaterTest test = new AtomicIntegerFieldUpdaterTest();
        if (updater.compareAndSet(test, EXPECT, UPDATE)) {
            log.info("update success 1, {}", test.getCount());
        }

        if (updater.compareAndSet(test, EXPECT, UPDATE)) {
            log.info("update success 2, {}", test.getCount());
        } else {
            log.info("update failed, {}", test.getCount());
        }
    }
}
