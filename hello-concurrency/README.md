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

Atomic包下的类都是基于CAS实现原子性。
