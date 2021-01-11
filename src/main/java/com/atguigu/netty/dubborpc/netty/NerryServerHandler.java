package com.atguigu.netty.dubborpc.netty;

import com.atguigu.netty.dubborpc.customer.ClientBootstrap;
import com.atguigu.netty.dubborpc.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NerryServerHandler  extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取客户端发送的信息，并调用服务
        System.out.println("msg"+msg);

        if(msg.toString().startsWith(ClientBootstrap.providerName)){
            String result = new HelloServiceImpl().hello(msg.toString().substring(msg.toString().lastIndexOf("#")+1));
            ctx.writeAndFlush(result);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
