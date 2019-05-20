package com.chen.webservice.service.impl;

import com.chen.webservice.service.DubboService;

import javax.jws.WebService;

/**
 * DubboServiceImpl
 * <p>
 * @Author LeifChen
 * @Date 2019-05-16
 */
@WebService(serviceName = "DubboService",
        targetNamespace = "http://service.webservice.chen.com",
        endpointInterface = "com.chen.webservice.service.DubboService")
public class DubboServiceImpl implements DubboService {

    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
