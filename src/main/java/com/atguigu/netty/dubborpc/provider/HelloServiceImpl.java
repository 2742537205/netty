package com.atguigu.netty.dubborpc.provider;

import com.atguigu.netty.dubborpc.publicinterface.HelloService;

public class HelloServiceImpl implements HelloService {

    //当前消费方调用该方法时，就返回一个结果
    @Override
    public String hello(String mes) {
        System.out.println("收到客户端消息="+mes);
        //根据mes 返回不同的结果
        if(mes !=null){
            return "你还客户端";
        }else{
            return "mes为null";
        }
    }

}
