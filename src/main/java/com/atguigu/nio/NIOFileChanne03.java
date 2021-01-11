
/**
 *
 * 通道和通道之间的数据复制
 * */

package com.atguigu.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class NIOFileChanne03 {
    public static void main(String[] args) throws IOException {
        //创建相关流
        FileInputStream fileInputStream = new FileInputStream("f:\\a.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("f:\\a.jpg");

        //获取各个流对应的fileChannel
        FileChannel sourceCh = fileInputStream.getChannel();
        FileChannel destCh = fileOutputStream.getChannel();

        //使用transferForm完成拷贝
        destCh.transferFrom(sourceCh,0,sourceCh.size());

        //关闭相关通道和流
        sourceCh.close();
        destCh.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
