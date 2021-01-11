package com.atguigu.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class MyMessageDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("MyMessageDecoder decoder被调用");
        //需要将得到二进制字节码 -》 MessageProtocol 数据包(对象)
        int length = byteBuf.readInt();
        byte[] content = new byte[length];
        byteBuf.readBytes(content);

        //封装成对象，放入out，传递下一个handler业务处理
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(length);
        messageProtocol.setContext(content);

        list.add(messageProtocol);
    }
}