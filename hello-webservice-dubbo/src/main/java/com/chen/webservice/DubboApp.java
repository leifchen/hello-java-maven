package com.chen.webservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * DubboApp
 * <p>
 * @Author LeifChen
 * @Date 2019-05-20
 */
@SpringBootApplication
@ImportResource(locations = "classpath:provider.xml")
public class DubboApp {

    public static void main(String[] args) {
        SpringApplication.run(DubboApp.class, args);
    }
}
