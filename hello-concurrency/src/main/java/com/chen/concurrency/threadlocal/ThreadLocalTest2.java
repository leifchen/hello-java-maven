package com.chen.concurrency.threadlocal;

/**
 * 线程内共享变量的ThreadLocal用法
 * <p>
 * @Author LeifChen
 * @Date 2020-10-16
 */
public class ThreadLocalTest2 {

    public static void main(String[] args) {
        new Service1().process("");
    }
}

class Service1 {
    public void process(String name) {
        User user = new User("LeifChen");
        UserContextHolder.holder.set(user);
        new Service2().process();
    }
}

class Service2 {
    public void process() {
        User user = UserContextHolder.holder.get();
        System.out.println("Service2拿到用户名：" + user.name);
        new Service3().process();
    }
}

class Service3 {
    public void process() {
        User user = UserContextHolder.holder.get();
        System.out.println("Service3拿到用户名：" + user.name);
        UserContextHolder.holder.remove();
    }
}

class UserContextHolder {
    public static ThreadLocal<User> holder = new ThreadLocal<>();
}

class User {
    String name;
    public User(String name) {
        this.name = name;
    }
}
