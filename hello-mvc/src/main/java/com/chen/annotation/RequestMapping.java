package com.chen.annotation;

import java.lang.annotation.*;

/**
 * RequestMapping注解
 * <p>
 * @Author LeifChen
 * @Date 2019-06-20
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

    String value() default "";
}
