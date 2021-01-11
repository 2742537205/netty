package com.atguigu.netty.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;

public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        //创建BossGroup 和 WorkGroup
        //说明
        //1.创建两个线程组 bossgroup 和 workgroup
        //2.bossgroup 负责处理连接请求，workgroup负责客户端的业务处理
        //3.两个都是无限循环监听，具体看笔记里的流程图
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        //创建服务器端的启动对象，配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();

        //使用链式编程来进行设置
        bootstrap.group(bossGroup,workerGroup) //设置两个线程组
        .channel(NioServerSocketChannel.class) // 作为服务器的通道实现
        .option(ChannelOption.SO_BACKLOG,128) //设置线程队列得到连接个数
        .childOption(ChannelOption.SO_KEEPALIVE,true) //设置保持活动连接状态
        .childHandler(new ChannelInitializer<SocketChannel>() {
            //给 pipeline 设置处理器
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {

                ChannelPipeline pipeline = socketChannel.pipeline();
                //在 pipeline中加入 解码器
                //指定对哪种对象进行解码
                pipeline.addLast("decoder",new ProtobufDecoder(StudentPOJO.Student.getDefaultInstance()));
                pipeline.addLast(new NettyServerHandler());
            }
        }); //给 workerGroup 的 NioEventLoop 对应的管道设置处理器
        System.out.println("服务器 is ready.........");

        //绑定一个端口并且同步，生成了一个 ChannelFuture对象
        //启动服务器(并绑定端口)
        ChannelFuture cf = bootstrap.bind(6668).sync();

        //给cf注册监听器 ，监控我们关心的事件
        cf.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(cf.isSuccess()){
                    System.out.println("监听端口  6668 成功");
                }else{
                    System.out.println("监听端口 6668 失败");
                }
            }
        });

        cf.channel().closeFuture().sync();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
