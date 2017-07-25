package com.leige.MasterAndWork;

import java.util.HashMap;
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
}
