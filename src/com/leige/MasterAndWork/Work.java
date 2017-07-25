package com.leige.MasterAndWork;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author 张亚磊
 * @Description: Work
 * @date 2017/7/25  11:04
 */
public class Work implements  Runnable {

    private ConcurrentLinkedQueue<Task> taskQueen;
    private ConcurrentHashMap<String, Object> resultMap;

    public void setTaskQueen(ConcurrentLinkedQueue<Task> taskQueen) {
        this.taskQueen=taskQueen;
    }

    public void setResultMap(ConcurrentHashMap<String, Object> resultMap) {
        this.resultMap=resultMap;
    }
    @Override
    public void run() {
       while (true){
          Task input=taskQueen.poll();
          if(input==null)  break;
         //处理业务
           Object output= handle(input);
           this.resultMap.put(Integer.toString(input.getId()),output);
       }
    }

    private Object handle(Task input) {
        Object output=null;
        try {
            //表示处理任务的时间
            Thread.sleep(500);
            output=input.getPrice();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return output;
    }


}
