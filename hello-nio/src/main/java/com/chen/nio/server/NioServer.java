package com.chen.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO服务端
 * <p>
 * @Author LeifChen
 * @Date 2019-05-23
 */
public class NioServer {

    public static void main(String[] args) throws IOException {
        NioServer.start();
    }

    /**
     * 启动
     * @throws IOException
     */
    private static void start() throws IOException {
        // 1.创建Selector
        Selector selector = Selector.open();
        // 2.通过ServerSocketChannel创建channel通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 3.为channel通道绑定监听端口
        serverSocketChannel.bind(new InetSocketAddress(8000));
        // 4.设置channel为非阻塞模式
        serverSocketChannel.configureBlocking(false);
        // 5.将Channel注册到Selector上，监听连接事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器启动成功!");

        // 6.循环等待新接入的连接
        while (true) {
            int readyChannels = selector.select();
            if (readyChannels == 0) {
                continue;
            }

            // 获取可用Channel的集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                // SelectionKey实例
                SelectionKey selectionKey = (SelectionKey) iterator.next();
                // 移除Set中的当前SelectionKey
                iterator.remove();
                // 7.根据就绪状态，调用对应方法处理业务逻辑
                if (selectionKey.isAcceptable()) {
                    acceptHandler(serverSocketChannel, selector);
                }
                if (selectionKey.isReadable()) {
                    readHandler(selectionKey, selector);
                }
            }
        }
    }

    /**
     * 接入事件处理器
     * @param serverSocketChannel
     * @param selector
     * @throws IOException
     */
    private static void acceptHandler(ServerSocketChannel serverSocketChannel, Selector selector) throws IOException {
        // 接入事件，创建SocketChannel
        SocketChannel socketChannel = serverSocketChannel.accept();
        // 将SocketChannel设置为非阻塞工作模式
        socketChannel.configureBlocking(false);
        // 将Channel注册到Selector上，监听可读事件
        socketChannel.register(selector, SelectionKey.OP_READ);
        // 回复客户端提示信息
        socketChannel.write(Charset.forName("UTF-8").encode("你与聊天室里其他人都不是朋友关系，请注意隐私安全"));
    }

    /**
     * 可读事件处理器
     * @param selectionKey
     * @param selector
     */
    private static void readHandler(SelectionKey selectionKey, Selector selector) throws IOException {
        // 要从SelectionKey中获取到已经就绪的Channel
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        // 创建Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 循环读取客户端请求信息
        StringBuilder request = new StringBuilder();
        while (socketChannel.read(byteBuffer) > 0) {
            // 切换Buffer为读模式
            byteBuffer.flip();
            // 读取Buffer中内容
            request.append(Charset.forName("UTF-8").decode(byteBuffer));
        }
        // 将Channel再次注册到Selector上，监听它的可读事件
        socketChannel.register(selector, SelectionKey.OP_READ);

        // 将客户端发送的请求信息，广播给其他客户端
        if (request.length() > 0) {
            broadCast(selector, socketChannel, request.toString());
        }
    }

    /**
     * 广播信息
     * @param selector
     * @param sourceChannel
     * @param request
     */
    private static void broadCast(Selector selector, SocketChannel sourceChannel, String request) {
        // 获取到所有已接入的客户端Channel
        Set<SelectionKey> selectionKeySet = selector.keys();

        // 循环向所有Channel广播信息
        selectionKeySet.forEach(selectionKey -> {
            Channel targetChannel = selectionKey.channel();

            // 剔除发消息的客户端
            if (targetChannel instanceof SocketChannel && targetChannel != sourceChannel) {
                try {
                    ((SocketChannel) targetChannel).write(Charset.forName("UTF-8").encode(request));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
