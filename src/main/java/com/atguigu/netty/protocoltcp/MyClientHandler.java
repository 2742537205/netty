package com.atguigu.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    int count;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol byteBuf) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //使用客户端发送10条数据
        for(int i=0;i<10;i++){
           String mes = "哈哈哈哈哈哈，谷歌";
           byte[] context = mes.getBytes(Charset.forName("utf-8"));
           int length = mes.getBytes(Charset.forName("utf-8")).length;

           //创建协议包对象
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(length);
            messageProtocol.setContext(context);
            ctx.writeAndFlush(messageProtocol);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
