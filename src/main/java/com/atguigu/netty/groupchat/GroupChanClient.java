package com.atguigu.netty.groupchat;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class GroupChanClient {
    //属性
    private final String host;
    private final int port;

    public GroupChanClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //得到 pipeline
                        ChannelPipeline pipeline = ch.pipeline();
                        //加入相关handler
                        pipeline.addLast("decoder", new StringDecoder());
                        pipeline.addLast("encoder", new StringEncoder());
                        //加入自定义的handler
                        pipeline.addLast(new GroupChatClientHander());
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect(host,port).sync();
        //得到 channel
        Channel channel = channelFuture.channel();
        //客户端需要输入信息，创建一个扫描器
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String msg = scanner.nextLine();
            channel.writeAndFlush(msg+"\r\n");

        }
        group.shutdownGracefully();
    }

    public static void main(String[] args) throws InterruptedException {
        new GroupChanClient("127.0.0.1",7000).run();
    }


}