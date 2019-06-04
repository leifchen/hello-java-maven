package com.chen.zookeeper.api.operator;

import com.chen.zookeeper.util.AclUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;

/**
 * ZooKeeper的节点操作
 * <p>
 * @Author LeifChen
 * @Date 2019-05-29
 */
@Slf4j
public class ZooKeeperOperator implements Watcher {

    private static final String SCHEMA_DIGEST = "digest";
    private static final String SCHEMA_IP = "ip";

    private static final String CONNECT_STRING = "192.168.33.101:2181";
    private static final int SESSION_TIMEOUT = 5000;

    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT, new ZooKeeperOperator());
        Thread.sleep(200);

        String createCtx = "{'create':'success'}";
        // 创建永久节点
        zooKeeper.create("/test", "LeifChen".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, new CreateCallback(), createCtx);
        Thread.sleep(200);
        // 创建临时节点
        zooKeeper.create("/temp", "2019".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        Thread.sleep(200);

        String modifyCtx = "{'modify':'success'}";
        Thread.sleep(200);
        // 修改节点值
        zooKeeper.setData("/test", "Chen".getBytes(), 0, new ModifyCallback(), modifyCtx);

        Stat stat = new Stat();
        // 查看节点
        byte[] resByte = zooKeeper.getData("/test", true, stat);
        log.info("查看节点test修改后的值: {}", new String(resByte));
        log.info("查看节点test修改后的版本: {}", stat.getVersion());

        String deleteCtx = "{'delete':'success'}";
        // 删除节点
        zooKeeper.delete("/test", stat.getVersion(), new DeleteCallback(), deleteCtx);
        Thread.sleep(200);

        // 创建子节点
        zooKeeper.create("/parent", "p".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, new CreateCallback(), createCtx);
        zooKeeper.create("/parent/child1", "1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, new CreateCallback(), createCtx);
        zooKeeper.create("/parent/child2", "2".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, new CreateCallback(), createCtx);
        zooKeeper.create("/parent/child3", "3".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, new CreateCallback(), createCtx);
        Thread.sleep(200);
        // 判断节点是否存在
        Stat parentStat = zooKeeper.exists("/parent", true);
        if (parentStat != null) {
            log.info("节点/parent存在");
        } else {
            log.info("节点/parent不存在");
        }
        // 查看子节点
        List<String> children = zooKeeper.getChildren("/parent", true);
        for (String child : children) {
            log.info("查看parent子节点: {}", child);
        }

        // 自定义用户密码权限
        List<ACL> userAcls = new ArrayList<>(2);
        Id user1 = new Id(SCHEMA_DIGEST, AclUtils.getDigestUserPwd("user1:123456"));
        Id user2 = new Id(SCHEMA_DIGEST, AclUtils.getDigestUserPwd("user2:123456"));
        userAcls.add(new ACL(ZooDefs.Perms.ALL, user1));
        userAcls.add(new ACL(ZooDefs.Perms.READ, user2));
        userAcls.add(new ACL(ZooDefs.Perms.DELETE | ZooDefs.Perms.CREATE, user2));
        zooKeeper.create("/acl/digest", "digest".getBytes(), userAcls, CreateMode.PERSISTENT, new CreateCallback(), createCtx);
        Thread.sleep(200);
        zooKeeper.addAuthInfo(SCHEMA_DIGEST, "user2:123456".getBytes());
        zooKeeper.create("/acl/digest/child", "digest".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT, new CreateCallback(), createCtx);
        Thread.sleep(200);

        // 自定义IP权限
        List<ACL> ipAcls = new ArrayList<>(2);
        Id ipId1 = new Id(SCHEMA_IP, "192.168.33.101");
        ipAcls.add(new ACL(ZooDefs.Perms.ALL, ipId1));
        zooKeeper.create("/ip", "ip".getBytes(), ipAcls, CreateMode.PERSISTENT, new CreateCallback(), createCtx);
        Thread.sleep(200);
        zooKeeper.create("/ip/child", "ip".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT, new CreateCallback(), createCtx);
        Thread.sleep(200);
    }

    @Override
    public void process(WatchedEvent event) {
        log.info("接收到watch的通知: {}", event);
    }
}
