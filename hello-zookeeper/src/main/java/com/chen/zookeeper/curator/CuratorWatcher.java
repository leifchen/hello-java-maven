package com.chen.zookeeper.curator;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.List;

/**
 * Curator Watcher
 * <p>
 * @Author LeifChen
 * @Date 2019-06-04
 */
@Slf4j
public class CuratorWatcher {

    private static final String CONNECT_STRING = "192.168.33.101:2181";
    private static final int SESSION_TIMEOUT = 10000;

    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);

        CuratorFramework curator = CuratorFrameworkFactory.builder()
                .connectString(CONNECT_STRING)
                .sessionTimeoutMs(SESSION_TIMEOUT)
                .retryPolicy(retryPolicy)
                .namespace("watcher")
                .build();
        curator.start();

        CuratorFrameworkState state = curator.getState();
        log.info("当前客户的状态: {}", (state == CuratorFrameworkState.STARTED ? "连接中" : "已关闭"));

        // 为节点添加Watcher事件
        final NodeCache nodeCache = new NodeCache(curator, "/test");
        nodeCache.start(true);
        if (nodeCache.getCurrentData() != null) {
            log.info("节点初始化数据{}", new String(nodeCache.getCurrentData().getData()));
        } else {
            log.info("节点初始化数据为空");
        }
        nodeCache.getListenable().addListener(() -> {
            if (nodeCache.getCurrentData() == null) {
                log.info("空");
                return;
            }
            log.info("节点路径:{}, 数据:{}", nodeCache.getCurrentData().getPath(), new String(nodeCache.getCurrentData().getData()));
        });

        final PathChildrenCache childrenCache = new PathChildrenCache(curator, "/child", true);
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);

        List<ChildData> childDataList = childrenCache.getCurrentData();
        log.info("当前节点的子节点数据列表:");
        for (ChildData child : childDataList) {
            log.info(new String(child.getData()));
        }

        childrenCache.getListenable().addListener((client, event) -> {
            if (event.getType() == PathChildrenCacheEvent.Type.INITIALIZED) {
                log.info("子节点初始化OK");
            } else if (event.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED) {
                log.info("添加子节点:" + event.getData().getPath());
                log.info("子节点数据:" + new String(event.getData().getData()));
            } else if (event.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED) {
                log.info("删除子节点:" + event.getData().getPath());
            } else if (event.getType() == PathChildrenCacheEvent.Type.CHILD_UPDATED) {
                log.info("修改子节点路径:" + event.getData().getPath());
                log.info("修改子节点数据:" + new String(event.getData().getData()));
            }
        });

        Thread.sleep(2000000);

        curator.close();
        state = curator.getState();
        log.info("当前客户的状态: {}", (state == CuratorFrameworkState.STARTED ? "连接中" : "已关闭"));
    }
}
