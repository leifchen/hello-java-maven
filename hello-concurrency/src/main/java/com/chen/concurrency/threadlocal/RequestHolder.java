package com.chen.concurrency.threadlocal;

/**
 * RequestHolder
 * <p>
 * @Author LeifChen
 * @Date 2019-06-17
 */
public class RequestHolder {

    private static final ThreadLocal<Long> REQUEST_HOLDER = new ThreadLocal<>();

    public static void add(Long id) {
        REQUEST_HOLDER.set(id);
    }

    public static Long getId() {
        return REQUEST_HOLDER.get();
    }

    public static void remove() {
        REQUEST_HOLDER.remove();
    }
}
