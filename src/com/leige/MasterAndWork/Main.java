package com.leige.MasterAndWork;

import java.util.Random;

/**
 * @author 张亚磊
 * @Description:
 * @date 2017/7/25  13:28
 */
public class Main {
    public static void main(String[] args) {
        int total=Runtime.getRuntime().availableProcessors();
        System.out.println("可用线程数为:"+total);
       Master master=new Master(new Work(),50);
        Random r=new Random();
        for (int i = 1; i <=100; i++) {
            Task t=new Task();
            t.setId(i);
            t.setName("任务"+i);
            t.setPrice(r.nextInt(1000));
            master.submit(t);
        }
        System.out.println("任务执行开始........");
       master.excute();
        long start=System.currentTimeMillis();
        while (true){
              if(master.isComplete()){
                  int result=master.getResult();
                  long end=System.currentTimeMillis()-start;
                  System.out.println("任务执行完毕，结果:"+result+"共耗时:"+end+"ms");
                  break;
              }
        }

    }
}
