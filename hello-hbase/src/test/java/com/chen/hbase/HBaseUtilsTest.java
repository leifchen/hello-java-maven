package com.chen.hbase;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * HBase 工具类 - 单元测试
 * <p>
 * @Author LeifChen
 * @Date 2021-02-04
 */
@Slf4j
public class HBaseUtilsTest {

    @Test
    public void createTable() {
        boolean res = HBaseUtils.createTable("FileTable", new String[]{"fileInfo", "saveInfo"});
        assertTrue(res);
    }

    @Test
    public void addFileDetails() {
        boolean res = HBaseUtils.putRow("FileTable", "rowkey1", "fileInfo", "name", "file1.txt");
        assertTrue(res);
        res = HBaseUtils.putRow("FileTable", "rowkey1", "fileInfo", "type", "txt");
        assertTrue(res);
        res = HBaseUtils.putRow("FileTable", "rowkey1", "fileInfo", "size", "1024");
        assertTrue(res);
        res = HBaseUtils.putRow("FileTable", "rowkey1", "saveInfo", "creator", "zhangsan");
        assertTrue(res);

        List<Put> puts = new ArrayList<>();
        Put put = new Put(Bytes.toBytes("rowkey2"));
        put.addColumn(Bytes.toBytes("fileInfo"), Bytes.toBytes("name"), Bytes.toBytes("file2.jpg"));
        puts.add(put);
        put.addColumn(Bytes.toBytes("fileInfo"), Bytes.toBytes("type"), Bytes.toBytes("jpg"));
        puts.add(put);
        put.addColumn(Bytes.toBytes("fileInfo"), Bytes.toBytes("size"), Bytes.toBytes("1024"));
        puts.add(put);
        put.addColumn(Bytes.toBytes("saveInfo"), Bytes.toBytes("creator"), Bytes.toBytes("lisi"));
        puts.add(put);
        res = HBaseUtils.putRows("FileTable", puts);
        assertTrue(res);
    }

    @Test
    public void getFileDetails() {
        Result result = HBaseUtils.getRow("FileTable", "rowkey1");
        assertNotNull(result);

        log.info("rowkey={}", Bytes.toString(result.getRow()));
        log.info("fileName={}", Bytes.toString(result.getValue(Bytes.toBytes("fileInfo"), Bytes.toBytes("name"))));
    }

    @Test
    public void scanFileDetails() {
        ResultScanner scanner = HBaseUtils.getScanner("FileTable", "rowkey1", "rowkey2", null);
        assertNotNull(scanner);
        scanner.forEach(result -> {
            log.info("rowkey={}", Bytes.toString(result.getRow()));
            log.info("fileName={}", Bytes.toString(result.getValue(Bytes.toBytes("fileInfo"), Bytes.toBytes("name"))));
        });
        scanner.close();
    }

    @Test
    public void deleteRow() {
        boolean res = HBaseUtils.deleteRow("FileTable", "rowkey1");
        assertTrue(res);
    }

    @Test
    public void deleteTable() {
        boolean res = HBaseUtils.deleteTable("FileTable");
        assertTrue(res);
    }
}