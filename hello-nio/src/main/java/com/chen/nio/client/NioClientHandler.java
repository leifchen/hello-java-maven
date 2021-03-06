package com.chen.nio.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * 客户端线程类，专门接收服务器端响应信息
 * <p>
 * @Author LeifChen
 * @Date 2019-05-23
 */
public class NioClientHandler implements Runnable {

    private Selector selector;

    public NioClientHandler(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = (SelectionKey) iterator.next();
                    iterator.remove();
                    if (selectionKey.isReadable()) {
                        readHandler(selectionKey, selector);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 可读事件处理器
     */
    private void readHandler(SelectionKey selectionKey, Selector selector) throws IOException {
        // 要从SelectionKey中获取到已经就绪的Channel
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        // 创建Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 循环读取服务器端响应信息
        StringBuilder response = new StringBuilder();
        while (socketChannel.read(byteBuffer) > 0) {
            // 切换buffer为读模式
            byteBuffer.flip();
            // 读取buffer中的内容
            response.append(Charset.forName("UTF-8").decode(byteBuffer));
        }

        // 将Channel再次注册到Selector上，监听他的可读事件
        socketChannel.register(selector, SelectionKey.OP_READ);

        // 将服务器端响应信息打印到本地
        if (response.length() > 0) {
            System.out.println(response);
        }
    }

}
