package com.chen.concurrency.immutable;

import com.chen.concurrency.annotation.ThreadSafe;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;

/**
 * 不可变对象--Collections.unmodifiableXXX
 * <p>
 * @Author LeifChen
 * @Date 2019-06-17
 */
@Slf4j
@ThreadSafe
public class ImmutableDemo3 {

    private static ImmutableList<Integer> list = ImmutableList.of(1, 2, 3);

    private static ImmutableSet<Integer> set = ImmutableSet.copyOf(list);

    private static ImmutableMap<String, Integer> map = ImmutableMap.<String, Integer>builder()
            .put("a", 1)
            .put("b", 2)
            .put("c", 3)
            .build();

    public static void main(String[] args) {
        // Error
        list.add(4);
        map.put("a", 2019);
    }
}
