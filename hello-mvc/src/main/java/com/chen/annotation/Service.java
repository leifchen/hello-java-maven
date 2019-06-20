package com.chen.annotation;

import java.lang.annotation.*;

/**
 * Service注解
 * <p>
 * @Author LeifChen
 * @Date 2019-06-20
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {

    String value() default "";
}
