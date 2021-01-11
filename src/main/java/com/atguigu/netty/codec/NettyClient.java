package com.atguigu.netty.codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        //客户端需要一个事件循环组
        EventLoopGroup eventExecutors = new NioEventLoopGroup();

        //创建客户端启动对象
        //注意客户端使用的不是 ServerBootStrap 而是  Bootstrap
        Bootstrap bootstrap = new Bootstrap();

        //设置相关参数
        bootstrap.group(eventExecutors) //设置线程组
                 .channel(NioSocketChannel.class) //设置客户端通道的实现类(反射)
                 .handler(new ChannelInitializer<SocketChannel>() {
                     @Override
                     protected void initChannel(SocketChannel socketChannel) throws Exception {
                         ChannelPipeline pipeline = socketChannel.pipeline();
                         //在 pipeline 中加入 ProtoBufEncoder
                         pipeline.addLast("encoder",new ProtobufEncoder());
                         pipeline.addLast(new NettyClientHandler());
                     }
                 });
        System.out.println("客户端 ok ...");

        //启动客户端去连接服务器端
        //关于 ChannelFuture 要分析，涉及到 netty的异步模型
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1",6668).sync();
        //给关闭通道进行监听
        channelFuture.channel().closeFuture().sync();
        eventExecutors.shutdownGracefully();

    }
}
