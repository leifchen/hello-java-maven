package com.chen.webservice.client;

import com.chen.webservice.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * HelloServiceClient
 * <p>
 * @Author LeifChen
 * @Date 2020-07-07
 */
@Service
public class HelloServiceClient {

    @Autowired
    private HelloService helloService;

    public String hello() {
        return helloService.hello("cxf webservice");
    }
}
