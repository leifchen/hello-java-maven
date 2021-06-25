package com.chen.hbase;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.CollectionUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * RegionObserver 协处理器
 * <p>
 *
 * @Author LeifChen
 * @Date 2021-06-25
 */
public class RegionObserver extends BaseRegionObserver {

    private byte[] columnFamily = Bytes.toBytes("cf");
    private byte[] countCol = Bytes.toBytes("countCol");
    private byte[] unDeleteCol = Bytes.toBytes("unDeleteCol");

    /**
     * cf:countCol 进行累加操作（每次插入的时候都要与之前的值进行相加）
     */
    @Override
    public void prePut(ObserverContext<RegionCoprocessorEnvironment> e,
                       Put put, WALEdit edit,
                       Durability durability) throws IOException {

        if (put.has(columnFamily, countCol)) {
            // 获取old countCol value
            Result rs = e.getEnvironment().getRegion().get(new Get(put.getRow()));
            int oldNum = 0;
            for (Cell cell : rs.rawCells()) {
                if (CellUtil.matchingColumn(cell, columnFamily, countCol)) {
                    oldNum = Integer.parseInt(Bytes.toString(CellUtil.cloneValue(cell)));
                }
            }

            // 获取new countCol value
            List<Cell> cells = put.get(columnFamily, countCol);
            int newNum = 0;
            for (Cell cell : cells) {
                if (CellUtil.matchingColumn(cell, columnFamily, countCol)) {
                    newNum = Integer.parseInt(Bytes.toString(CellUtil.cloneValue(cell)));
                }
            }

            // sum AND update Put实例
            put.addColumn(columnFamily, countCol, Bytes.toBytes(String.valueOf(oldNum + newNum)));
        }
    }

    /**
     * 不能直接删除 unDeleteCol （删除countCol时将unDeleteCol一并删除）
     */
    @Override
    public void preDelete(ObserverContext<RegionCoprocessorEnvironment> e, Delete delete,
                          WALEdit edit,
                          Durability durability) throws IOException {

        // 判断是否操作cf列族
        List<Cell> cells = delete.getFamilyCellMap().get(columnFamily);
        if (CollectionUtils.isEmpty(cells)) {
            return;
        }

        boolean deleteFlag = false;
        for (Cell cell : cells) {
            byte[] qualifier = CellUtil.cloneQualifier(cell);

            if (Arrays.equals(qualifier, unDeleteCol)) {
                throw new IOException("can not delete unDel column");
            }

            if (Arrays.equals(qualifier, countCol)) {
                deleteFlag = true;
            }
        }

        if (deleteFlag) {
            delete.addColumn(columnFamily, unDeleteCol);
        }
    }
}
