package com.leige.MasterAndWork;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author 张亚磊
 * @Description: Master 调度器
 * @date 2017/7/25  11:04
 */
public class Master {
    /**
     * 存放任务的队列
     */
    private ConcurrentLinkedQueue<Task> taskQueen=new ConcurrentLinkedQueue<>();
    /**
     * 存放work的集合
     */
    private HashMap<String,Thread> works=new HashMap<>();
    /**
     *存放结果集合
     */
    private ConcurrentHashMap<String,Object> resultMap=new ConcurrentHashMap<String,Object>();

    public Master(Work work ,int workCount){
      for (int i=1;i<=workCount;i++){
          work.setTaskQueen(this.taskQueen);
          work.setResultMap(this.resultMap);
          //key是每一个子节点的名字，value是每一个子节点的实例
          works.put("子节点"+i,new Thread(work));
      }
    }
    /**
      * @Title: 
      * @Description: 提交任务方法
      * @param task
      * @author 张亚磊
      * @date 2017年07月25日 13:16:42
      */
    public  void  submit(Task task){
       this.taskQueen.add(task);
    }
    //需要有一个执行的方法
    public void excute() {
       for(Map.Entry<String,Thread> me : works.entrySet()){
            me.getValue().start();
       }
   }
    /**
      * @Title:
      * @Description: 判断线程是否执行完毕
      * @param 
      * @return 
      * @author 张亚磊
      * @date 2017年07月25日 13:33:51
      */
    public boolean isComplete() {
        for(Map.Entry<String,Thread> me : works.entrySet()){
            if(me.getValue().getState()!= Thread.State.TERMINATED){
               return  false;
            }
        }
        return  true;
    }

    public int getResult() {
        int ret=0;
        for(Map.Entry<String,Object> result : resultMap.entrySet()){
            ret+=(int)result.getValue();
        }
        return  ret;
    }
}
