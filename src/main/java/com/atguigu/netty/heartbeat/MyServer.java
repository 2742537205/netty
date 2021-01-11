package com.atguigu.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class MyServer {

    public static void main(String[] args){
        //创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();  //8个
        ServerBootstrap b = new ServerBootstrap();
        try {


            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //获取到pipeline
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //加入一个netty 提供 IdleStateHandler
                            /**
                             * 说明：
                             * 1.IdleStateHandler 是 netty 提供的处理空闲状态的处理器
                             * 2. long readerIdlerTime：表示多次时间没有读，就会触发超时事件执行自定义 userEventTrigger()方法
                             * 3.long writeIdlerTime：表示多长时间没有写，就会触发超时事件执行自定义 userEventTrigger()方法
                             * 4.long allIdleTime： 表示多长时间没有读写，就会触发超时事件执行自定义 userEventTrigger()方法
                             * */
                            pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
                            //加入一个对空闲检测进一步的handler(自定义)
                            pipeline.addLast(new MyServerHandler());
                        }
                    });
            //启动服务器
            ChannelFuture channelFuture = b.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
