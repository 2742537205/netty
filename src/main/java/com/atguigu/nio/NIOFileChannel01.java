





/**
 *
 *
 * 爱你宝宝 2020/12/8
 *
 * 这个类的作用：
 *              用于FileChannel通道向本地写入文件的案列
 * */





package com.atguigu.nio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel01 {
    public static void main(String[] args) throws IOException {
        String str = "hello 尚硅谷";

        //创建一个输出流 -> channel
        FileOutputStream fileOutputStream = new FileOutputStream("f:\\file01.txt");

        //通过 fileOutPutStream 获取对应的 FileChannel（文件通道）
        //这个 fileChannel 真实类型是 FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();

        //创建一个缓冲区 ByteBUffer 并指定 最大容量为 1024
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //将 str字符串转换为 byte并放入到 byteBuffer中
        byteBuffer.put(str.getBytes());

        //byteBuffer得到数据后，由于当前position的index为最大容量的index，所以需要用flip()方法进行初始化位置
        byteBuffer.flip();

        //将缓冲区中的数据写入到 FIleChannel 通道中
        fileChannel.write(byteBuffer);

        //关闭原生IO的文件输出流
        fileOutputStream.close();

    }
}
