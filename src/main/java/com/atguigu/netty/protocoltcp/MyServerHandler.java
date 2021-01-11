package com.atguigu.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol msg) throws Exception {
        //接受到数据，并处理
        int len = msg.getLen();
        byte[] content = msg.getContext();
        System.out.println("服务器接收到消息如下：");
        System.out.println("长度"+len);
        System.out.println("内容="+new String(content,Charset.forName("utf-8")));
        System.out.println("服务器接收到消息次数："+(++this.count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
