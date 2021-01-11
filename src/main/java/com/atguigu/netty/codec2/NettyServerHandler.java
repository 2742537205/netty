package com.atguigu.netty.codec2;


import com.atguigu.netty.codec.StudentPOJO;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 说明：
 *  1.我们自定义一个 handler 需要继续 netty 规定好的某个 HandLerAdapter(规范)
 *  2.这时我们自定义一个Handler  才能称为 handler
 * */
public class NettyServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {

    //读取数据实际（这里我们可以读取客户端发送的消息）
    /*
    *  ChannelHandLerContext ctx：上下文对象 ，含有 管道，通道，地址
    *  Object msg：就是客户端发送的数据 默认Object
    * */

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MyDataInfo.MyMessage msg) throws Exception {
        //根据 datatype 显示不同的信息
        MyDataInfo.MyMessage.DataType dataType = msg.getDataType();
        if(dataType == MyDataInfo.MyMessage.DataType.StudentType){
            MyDataInfo.Student student = msg.getStudent();
            System.out.println("id="+student.getId());
        }else if(dataType == MyDataInfo.MyMessage.DataType.WorkerType){
            MyDataInfo.Worker worker = msg.getWorker();
            System.out.println("名字"+worker.getName());
        }else{
            System.out.println("类型不正确");
        }
    }


    //数据读取完毕

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //writeAndFlush 是 write+flush
        //将数据写入到缓存，并刷新
        //一般讲，我们对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端",CharsetUtil.UTF_8));
    }

    //处理异常，一般是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}
