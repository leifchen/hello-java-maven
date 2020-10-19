package com.chen.service.impl;

import com.chen.service.PrintService;

/**
 * Bar输出接口的实现类
 * <p>
 * @Author LeifChen
 * @Date 2020-10-19
 */
public class PrintServiceBarImpl implements PrintService {
    @Override
    public void print() {
        System.out.println("Bar");
    }
}
