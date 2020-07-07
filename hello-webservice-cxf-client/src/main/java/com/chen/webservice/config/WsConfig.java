package com.chen.webservice.config;

import com.chen.webservice.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * WebService 配置
 * <p>
 * @Author LeifChen
 * @Date 2020-07-07
 */
@Slf4j
@Configuration
public class WsConfig {

    @Resource
    private WsProperties ws;

    @Resource(name = "helloServiceProxy")
    private JaxWsProxyFactoryBean helloServiceProxy;

    /**
     * 返回 访问WebService代理工厂
     * @return 访问WebService代理工厂
     */
    @Bean
    @Lazy
    public JaxWsProxyFactoryBean helloServiceProxy() {
        String[] arrUrl = ws.getUrl();
        log.debug("☆☆☆ arrUrl = {}", arrUrl.length);
        if (ArrayUtils.isEmpty(arrUrl)) {
            throw new IllegalArgumentException("ws.url is null!");
        }
        JaxWsProxyFactoryBean wsProxy = new JaxWsProxyFactoryBean();
        wsProxy.setServiceClass(HelloService.class);
        wsProxy.setAddress(arrUrl[0]);
        return wsProxy;
    }

    /**
     * 返回 访问soup服务的对象
     * @return soup服务的接口对象, bean的id就是方法的名称performanceMgtService
     */
    @Bean
    @Lazy
    public HelloService helloService() {
        this.ignoreParameterConf(helloServiceProxy);
        return (HelloService) helloServiceProxy.create();
    }

    /**
     * 忽略参数不匹配问题
     * @param proxy
     */
    private void ignoreParameterConf(JaxWsProxyFactoryBean proxy) {
        Map<String, Object> properties = proxy.getProperties();
        if (properties == null) {
            properties = new HashMap<>();
            proxy.setProperties(properties);
        }
        properties.put("set-jaxb-validation-event-handler", Boolean.FALSE);
        log.info("====registerSoapService set-jaxb-validation-event-handler   false====");
    }
}
