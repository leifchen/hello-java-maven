package com.chen.zookeeper.curator;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * Curator节点操作
 * <p>
 * @Author LeifChen
 * @Date 2019-06-04
 */
@Slf4j
public class CuratorOperator {

    private static final String CONNECT_STRING = "192.168.33.101:2181";
    private static final int SESSION_TIMEOUT = 10000;

    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
        // RetryPolicy retryPolicy = new RetryNTimes(3, 5000);
        // RetryPolicy retryPolicy = new RetryOneTime(3000);
        // RetryPolicy retryPolicy = new RetryForever(3000);
        // RetryPolicy retryPolicy = new RetryUntilElapsed(2000, 3000);

        // 构建连接ZooKeeper
        CuratorFramework curator = CuratorFrameworkFactory.builder()
                .connectString(CONNECT_STRING)
                .sessionTimeoutMs(SESSION_TIMEOUT)
                .retryPolicy(retryPolicy)
                .namespace("workspace")
                .build();
        curator.start();

        CuratorFrameworkState state = curator.getState();
        log.info("当前客户的状态: {}", (state == CuratorFrameworkState.STARTED ? "连接中" : "已关闭"));

        // 创建节点
        curator.create()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath("/a", "0".getBytes());

        // 更新节点数据
        curator.setData()
                .withVersion(0)
                .forPath("/a", "1".getBytes());

        // 读取节点数据
        Stat stat = new Stat();
        byte[] data = curator.getData().storingStatIn(stat).forPath("/a");
        log.info("节点/a的数据为:{}", new String(data));
        log.info("节点/a的版本为:{}", stat.getVersion());

        // 删除节点
        curator.delete()
                .guaranteed()
                .deletingChildrenIfNeeded()
                .forPath("/a");

        // 递归创建节点
        curator.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath("/b/b1", "0".getBytes());
        curator.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath("/b/b2", "0".getBytes());
        curator.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath("/b/b3", "0".getBytes());

        // 查询子节点数据
        List<String> childNodes = curator.getChildren().forPath("/b");
        for (String child : childNodes) {
            log.info("子节点: {}", child);
        }

        // 判断节点是否存在
        Stat existStat = curator.checkExists().forPath("/b/b1");
        log.info("节点/b/b1{}", existStat == null ? "不存在" : "存在");

        Thread.sleep(2000);

        curator.close();
        state = curator.getState();
        log.info("当前客户的状态: {}", (state == CuratorFrameworkState.STARTED ? "连接中" : "已关闭"));
    }
}
