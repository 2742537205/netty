package com.atguigu.netty.protocoltcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipelin = socketChannel.pipeline();
        pipelin.addLast(new MyMessageEncoder());
        pipelin.addLast(new MyClientHandler());
    }
}
