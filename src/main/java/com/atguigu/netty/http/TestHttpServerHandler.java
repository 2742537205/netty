package com.atguigu.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.nio.ByteBuffer;

/**
 *  说明：
 *         1.SimpleChannelInboundHandler 是 ChannelInboundHandlerAdapter 的继承类
 *         2.HttpObject 客户端和服务器相互通讯的数据被封装成 httpObject
 * */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {


    //channelRead0   读取客户端
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        //判断httpObject是不是httpquest请求
        if(httpObject instanceof HttpRequest){
            System.out.println("msg 类型："+httpObject.getClass());
            System.out.println("客户端地址："+channelHandlerContext.channel().remoteAddress());

            //服务器过滤特定的资源
            //获取到请求包
            HttpRequest httpRequest = (HttpRequest)httpObject;
            //获取uri ,过滤特定的资源
            URI uri = new URI(httpRequest.uri());
            if("/favicon.ico".equals(uri.getPath())){
                System.out.println("请求了 favicon.ico ,不做响应");
                return;
            }

            //回复信息给浏览器【http协议】
            ByteBuf context = Unpooled.copiedBuffer("hello，我是服务器", CharsetUtil.UTF_8);

            //构造一个http的响应 即 httpresponse
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK,context);

            //设置响应头的字符集
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain;charset=utf-8");

            //向响应头设置 返回数据的长度
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,context.readableBytes());

            //将构建好的 响应包(response) 返回
            channelHandlerContext.writeAndFlush(response);



        }
    }
}
