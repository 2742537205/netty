package com.atguigu.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {
    //定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    //构造器
    //初始化工作
    public GroupChatServer(){
        try {
            //得到选择器
            selector = Selector.open();
            //获取ServerSocketChannel
            listenChannel =  ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            ///设置非阻塞
            listenChannel.configureBlocking(false);
            //将该listenChannel 注册到 selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //监听
    public void listen(){
        try {

            //循环处理
            while (true){
                int count = selector.select();
                if(count>0){ //有事件处理
                    //遍历得到 selectionkey集合
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while(iterator.hasNext()){
                        //取出selectionkey
                        SelectionKey key = iterator.next();

                        //监听到accept
                        if(key.isAcceptable()){
                            SocketChannel sc = listenChannel.accept();
                            //设置非阻塞
                            sc.configureBlocking(false);
                            //将sc注册到selector
                            sc.register(selector,SelectionKey.OP_READ);
                            //提示
                            System.out.println(sc.getRemoteAddress()+"上线");
                        }

                        if(key.isReadable()){ //通道发生read事件，即通道是可读的状态
                            readData(key);
                        }
                        //当前的key删除，防止重复处理
                        iterator.remove();
                    }


                }else{
                    System.out.println("等待....");
                }
            }

        }catch (Exception e){
                e.printStackTrace();
        }finally {

        }
    }

    //读取客户端消息
    public void readData(SelectionKey key){
        //取到关联的channel
        SocketChannel channel = null;

        try{
            //得到 channel
            channel = (SocketChannel) key.channel();
            //创建buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            int count = channel.read(buffer);

            //根据count的值做处理

            if(count>0){
                //把缓冲区的数据转成字符串
                String msg = new String(buffer.array());
                //输出该消息
                System.out.println("form客户端："+msg);
                //向其他的客户端转发消息（去掉自己 ）
                sendInfoToTherClients(msg,channel);
            }

        }catch (IOException e){
            try {
                System.out.println(channel.getRemoteAddress()+"离线了");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            }catch (IOException e2){
                e2.printStackTrace();
            }

        }finally {

        }
    }

    public void sendInfoToTherClients(String msg, SocketChannel self) throws IOException{
        System.out.println("服务器转发消息中....");
        //遍历所有注册到 selector上的SocketChannel，并排除 self
        for (SelectionKey key:selector.keys()){
            //通过 key 取出对应的 SocketChannel
            Channel targetChannel = key.channel();
            //排除self
            if(targetChannel instanceof SocketChannel && targetChannel !=self){
                //转型
                SocketChannel dest = (SocketChannel)targetChannel;
                //将msg存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将Buffer 的数据写入通道
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args){
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }


}
