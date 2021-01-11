package com.atguigu.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {
    public static void main(String[] args) throws IOException {
        //得到一个网络通道
        SocketChannel socketChannel  = SocketChannel.open();
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //提供服务端的ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1",6666);
        //连接服务器
        if(!socketChannel.connect(inetSocketAddress)){
            while (!socketChannel.finishConnect()){
                System.out.println("不会阻塞，其他工作");
            }
        }
        //如果连接成功，就发送数据
        String str = "h ello，三十个";
        //Wraps a byte array into a buffer
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        //发送数据，将buffer 数据写入 channel
        socketChannel.write(buffer);
        System.in.read();
    }
}
