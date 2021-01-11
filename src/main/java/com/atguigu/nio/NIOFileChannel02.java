
/**
 *  将本地文件写入到程序中
 * */

package com.atguigu.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel02 {
    public static void main(String[] args) throws IOException {

        //创建文件的输入流
        File file = new File("f:\\file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        //通过fileInputStream 获取对应的 FileChannel -> 实际类型 FileChannelImpl
        FileChannel fileChannel = fileInputStream.getChannel();

        //创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)file.length());

        //将通道的数据读入到Buffer中
        fileChannel.read(byteBuffer);

        //将ByteBuffer的字节数据转成 String
        System.out.println(byteBuffer.array());
        fileInputStream.close();

    }
}
