package com.atguigu.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        byte[] buffer = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(buffer);
        String msg = new String(buffer, Charset.forName("utf-8"));
        System.out.println("服务器接收到数据"+msg);
        System.out.println("服务器接收到次数"+(++this.count));

        //服务器回送数据给客户端
        ByteBuf response = Unpooled.copiedBuffer(UUID.randomUUID().toString()+" ",Charset.forName("utf-8"));
        channelHandlerContext.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
