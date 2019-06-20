package com.chen.annotation;

import java.lang.annotation.*;

/**
 * RequestParam注解
 * <p>
 * @Author LeifChen
 * @Date 2019-06-20
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {

    String value() default "";
}
