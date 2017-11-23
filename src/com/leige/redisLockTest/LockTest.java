package com.leige.redisLockTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:config/spring/applicationContext*.xml"})
public class LockTest {
    //不加锁
    static class Outputer {
    	//RedisLock redisLock = new RedisLock("redisLock");
        public void output(String name) {
        	  //上锁
          //  redisLock.lock();
            try {
                for(int i=0; i<name.length(); i++) {
                    System.out.print(name.charAt(i));
                }
                System.out.println();
            }finally{
                //任何情况下都要释放锁
              //  redisLock.unlock();
            }  
        }
    }
    
    @Test 
    public void test1() {
        final Outputer output = new Outputer();
        //线程1打印zhangsan
        new Thread(new Runnable(){
            @Override
            public void run() {
                while(true) {
                     try{
                         Thread.sleep(1000);
                     }catch(InterruptedException e) {
                         e.printStackTrace();
                     }
                     output.output("zhangsan");
                }  
            }
        }).start();
         
        //线程2打印lingsi
        new Thread(new Runnable(){
            @Override
            public void run() {
                while(true) {
                     try{
                         Thread.sleep(1000);
                     }catch(InterruptedException e) {
                         e.printStackTrace();
                     }
                     output.output("lingsi");
                }
            }
        }).start();
         
        //线程3打印wangwu
        new Thread(new Runnable(){
            @Override
            public void run() {
                while(true) {
                     try{
                         Thread.sleep(1000);
                     }catch(InterruptedException e) {
                         e.printStackTrace();
                     }
                     output.output("huangwu");
                }
            }
        }).start();
    }
}