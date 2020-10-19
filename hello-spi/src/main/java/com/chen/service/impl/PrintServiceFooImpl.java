package com.chen.service.impl;

import com.chen.service.PrintService;

/**
 * Foo输出接口的实现类
 * <p>
 * @Author LeifChen
 * @Date 2020-10-19
 */
public class PrintServiceFooImpl implements PrintService {
    @Override
    public void print() {
        System.out.println("Foo");
    }
}
