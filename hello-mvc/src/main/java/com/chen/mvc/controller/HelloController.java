package com.chen.mvc.controller;

import com.chen.annotation.Autowired;
import com.chen.annotation.Controller;
import com.chen.annotation.RequestMapping;
import com.chen.annotation.RequestParam;
import com.chen.mvc.service.HelloService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 控制器
 * <p>
 * @Author LeifChen
 * @Date 2019-06-20
 */
@Slf4j
@Controller
@RequestMapping("/hello")
public class HelloController {

    @Autowired("helloService")
    private HelloService helloService;

    @RequestMapping("/test")
    public void test(HttpServletResponse res,
                     @RequestParam("name") String name) {

        helloService.test();
        try {
            res.getWriter().write("hello " + name);
        } catch (IOException e) {
            log.error("exception", e);
        }
    }
}
