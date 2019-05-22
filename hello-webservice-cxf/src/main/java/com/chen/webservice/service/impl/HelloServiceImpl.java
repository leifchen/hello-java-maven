package com.chen.webservice.service.impl;

import com.chen.webservice.service.HelloService;
import org.springframework.stereotype.Service;

import javax.jws.WebService;

/**
 * HelloService的实现类
 * <p>
 * @Author LeifChen
 * @Date 2019-05-22
 */
@Service("helloService")
@WebService(serviceName = "HelloService",
        targetNamespace = "http://service.webservice.chen.com",
        endpointInterface = "com.chen.webservice.service.HelloService")
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String msg) {
        return "Hello " + msg;
    }
}
