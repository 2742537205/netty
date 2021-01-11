package com.atguigu.netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;


import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        /**
         *  channelHandlerContext 上下文对象
         *  byteBuf  入站的 ByteBuf
         *  list 集合，将解码后的数据传给下一个handler
         * */
        System.out.println("服务端的解码器");
        //因为long 有8个字节 需要判断有8个字符，才能读取一个long
        if(byteBuf.readableBytes()>=8){
            list.add(byteBuf.readLong());
        }
    }
}
