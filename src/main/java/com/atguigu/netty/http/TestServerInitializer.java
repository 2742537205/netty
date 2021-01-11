package com.atguigu.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class TestServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //向管道加入处理器

        //得到管道
        ChannelPipeline pipeline = socketChannel.pipeline();

        /**
         *  加入一个 netty 提供的 httpServerCodec
         *  httpServerCodec说明
         *   1.HttpServerCodec 是 netty 提供的处理 http 的 编-解码器
         * */
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());

        //2.增加一个自定义的handler
        pipeline.addLast("MyTestHttpServerHandler",new TestHttpServerHandler());

    }
}
