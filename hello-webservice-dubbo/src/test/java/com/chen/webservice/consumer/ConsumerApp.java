package com.chen.webservice.consumer;

import com.chen.webservice.service.DubboService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * ConsumerApp
 * <p>
 * @Author LeifChen
 * @Date 2019-05-17
 */
public class ConsumerApp {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("consumer.xml");
        context.start();
        DubboService dubboService = context.getBean("dubboService", DubboService.class);
        String hello = dubboService.sayHello("Dubbo");
        System.out.println("result: " + hello);
    }
}
