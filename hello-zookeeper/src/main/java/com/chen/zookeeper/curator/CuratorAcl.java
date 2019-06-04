package com.chen.zookeeper.curator;

import com.chen.zookeeper.util.AclUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * Curator 权限
 * <p>
 * @Author LeifChen
 * @Date 2019-06-04
 */
@Slf4j
public class CuratorAcl {
    private static final String CONNECT_STRING = "192.168.33.101:2181";
    private static final int SESSION_TIMEOUT = 10000;

    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);

        CuratorFramework curator = CuratorFrameworkFactory.builder()
                .connectString(CONNECT_STRING)
                .sessionTimeoutMs(SESSION_TIMEOUT)
                .retryPolicy(retryPolicy)
                .authorization("digest", "user1:123456".getBytes())
                .namespace("acl")
                .build();
        curator.start();

        CuratorFrameworkState state = curator.getState();
        log.info("当前客户的状态: {}", (state == CuratorFrameworkState.STARTED ? "连接中" : "已关闭"));

        String nodePath = "/father/child";

        List<ACL> acls = new ArrayList<>();
        Id user1 = new Id("digest", AclUtils.getDigestUserPwd("user1:123456"));
        Id user2 = new Id("digest", AclUtils.getDigestUserPwd("user2:123456"));
        acls.add(new ACL(ZooDefs.Perms.ALL, user1));
        acls.add(new ACL(ZooDefs.Perms.READ, user2));
        acls.add(new ACL(ZooDefs.Perms.DELETE | ZooDefs.Perms.CREATE, user2));

        // 创建节点
        byte[] data = "LeifChen".getBytes();
        curator.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(acls, true)
                .forPath(nodePath, data);

        Thread.sleep(2000);

        curator.close();
        state = curator.getState();
        log.info("当前客户的状态: {}", (state == CuratorFrameworkState.STARTED ? "连接中" : "已关闭"));
    }
}
