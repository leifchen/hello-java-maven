package com.chen.concurrency.immutable;

import com.chen.concurrency.annotation.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 不可变对象--Collections.unmodifiableXXX
 * <p>
 * @Author LeifChen
 * @Date 2019-06-17
 */
@Slf4j
@NotThreadSafe
public class ImmutableDemo2 {

    private static Map<String, Integer> map = new HashMap<>();

    static {
        map.put("a", 1);
        map.put("b", 1);
        map.put("c", 1);
        map = Collections.unmodifiableMap(map);
    }

    public static void main(String[] args) {
        map.put("a", 2019);
        // error
        log.info("{}", map.get("a"));
    }
}
