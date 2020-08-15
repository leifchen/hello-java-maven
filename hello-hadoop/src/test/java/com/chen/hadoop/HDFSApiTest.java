package com.chen.hadoop;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;

import static org.junit.Assert.assertTrue;

/**
 * HDFS API 单元测试
 * <p>
 * @Author LeifChen
 * @Date 2020-08-14
 */
@Slf4j
public class HDFSApiTest {

    private static final String HADOOP_URI = "hdfs://hadoop100:9000";


    Configuration configuration = null;
    FileSystem fileSystem = null;

    @Before
    public void setUp() throws Exception {
        log.info("HDFS API set up.");
        System.setProperty("hadoop.home.dir", "E:\\hadoop-2.6.0-cdh5.15.1");
        configuration = new Configuration();
        configuration.set("dfs.replication", "1");
        fileSystem = FileSystem.get(new URI(HADOOP_URI), configuration, "hadoop");
    }

    @After
    public void tearDown() {
        configuration = null;
        fileSystem = null;
        log.info("HDFS API tear down.");
    }

    /**
     * 创建目录
     * @throws IOException
     */
    @Test
    public void mkdir() throws IOException {
        boolean result = fileSystem.mkdirs(new Path("/hdfsapi/test"));
        assertTrue(result);
    }

    /**
     * 创建文件
     * @throws IOException
     */
    @Test
    public void create() throws IOException {
        FSDataOutputStream output = fileSystem.create(new Path("/hdfsapi/test/old.txt"));
        output.write("hello world".getBytes());
        output.flush();
        output.close();
    }

    /**
     * 重命名
     * @throws IOException
     */
    @Test
    public void rename() throws IOException {
        Path oldPath = new Path("/hdfsapi/test/old.txt");
        Path newPath = new Path("/hdfsapi/test/new.txt");
        boolean result = fileSystem.rename(oldPath, newPath);
        assertTrue(result);
    }

    /**
     * 上传本地文件到HDFS
     * @throws IOException
     */
    @Test
    public void copyFromLocalFile() throws IOException {
        Path src = new Path("E:/hello.txt");
        Path dst = new Path("/hdfsapi/test/hello.txt");
        fileSystem.copyFromLocalFile(src, dst);
    }

    /**
     * 从HDFS下载文件到本地
     * @throws IOException
     */
    @Test
    public void copyToLocalFile() throws IOException {
        Path src = new Path("/hdfsapi/test/new.txt");
        Path dst = new Path("E:/");
        fileSystem.copyToLocalFile(src, dst);
    }

    /**
     * 查看指定目录下的所有文件
     * @throws IOException
     */
    @Test
    public void listFiles() throws IOException {
        FileStatus[] fileStatuses = fileSystem.listStatus(new Path("/hdfsapi/test"));
        for (FileStatus fileStatus : fileStatuses) {
            String isDir = fileStatus.isDirectory() ? "文件夹" : "文件";
            String permission = fileStatus.getPermission().toString();
            short replication = fileStatus.getReplication();
            long len = fileStatus.getLen();
            String path = fileStatus.getPath().toString();
            log.info(isDir + "\t" + permission + "\t" + replication + "\t" + len + "\t" + path);
        }
    }

    /**
     * 递归列出文件夹下的所有文件
     * @throws IOException
     */
    @Test
    public void listFilesRecursive() throws IOException {
        RemoteIterator<LocatedFileStatus> files = fileSystem.listFiles(new Path("/hdfsapi"), true);
        while (files.hasNext()) {
            LocatedFileStatus file = files.next();
            String isDir = file.isDirectory() ? "文件夹" : "文件";
            String permission = file.getPermission().toString();
            short replication = file.getReplication();
            long len = file.getLen();
            String path = file.getPath().toString();
            log.info(isDir + "\t" + permission + "\t" + replication + "\t" + len + "\t" + path);
        }
    }

    /**
     * 查看文件块信息
     */
    @Test
    public void getFileBlockLocations() throws IOException {
        FileStatus fileStatus = fileSystem.getFileStatus(new Path("/hdfsapi/test/tmp/hello.txt"));
        BlockLocation[] blocks = fileSystem.getFileBlockLocations(fileStatus, 0, fileStatus.getLen());
        for (BlockLocation block : blocks) {
            for (String host : block.getHosts()) {
                log.info(host);
            }
        }
    }

    /**
     * 递归删除目录下的所有文件
     * @throws IOException
     */
    @Test
    public void delete() throws IOException {
        boolean result = fileSystem.delete(new Path("/hdfsapi/"), true);
        assertTrue(result);
    }
}