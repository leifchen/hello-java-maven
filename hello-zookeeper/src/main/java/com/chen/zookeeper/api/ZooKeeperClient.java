package com.chen.zookeeper.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * ZooKeeper客户端
 * <p>
 * @Author LeifChen
 * @Date 2019-05-28
 */
@Slf4j
public class ZooKeeperClient implements Watcher {

    private static final String CONNECT_STRING = "192.168.33.101:2181";
    private static final int SESSION_TIMEOUT = 5000;

    public static void main(String[] args) throws Exception {
        ZooKeeper zk = new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT, new ZooKeeperClient());

        log.info("客户端开始连接ZooKeeper服务器...");
        log.warn("连接状态: {}", zk.getState());
        Thread.sleep(1000);
        log.warn("连接状态: {}", zk.getState());

        long sessionId = zk.getSessionId();
        String ssid = "0x" + Long.toHexString(sessionId);
        log.info("sessionId: {}", ssid);
        byte[] sessionPasswd = zk.getSessionPasswd();

        Thread.sleep(200);
        log.warn("开始会话重连...");

        ZooKeeper zkSession = new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT, new ZooKeeperClient(), sessionId, sessionPasswd);

        log.warn("重新连接状态: {}", zkSession.getState());
        Thread.sleep(1000);
        log.warn("重新连接状态: {}", zkSession.getState());
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        log.info("接收到watch通知: {}", watchedEvent);
    }
}
