package Mytest;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 张亚磊
 * @Description:
 * @date 2018/1/12  16:19
 */
public class ReentrantLockTest {
    private ReentrantLock lock=new ReentrantLock();
    private Condition aCondition=lock.newCondition();
    private Condition bCondition=lock.newCondition();



    class Produce implements Runnable{
        @Override
        public void run() {
            while (true){
                try {
                    lock.lock();
                    System.out.println("生产者生产一个产品");
                    Thread.sleep(1000);
                    bCondition.signal();
                    aCondition.await();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
            }
        }
    }

    class Consumer implements Runnable{
        @Override
        public void run() {
            while (true){
                try {
                    lock.lock();
                    System.out.println("消费者消费一个产品");
                    Thread.sleep(1000);
                    aCondition.signal();
                    bCondition.await();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
            }
        }
    }

    public static void main(String[] args) {
        ReentrantLockTest reentrantLockTest=new ReentrantLockTest();
        Thread produce=new Thread(reentrantLockTest.new Produce());
        Thread consumer=new Thread(reentrantLockTest.new Consumer());
        produce.start();
        consumer.start();
    }

}
