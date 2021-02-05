package com.chen.hbase;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Table;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * HBase 连接 - 单元测试
 * <p>
 * @Author LeifChen
 * @Date 2021-02-04
 */
@Slf4j
public class HBaseConnTest {

    @Test
    public void getHBaseConn() {
        Connection conn = HBaseConn.getHBaseConn();
        assertFalse(conn.isClosed());
        HBaseConn.closeConn();
        assertTrue(conn.isClosed());
    }

    @Test
    public void getTable() {
        try {
            Table table = HBaseConn.getTable("test");
            assertEquals("test", table.getName().getNameAsString());
            table.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}