package com.chen.mvc.service.impl;

import com.chen.annotation.Service;
import com.chen.mvc.service.HelloService;
import lombok.extern.slf4j.Slf4j;

/**
 * HelloService的实现类
 * <p>
 * @Author LeifChen
 * @Date 2019-06-20
 */
@Slf4j
@Service("helloService")
public class HelloServiceImpl implements HelloService {

    @Override
    public void test() {
        log.info("执行HelloService的test()方法");
    }
}
