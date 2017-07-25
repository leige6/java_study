package com.leige.MultiTest;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 张亚磊
 * @Description: 线程模拟消费放入队列
 * @date 2017/7/21  9:38
 */
public class TakeAndPutQueen {
    private AtomicInteger count=new AtomicInteger(0);
    private final int minSize=0;
    private final int maxSize;
    private Object lock=new Object();
    private static LinkedList<Object> queen=new LinkedList<>();
    private  TakeAndPutQueen(int maxSize){
        this.maxSize=maxSize;
    }

    public  int getSize(){
        return  count.get();
    }

    public  void  put(Object a){
        synchronized (lock){
            while (count.get()==maxSize){
                try{
                    lock.wait();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            queen.add(a);
            count.incrementAndGet();
            lock.notify();
        }

    }

    public  Object  take(){
        Object ret =null;
        synchronized (lock) {
            while (count.get()==minSize){
                try{
                    lock.wait();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            ret = queen.removeFirst();
            count.decrementAndGet();
            lock.notify();
        }
        return ret;
    }

    public static void main(String[] args) throws Exception  {
        final TakeAndPutQueen takeAndPutQueen=new TakeAndPutQueen(5);
        takeAndPutQueen.put("a");
        takeAndPutQueen.put("b");
        takeAndPutQueen.put("c");
        takeAndPutQueen.put("d");
        takeAndPutQueen.put("e");
        System.out.println("当前容器长度:"+takeAndPutQueen.getSize());
        Thread t1=new Thread(new Runnable() {
            @Override
            public void run() {
                takeAndPutQueen.put("e");
                System.out.println("添加元素"+"e");
                takeAndPutQueen.put("f");
                System.out.println("添加元素"+"f");
            }
        });


        Thread t2=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                Thread.sleep(1000);
                Object r1=takeAndPutQueen.take();
                System.out.println("移除元素"+r1.toString());
                Thread.sleep(1000);
                Object r2=takeAndPutQueen.take();
                System.out.println("移除元素"+r2.toString());
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        Thread.sleep(1000);
        t2.start();
    }
}
