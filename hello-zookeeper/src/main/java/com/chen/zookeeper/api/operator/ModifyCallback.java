package com.chen.zookeeper.api.operator;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.data.Stat;

/**
 * 修改节点的回调
 * <p>
 * @Author LeifChen
 * @Date 2019-05-30
 */
@Slf4j
public class ModifyCallback implements StatCallback {

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        log.info("修改节点值: {}", path);
        log.info(ctx.toString());
    }
}
