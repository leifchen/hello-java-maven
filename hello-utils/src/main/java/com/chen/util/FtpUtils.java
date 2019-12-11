package com.chen.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FTP工具类
 * <p>
 * @Author LeifChen
 * @Date 2019-11-27
 */
@Slf4j
public class FtpUtils {

    private static final String DEFAULT_CHARSET = "UTF-8";

    private String host;
    private String user;
    private String pwd;

    private FTPClient ftpClient;

    private FtpUtils(String host, String user, String pwd) {
        this.ftpClient = new FTPClient();
        this.host = host;
        this.user = user;
        this.pwd = pwd;
    }

    /**
     * 创建FTP的客户端
     * @param host     FTP主机
     * @param username FTP用户
     * @param password FTP密码
     * @return
     */
    public static FtpUtils createFtpClient(String host, String username, String password) {
        return new FtpUtils(host, username, password);
    }

    /**
     * 连接FTP服务
     * @return
     * @throws IOException
     */
    public void connect() throws IOException {
        try {
            ftpClient.connect(host, 21);
        } catch (IOException e) {
            throw new IOException("Can't find FTP server : " + host);
        }

        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            disconnect();
            throw new IOException("Can't connect to server : " + host);
        }

        if (!ftpClient.login(user, pwd)) {
            disconnect();
            throw new IOException("Can't login to server : " + host);
        }

        // 设置文件传输的编码
        ftpClient.setControlEncoding(DEFAULT_CHARSET);
        // 设置传输的文件类型
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        // 设置FTP协议为PASV被动模式
        ftpClient.enterLocalPassiveMode();
    }

    /**
     * 关闭FTP服务
     */
    public void disconnect() {
        if (null != ftpClient && ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                log.error("Close FTP server failed : {}", e);
            }
        }
    }

    /**
     * 逐级创建FTP目录
     * @param remotePath
     * @throws Exception
     */
    public void createDir(String remotePath) throws Exception {
        check();

        String tempPath = "";
        String[] paths = remotePath.split("/");
        for (String path : paths) {
            tempPath += "/" + path;
            if (!ftpClient.changeWorkingDirectory(tempPath)) {
                ftpClient.makeDirectory(tempPath);
            }
        }
    }

    /**
     * 检查FTP服务
     * @throws Exception
     */
    private void check() throws Exception {
        if (!ftpClient.isConnected() || !ftpClient.isAvailable()) {
            throw new Exception("FTP连接已关闭或连接无效");
        }
    }

    /**
     * 上传文件
     * @param localPath  本地目录
     * @param remotePath FTP目录
     * @param fileName   文件名称
     * @throws Exception
     */
    public void upload(String localPath, String remotePath, String fileName) throws Exception {
        check();

        localPath = localPath.endsWith(File.pathSeparator) ? localPath : localPath + "/";
        File uploadFile = new File(localPath + fileName);

        if (!uploadFile.exists()) {
            throw new Exception("待上传文件为空或者文件不存在");
        }

        FileInputStream input = null;
        try {
            if (uploadFile.isFile()) {
                input = new FileInputStream(uploadFile);
                createDir(remotePath);
                boolean changeFlag = ftpClient.changeWorkingDirectory(remotePath);
                if (changeFlag) {
                    ftpClient.storeFile(fileName, input);
                    input.close();
                } else {
                    throw new Exception("变更FTP目录异常");
                }
            }
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new Exception("文件上传失败");
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

    /**
     * 下载文件
     * @param localPath  本地目录
     * @param remotePath FTP目录
     * @param fileName   文件名称
     * @throws Exception
     */
    public void download(String localPath, String remotePath, String fileName) throws Exception {
        check();

        if (StringUtils.isBlank(localPath) || StringUtils.isBlank(fileName) || StringUtils.isBlank(remotePath)) {
            throw new Exception("[localPath]、[remotePath]、[fileName]为空");
        }

        OutputStream outputStream = null;
        try {
            localPath = localPath.endsWith(File.pathSeparator) ? localPath : localPath + "/";
            File localFile = new File(localPath + fileName);

            outputStream = new FileOutputStream(localFile);
            boolean changeFlag = ftpClient.changeWorkingDirectory(remotePath);
            if (changeFlag) {
                ftpClient.retrieveFile(fileName, outputStream);
                outputStream.flush();
                outputStream.close();
            } else {
                throw new Exception("变更FTP目录异常");
            }
        } catch (IOException e) {
            log.error("文件下载失败", e);
            throw new Exception("文件下载失败");
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    /**
     * 文件迁移
     * @param srcPath    源目录
     * @param srcName    源文件名称
     * @param targetPath 目标目录
     * @param targetName 目标文件名称
     * @throws Exception
     */
    public void rename(String srcPath, String srcName, String targetPath, String targetName) throws Exception {
        check();

        if (StringUtils.isBlank(srcPath) || StringUtils.isBlank(srcName) || StringUtils.isBlank(targetPath) || StringUtils.isBlank(targetName)) {
            throw new Exception("[srcPath]、[srcFile]、[targetPath]、[targetName]为空");
        }

        createDir(targetPath);
        String from = (srcPath.endsWith(File.pathSeparator) ? srcPath : srcPath + "/") + srcName;
        String to = (targetPath.endsWith(File.pathSeparator) ? targetPath : targetPath + "/") + targetName;

        try {
            ftpClient.rename(from, to);
        } catch (Exception e) {
            log.error("文件迁移失败", e);
            throw new Exception("文件迁移失败");
        }
    }

    /**
     * 删除文件
     * @param remotePath
     * @param fileName
     * @throws Exception
     */
    public void delete(String remotePath, String fileName) throws Exception {
        check();

        try {
            boolean changeFlag = ftpClient.changeWorkingDirectory(remotePath);
            if (changeFlag) {
                boolean deleteFlag = ftpClient.deleteFile(fileName);
                if (deleteFlag) {
                    log.info("删除服务器文件成功");
                } else {
                    log.warn("删除服务器文件失败");
                }
            } else {
                throw new Exception("变更FTP目录异常");
            }
        } catch (IOException e) {
            log.error("文件删除失败", e);
            throw new Exception("文件删除失败");
        }
    }

    /**
     * 列表展示文件名称
     * @param filePath 文件目录
     * @return
     * @throws IOException
     */
    public List<String> listFileNames(String filePath) throws IOException {
        List<String> fileList = new ArrayList<>();

        FTPFile[] ftpFiles = ftpClient.listFiles(filePath);
        for (FTPFile ftpFile : ftpFiles) {
            if (ftpFile.isFile()) {
                fileList.add(ftpFile.getName());
            }
        }

        return fileList;
    }
}