package com.chen.annotation;

import java.lang.annotation.*;

/**
 * Controller注解
 * <p>
 * @Author LeifChen
 * @Date 2019-06-20
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {

    String value() default "";
}
