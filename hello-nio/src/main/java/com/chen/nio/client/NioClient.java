package com.chen.nio.client;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * NIO客户端
 * <p>
 * @Author LeifChen
 * @Date 2019-05-23
 */
public class NioClient {

    /**
     * 启动
     * @param nickname
     * @throws IOException
     */
    public static void start(String nickname) throws IOException {
        // 连接服务器端
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8000));

        // 接收服务器端响应
        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("Thread-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        // 新开线程，专门负责来接收服务器端的响应数据
        singleThreadPool.execute(() -> new NioClientHandler(selector).run());
        singleThreadPool.shutdown();

        // 向服务器端发送数据
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String request = scanner.nextLine();
            if (request != null && request.length() > 0) {
                socketChannel.write(Charset.forName("UTF-8").encode(nickname + " : " + request));
            }
        }
    }
}
