package com.chen.webservice.config;

import com.alibaba.dubbo.remoting.http.servlet.DispatcherServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * DubboConfig
 * <p>
 * @Author LeifChen
 * @Date 2019-05-20
 */
@Configuration
public class DubboConfig {

    @Bean
    public ServletRegistrationBean dubboDispatcherServlet() {
        return new ServletRegistrationBean(new DispatcherServlet(), "/services/*");
    }
}
