package com.atguigu.netty.protocoltcp;

//协议包
public class MessageProtocol {

    private int len;
    private byte[] context;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContext() {
        return context;
    }

    public void setContext(byte[] context) {
        this.context = context;
    }
}
