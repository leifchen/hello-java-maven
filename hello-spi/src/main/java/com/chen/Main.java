package com.chen;

import com.chen.service.PrintService;

import java.util.ServiceLoader;

/**
 * 主程序
 * <p>
 * @Author LeifChen
 * @Date 2020-10-19
 */
public class Main {

    public static void main(String[] args) {
        ServiceLoader<PrintService> printServices = ServiceLoader.load(PrintService.class);
        printServices.forEach(PrintService::print);
    }
}
