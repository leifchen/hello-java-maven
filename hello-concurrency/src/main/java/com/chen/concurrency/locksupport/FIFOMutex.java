package com.chen.concurrency.locksupport;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * 先进先出锁
 * <p>
 * @Author LeifChen
 * @Date 2020-04-22
 */
public class FIFOMutex {

    private final AtomicBoolean locked = new AtomicBoolean(false);
    private final Queue<Thread> waiters = new ConcurrentLinkedQueue<>();

    public void lock() {
        boolean wasInterrupted = false;
        Thread current = Thread.currentThread();
        waiters.add(current);

        // 如果当前线程不是队首或者当前锁已经被其他线程获取，则调用park方法挂起自己
        while (waiters.peek() != current || locked.compareAndSet(false, true)) {
            LockSupport.park(this);
            if (Thread.interrupted()) {
                wasInterrupted = true;
            }
        }
        waiters.remove();
        if (wasInterrupted) {
            current.interrupt();
        }
    }

    public void unlock() {
        locked.set(false);
        LockSupport.unpark(waiters.peek());
    }
}
