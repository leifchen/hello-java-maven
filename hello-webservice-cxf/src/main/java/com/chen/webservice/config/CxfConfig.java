package com.chen.webservice.config;

import com.chen.webservice.service.UserService;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

/**
 * CxfConfig
 * <p>
 * @Author LeifChen
 * @Date 2019-05-16
 */
@Configuration
public class CxfConfig {

    @Autowired
    private Bus bus;
    @Autowired
    private UserService userService;

    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, userService);
        endpoint.publish("/user");
        return endpoint;
    }
}
