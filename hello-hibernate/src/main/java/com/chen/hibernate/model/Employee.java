package com.chen.hibernate.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Employee
 * <p>
 * @Author LeifChen
 * @Date 2019-07-04
 */
@Data
public class Employee {
    private Integer id;
    private String name;
    private BigDecimal salary;
}
