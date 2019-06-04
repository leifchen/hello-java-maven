package com.chen.zookeeper.api.operator;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.AsyncCallback.VoidCallback;

/**
 * 删除节点的回调
 * <p>
 * @Author LeifChen
 * @Date 2019-05-29
 */
@Slf4j
public class DeleteCallback implements VoidCallback {

    @Override
    public void processResult(int rc, String path, Object ctx) {
        log.info("删除节点: {}", path);
        log.info(ctx.toString());
    }
}
