# hello-concurrency

Java 并发多线程

## [线程](src/main/java/com/chen/concurrency/thread)

- 线程的6种状态
- 线程的创建
- 线程的启动
- 线程的中断
- 线程的等待与通知
- 线程异常的捕获

## [ThreadLocal](src/main/java/com/chen/concurrency/threadlocal)

**ThreadLocal** 是一个为线程提供线程局部变量的工具类。

它的思想也十分简单，就是为线程提供一个线程私有的变量副本，这样多个线程都可以随意更改自己线程局部的变量，不会影响到其他线程。
不过需要注意的是，`ThreadLocal` 提供的只是一个浅拷贝，如果变量是一个引用类型，那么就要考虑它内部的状态是否会被改变，
想要解决这个问题可以通过重写 `ThreadLocal` 的 `initialValue()` 函数来自己实现深拷贝，建议在使用 `ThreadLocal` 一开始时就重写该函数。

## [Atomic包](src/main/java/com/chen/concurrency/atomic)

**Atomic** 包下的类都是基于 **CAS** 实现原子性。

## [AQS](src/main/java/com/chen/concurrency/aqs)

**AQS**（AbstractQueuedSynchronizer）基于FIFO等待队列实现，是一个用于构建锁、同步器、协作工具类的工具类。

ReentrantLock、CountDownLatch、Semaphore 内部都有一个内部类 **Sync**，其中 Sync 类继承了 AQS。

## [Future](src/main/java/com/chen/concurrency/future)

**Future** 能获得任务执行的状态，并且可以获取任务的返回值。

**FutureTask** 是一种包装器，可以把 Callable 转化成 Future 和 Runnable，因为它同时实现二者的接口。主要用来获取 Future 和任务结果。
