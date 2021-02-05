package com.chen.hbase;


import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;

/**
 * HBase 连接
 * <p>
 * @Author LeifChen
 * @Date 2021-02-04
 */
@Slf4j
public class HBaseConn {

    private static final HBaseConn INSTANCE = new HBaseConn();
    private static Configuration configuration;
    private static Connection connection;

    private HBaseConn() {
        System.setProperty("hadoop.home.dir", "E:\\hadoop-2.6.0-cdh5.15.1");
        if (configuration == null) {
            try {
                configuration = HBaseConfiguration.create();
                configuration.set("hbase.zookeeper.quorum", "hadoop100:2181");
            } catch (Exception e) {
                log.error("HBase配置失败", e);
            }
        }
    }

    private static Connection getConnection() {
        if (connection == null || connection.isClosed()) {
            try {
                connection = ConnectionFactory.createConnection(configuration);
            } catch (IOException e) {
                log.error("获取HBase连接失败", e);
            }
        }
        return connection;
    }

    public static Connection getHBaseConn() {
        return INSTANCE.getConnection();
    }

    public static Table getTable(String tableName) throws IOException {
        Connection conn = getHBaseConn();
        if (conn != null) {
            return conn.getTable(TableName.valueOf(tableName));
        }
        return null;
    }

    public static void closeConn() {
        if (connection != null) {
            try {
                connection.close();
            } catch (IOException e) {
                log.error("关闭HBase连接失败", e);
            }
        }
    }
}
