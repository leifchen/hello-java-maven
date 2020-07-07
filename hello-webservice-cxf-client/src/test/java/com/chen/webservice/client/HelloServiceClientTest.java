package com.chen.webservice.client;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * WebService测试
 * <p>
 * @Author LeifChen
 * @Date 2020-07-07
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloServiceClientTest {

    @Autowired
    private HelloServiceClient helloServiceClient;

    @Test
    public void hello() {
        String msg = helloServiceClient.hello();
        log.info(msg);
        assertEquals("Hello cxf webservice", msg);
    }
}