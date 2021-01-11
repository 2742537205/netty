package com.atguigu.netty.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;// 上下文
    private String result;
    private String para; //客户端调用方法，传入参数

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context=ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result = msg.toString();
        notify();  //唤醒等待的线程
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       ctx.close();
    }

    //被代理对象调用 ，发送数据给服务器 ->wait -> 等待被唤醒(channelRead) -> 返回结果
    @Override
    public synchronized Object call() throws Exception {
        context.writeAndFlush(para);
        //进行wait
        wait();  //等待channelRead 方法获取到服务器的结果后，唤醒
        return result;
    }

    void  setPara(String para){
        this.para = para;
    }
}
