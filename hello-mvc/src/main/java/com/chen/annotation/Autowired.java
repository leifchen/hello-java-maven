package com.chen.annotation;

import java.lang.annotation.*;

/**
 * Autowired注解
 * <p>
 * @Author LeifChen
 * @Date 2019-06-20
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {

    String value() default "";
}
