package com.atguigu.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class GroupChatServerHander extends SimpleChannelInboundHandler<String> {

    //定义channel组，管理所有的channel
    //GlobalEventExecutor.INSTANCE  是全局的事件执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    //handlerAdded 表示连接建立，一但连接，第一个被执行
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel=ctx.channel();

        /**
         * 将该客户端加入聊天的信息推送给其他在线的客户端
         * 该方法会将channelGroup中所有的channel遍历，并发送信息
         * */
        channelGroup.writeAndFlush("[客户端]"+channel.remoteAddress()+"加入聊天\n");
        //将channel添加
        channelGroup.add(channel);
    }

    //断开连接，将xx客户端离开的信息推送给当前在线的客户
    /**
     * 注意：触发当前方法的话，底层会自动将 channel 从 channelGroup 里remover掉。
     * */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]"+channel.remoteAddress()+"离开了\n");
    }

    //表示channel 处于活动状态，提示xx上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+" 上线了");
    }

    //表示channel 处于非活动状态，提示xx离线
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+" 离线了");
    }

    //读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        //获取到当前channel
        Channel channel = ctx.channel();

        //遍历channelGroup ，根据不同的情况，回送不同的信息
        channelGroup.forEach(ch->{
            if(channel!=ch){  //判断如果不是当前channel，转发消息
                ch.writeAndFlush("[客户]"+channel.remoteAddress()+"发送了消息"+s+"\n");
            }else{  //回显自己发送的消息给自己
                ch.writeAndFlush("[自己发送了消息]"+s+"\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //关闭通道
        ctx.close();
    }
}
