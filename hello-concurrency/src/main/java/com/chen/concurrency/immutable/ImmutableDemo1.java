package com.chen.concurrency.immutable;

import com.chen.concurrency.annotation.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 不可变对象--final
 * <p>
 * @Author LeifChen
 * @Date 2019-06-17
 */
@Slf4j
@NotThreadSafe
public class ImmutableDemo1 {

    private static final Integer YEAR = 2019;
    private static final String NAME = "LeifChen";
    private static final Map<String, Integer> MAP = new HashMap<>();

    static {
        MAP.put("a", 1);
        MAP.put("b", 1);
        MAP.put("c", 1);
    }

    public static void main(String[] args) {
        // Can not assigned
        // YEAR = 2;
        // NAME = "Chen";
        MAP.put("a", 2019);
        log.info("{}", MAP.get("a"));
    }
}
