package com.atguigu.nio;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 说明： MappedByteBuffer 可以让文件直接在内存(堆外内存)修改，操作系统不需要拷贝一次
 * */
public class MappedByteBufferTest {
    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("f:\\file01.txt","rw");

        //获取对应的通道
        FileChannel channel = randomAccessFile.getChannel();

        /**
         *  参数1： FIleChannel.MapMode.READ_WRITE 使用的读写模式
         *  参数2: 0：可以直接修改的起始位置
         *  参数3： 5 ：是映射到内存的大小(不是索引位置)，即将 file01.txt 的多少个字节映射到内存
         *  可以直接修改的范围就是 0-5
         *  实际类型 DirectByteBuffer
         * */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE,0,5);

        //将索引0的字节改成 H
        mappedByteBuffer.put(0,(byte) 'H');

        randomAccessFile.close();
    }
}
