package com.chen.concurrency.atomic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.stream.IntStream;

/**
 * LongAccumulator的测试
 * <p>
 * @Author LeifChen
 * @Date 2020-11-01
 */
@Slf4j
public class LongAccumulatorTest {

    public static void main(String[] args) {
        LongAccumulator accumulator = new LongAccumulator((x, y) -> x + y, 1);
        ExecutorService executor = Executors.newFixedThreadPool(10);
        IntStream.range(1, 10).forEach(i -> executor.submit(() -> accumulator.accumulate(i)));

        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        log.info("{}", accumulator.getThenReset());
    }
}
