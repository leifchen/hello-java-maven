package com.chen.zookeeper.api.operator;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.AsyncCallback.StringCallback;

/**
 * 创建节点的回调
 * <p>
 * @Author LeifChen
 * @Date 2019-05-29
 */
@Slf4j
public class CreateCallback implements StringCallback {

    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        log.info("创建节点: {}", path);
        log.info(ctx.toString());
    }
}
