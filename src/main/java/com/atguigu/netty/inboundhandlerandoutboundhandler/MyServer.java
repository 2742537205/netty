package com.atguigu.netty.inboundhandlerandoutboundhandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class MyServer {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        //创建服务器端的启动对象，配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();

        //使用链式编程来进行设置
        bootstrap.group(bossGroup,workerGroup) //设置两个线程组
                .channel(NioServerSocketChannel.class) // 作为服务器的通道实现
                .childHandler(new MyServerInitializer()); //给 workerGroup 的 NioEventLoop 对应的管道设置处理器

        //绑定一个端口并且同步，生成了一个 ChannelFuture对象
        //启动服务器(并绑定端口)
        ChannelFuture cf = bootstrap.bind(7000).sync();

        cf.channel().closeFuture().sync();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
