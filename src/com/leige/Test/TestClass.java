package com.leige.Test;

/**
 * @author 张亚磊
 * @Description:
 * @date 2017/8/23  9:28
 */
public class TestClass {

    static {
        System.out.println("我是初始化static{}");
    }

    {
        System.out.println("我是初始化{}");
    }
    TestClass(){
        System.out.println("无参构造函数");
    }
    TestClass(int a){
        System.out.println("有参构造函数");
    }

    public static void main(String[] args) {
        TestClass t=new TestClass();
    }

}
