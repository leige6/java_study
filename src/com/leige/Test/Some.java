package com.leige.Test;

/**
 * @author 张亚磊
 * @Description:
 * @date 2017/8/23  10:34
 */
public class Some {
    Some(){
        this(10);
        System.out.println("Some()");
    }

    Some(int x){
        System.out.println("Some(x)");
    }
}

class Other extends  Some{
    Other(){
        this(10);
        System.out.println("Other()");
    }

    Other(int y){
        System.out.println("Other(y)");
    }

    public static void main(String[] args) {
       new Other();
    }
}