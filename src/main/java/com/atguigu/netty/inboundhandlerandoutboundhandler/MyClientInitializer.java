package com.atguigu.netty.inboundhandlerandoutboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //加入一个 出站的handler  对数据进行一个编码
        /**
         * 注意：
         *    编写Encoder 是要注意传入的数据类型和处理的数据类型要一致，因为
         *    它会根据 自定义的handler所传的数据判断应不应该处理，如果一个处理就处理，不是的话就跳过Encoder直接传输
         * */
        pipeline.addLast(new MyLongToByteEncoder());

        //加入一个入站的handler 对数据进行解码
        pipeline.addLast(new MyByteToLongDecoder());

        //加入一个自定义的handler  处理业务
        pipeline.addLast(new MyClientHandler());
    }
}
