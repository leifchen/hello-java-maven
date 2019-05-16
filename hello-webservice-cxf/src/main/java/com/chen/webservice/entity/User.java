package com.chen.webservice.entity;

import lombok.Data;
import lombok.ToString;

/**
 * User
 * <p>
 * @Author LeifChen
 * @Date 2019-05-16
 */
@Data
@ToString
public class User {
    private Integer id;
    private String name;
    private Integer age;
}
