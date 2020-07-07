package com.chen.webservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * WebService 配置属性
 * <p>
 * @Author LeifChen
 * @Date 2020-07-07
 */
@Data
@Component
@ConfigurationProperties(prefix = "ws")
public class WsProperties {

    private String[] url;
}
