package com.atguigu.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 服务端
 * */

public class NIOServer {

    public static void main(String[] args) throws IOException {
        //创建ServerSocketChannel，等成功接收客户端的连接后，创建相应的SocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到一个 selector 对象
        Selector selector = Selector.open();

        //绑定一个端口6666，在服务端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //把serverSocketChannel 注册到 selector 关心事件为 OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待客户端连接
        while (true){
            //设置服务端等待1秒，如果没有关心事件发生，则返回
            if(selector.select(1000)==0){  //没有事件发生
                System.out.println("服务器等待1秒，无连接");
                continue;
            }

            //如果返回的大于0，就获取相关的 selectionKeys 集合
            //1.如果返回的>0 , 表示已经获取到关注的事件
            //2. selectionKeys() 返回关注事件的集合
            // 通过 selectionKeys 反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //遍历 Set<SelectionKey>，使用迭代器遍历
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()){
                //获取到SelectionKey
                SelectionKey key = keyIterator.next();
                //根据key 对应的通道发生的事件做相应处理
                if(key.isAcceptable()){  //判断如果是接收事件，则代表有新的客户端连接
                    //该客户端生成一个 SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    //将 socketChannel 设置为非阻塞
                    socketChannel.configureBlocking(false);
                    System.out.println("客户端连接成功，生成了一个socketChannel");
                    //将SocketChannel 注册到 selector ，关注事件为 OP_READ ,同时给socketChannel关联一个Buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                if(key.isReadable()){  //发生OP_READ
                    //通过key 反向获取到对应 Channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    //获取到该 channel 关联的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    channel.read(buffer);
                    System.out.println("form 客户端"+new String(buffer.array()));
                }
                //手动从集合中移除当前的 selectionkey，防止重复操作
                keyIterator.remove();



            }

        }


    }

}
