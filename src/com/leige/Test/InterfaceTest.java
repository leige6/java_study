package com.leige.Test;

/**
 * @author 张亚磊
 * @Description:
 * @date 2017/8/23  11:44
 */
public class InterfaceTest{
    public static void main(String[] args) {
       // AbstractSome s=new AbstractSome();
        Som s=new Som() {
            @Override
            public void excute() {

            }
            public void doService(){
                System.out.println("做一些服务");
            }

        };
        s.excute();
    }
}

interface  Som{
    void excute();
}

abstract class  AbstractSome implements Som{
   public abstract void  doSome();
    public void doService(){
        System.out.println("做一些服务");
    }
}