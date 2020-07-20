package com.chen;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

/**
 * Test
 * <p>
 * @Author LeifChen
 * @Date 2020-07-16
 */
public class Test {

    public static void main(String[] args) {
        initSet();
        checkPrimeNumber();
    }

    /**
     * 双括号初始化集合
     */
    private static void initSet() {
        Set<String> set = new HashSet<String>() {
            {
                add("A");
                add("B");
                add("C");
            }
        };
        System.out.println(set);
    }

    /**
     * 判断素数
     */
    private static void checkPrimeNumber() {
        System.out.println(BigInteger.valueOf(7).isProbablePrime(1));
    }
}
