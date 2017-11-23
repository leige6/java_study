package com.leige.redisLockTest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import com.itech.ups.app.redis.service.RedisUtilService;

@Service("redisUtilService")
public class RedisUtilServiceImpl implements RedisUtilService{
   
	@Autowired 
    @Qualifier("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    @Qualifier("redisTemplate")
    protected RedisTemplate<Serializable, Serializable> redisTemplateSerializable;
    
    
    /** 
    * @Title: setCacheObject  
    * @Description: 写入缓存
    * @param @param key 缓存的键值
    * @param @param value  缓存的对象
    * @param @return   
    * @throws 
    * @author 张亚磊
    * @date 2017年5月25日 上午11:30:00
    */
    public boolean setCacheObject(String key, Object value)
    {
        return setCacheObject(key,value,null);
    }
    
    
    /** 
    * @Title: setCacheObject 
    * @Description: 写入带有效期的缓存
    * @param @param key 缓存的键值
    * @param @param value 缓存的对象
    * @param @param expireTime 键值对缓存的时间，单位是秒
    * @param @return   
    * @throws 
    * @author 张亚磊
    * @date 2017年5月25日 上午11:30:41
    */
    public boolean setCacheObject(String key, Object value, Long expireTime)
    {
    	 boolean result = false;  
         try {  
        	 redisTemplate.opsForValue().set(key,value);
             if (expireTime != null) {  
                 redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);  
             }  
             result = true;  
         } catch (Exception e) {  
             e.printStackTrace();  
         }  
         return result;  
    }
    
    /** 
    * @Title: getCacheObject
    * @Description: 获得缓存的基本对象。
    * @param @param key  缓存键值
    * @param @return   缓存键值对应的数据
    * @throws 
    * @author 张亚磊
    * @date 2017年5月25日 上午11:29:15
    */
    public Object getCacheObject(String key)
    {
    	Object result = null; 
    	result=redisTemplate.opsForValue().get(key);
        return result;
    }
    
    /** 
    * @Title: remove  
    * @Description: 删除对应key的value 
    * @param @param key   
    * @return void
    * @throws 
    * @author 张亚磊
    * @date 2017年5月25日 上午11:43:31
    */
    public void remove(final String key) {  
        if (exists(key)) {  
            redisTemplate.delete(key);  
        }  
    }  
    
    /** 
    * @Title: exists  
    * @Description: 判断缓存是否存在 
    * @param @param key
    * @param @return   
    * @return boolean
    * @throws 
    * @author 张亚磊
    * @date 2017年5月25日 上午11:42:57
    */
    public boolean exists(final String key) {  
        return redisTemplate.hasKey(key);  
    }  
    
    
    /**
     * 缓存List数据
     * @param key        缓存的键值
     * @param dataList    待缓存的List数据
     * @return            缓存的对象
     */
    public Object setCacheList(String key, List<Object> dataList)
    {
        ListOperations<String, Object> listOperation = redisTemplate.opsForList();
        if(null != dataList)
        {
            int size = dataList.size();
            for(int i = 0; i < size ; i ++)
            {
                listOperation.rightPush(key,dataList.get(i));
            }
        }
        return listOperation;
    }
    
    /**
     * 获得缓存的list对象
     * @param key    缓存的键值
     * @return        缓存键值对应的数据
     */
    public List<Object> getCacheList(String key)
    {
        List<Object> dataList = new ArrayList<Object>();
        ListOperations<String, Object> listOperation = redisTemplate.opsForList();
        Long size = listOperation.size(key);
        
        for(int i = 0 ; i < size ; i ++)
        {
            dataList.add(listOperation.leftPop(key));
        }
        return dataList;
    }
    
    /**
     * @Title: range 
     * @Description: 获得缓存的list对象
     * @param @param key
     * @param @param start
     * @param @param end
     * @param @return    
     * @return List<T>    返回类型 
     * @throws
     */
    public List<Object> range(String key, long start, long end)
    {
        ListOperations<String, Object> listOperation = redisTemplate.opsForList();
        return listOperation.range(key, start, end);
    }
    
    
    /** 
    * @Title: listSize  
    * @Description: 获得缓存list的长度
    * @param @param key
    * @param @return   
    * @throws 
    * @author 张亚磊
    * @date 2017年5月25日 上午11:46:28
    */
    public Long listSize(String key) {  
        return redisTemplate.opsForList().size(key);  
    }  
    
    /** 
    * @Title: listSet  
    * @Description: 覆盖操作,将覆盖List中指定位置的值
    * @param @param key
    * @param @param index
    * @param @param obj   
    * @throws 
    * @author 张亚磊
    * @date 2017年5月25日 上午11:46:47
    */
    public void listSet(String key, int index, Object obj) {
        redisTemplate.opsForList().set(key, index, obj);
    }
        
  
    /** 
    * @Title: leftPush  
    * @Description:  向List尾部追加记录 
    * @param @param key
    * @param @param obj
    * @param @return   
    * @throws 
    * @author 张亚磊
    * @date 2017年5月25日 上午11:48:48
    */
    public long leftPush(String key, Object obj) {
        return redisTemplate.opsForList().leftPush(key, obj);
    }

    
    /** 
    * @Title: rightPush  
    * @Description: 向List头部追加记录
    * @param @param key
    * @param @param obj
    * @param @return   
    * @throws 
    * @author 张亚磊
    * @date 2017年5月25日 上午11:49:01
    */
    public long rightPush(String key, Object obj) {
        return redisTemplate.opsForList().rightPush(key, obj);
    }
    
    /** 
    * @Title: trim  
    * @Description: 删除，只保留start与end之间的记录 
    * @param @param key
    * @param @param start 记录的开始位置(0表示第一条记录)
    * @param @param end   记录的结束位置（如果为-1则表示最后一个，-2，-3以此类推）
    * @throws 
    * @author 张亚磊
    * @date 2017年5月25日 上午11:49:24
    */
    public void trim(String key, int start, int end) {
        redisTemplate.opsForList().trim(key, start, end);
    }
    
    /** 
    * @Title: remove  
    * @Description:  删除List中i条记录，被删除的记录值为obj
    * @param @param key
    * @param @param i 要删除的数量，如果为负数则从List的尾部检查并删除符合的记录
    * @param @param obj 要匹配的值
    * @param @return   删除后的List中的记录数
    * @throws 
    * @author 张亚磊
    * @date 2017年5月25日 上午11:50:08
    */
    public long remove(String key, long i, Object obj) {
        return redisTemplate.opsForList().remove(key, i, obj);
    }
    
   
    /** 
    * @Title: setCacheSet  
    * @Description: 缓存Set
    * @param @param key 缓存键值
    * @param @param dataSet  缓存的数据
    * @param @return    缓存数据的对象
    * @throws 
    * @author 张亚磊
    * @date 2017年5月25日 上午11:51:20
    */
    public BoundSetOperations<String, Object> setCacheSet(String key,Set<Object> dataSet)
    {
        BoundSetOperations<String, Object> setOperation = redisTemplate.boundSetOps(key);    
        /*T[] t = (T[]) dataSet.toArray();
             setOperation.add(t);*/
        
        Iterator<Object> it = dataSet.iterator();
        while(it.hasNext())
        {
            setOperation.add(it.next());
        }
        return setOperation;
    }
    

    /** 
    * @Title: getCacheSet  
    * @Description: 获得缓存的set
    * @param @param key
    * @param @return   
    * @throws 
    * @author 张亚磊
    * @date 2017年5月25日 下午1:01:03
    */
    public Set<Object> getCacheSet(String key/*,BoundSetOperations<String,T> operation*/)
    {
        Set<Object> dataSet = new HashSet<Object>();
        BoundSetOperations<String,Object> operation = redisTemplate.boundSetOps(key);    
        
        Long size = operation.size();
        for(int i = 0 ; i < size ; i++)
        {
            dataSet.add(operation.pop());
        }
        return dataSet;
    }
    
   
    /** 
    * @Title: setCacheMap  
    * @Description:  缓存Map
    * @param @param key
    * @param @param dataMap
    * @param @return   
    * @throws 
    * @author 张亚磊
    * @date 2017年5月25日 下午1:01:18
    */
    public int setCacheMap(String key,Map<String, Object> dataMap)
    {
        if(null != dataMap)
        {
            HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
            for (Map.Entry<String, Object> entry : dataMap.entrySet()) {  
                /*System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());  */
                if(hashOperations != null){
                    hashOperations.put(key,entry.getKey(),entry.getValue());
                } else{
                    return 0;
                }
            } 
        } else{
            return 0;
        }
        return dataMap.size();
    }
    
    /** 
    * @Title: getCacheMap  
    * @Description: 获得缓存的Map
    * @param @param key
    * @param @return   
    * @throws 
    * @author 张亚磊
    * @date 2017年5月25日 下午1:01:59
    */
    public Map<Object, Object> getCacheMap(String key/*,HashOperations<String,String,T> hashOperation*/)
    {
        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
        /*Map<String, T> map = hashOperation.entries(key);*/
        return map;
    }
    
    /**
     * 缓存Map
     * @param key
     * @param dataMap
     * @return
     */
    public void setCacheIntegerMap(String key,Map<Integer, Object> dataMap)
    {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        if(null != dataMap)
        {
            for (Map.Entry<Integer, Object> entry : dataMap.entrySet()) {  
                /*System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());  */
                hashOperations.put(key,entry.getKey(),entry.getValue());
            } 
            
        }
    }
    
    /**
     * 获得缓存的Map
     * @param key
     * @param hashOperation
     * @return
     */
    public Map<Object, Object> getCacheIntegerMap(String key/*,HashOperations<String,String,T> hashOperation*/)
    {
        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
        /*Map<String, T> map = hashOperation.entries(key);*/
        return map;
    }
    
    /**
     * 从hash中删除指定的存储
     * 
     * @param String key
     * */
    public void deleteMap(String key, Object[] hashKeys) {
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.opsForHash().delete(key, hashKeys);
    }
    
    /** 
    * @Title: expire  
    * @Description: 对key设置过期时间 
    * @param @param key
    * @param @param time
    * @param @param unit
    * @param @return   
    * @throws 
    * @author 张亚磊
    * @date 2017年5月25日 下午1:06:10
    */
    public boolean expire(String key, long time, TimeUnit unit) {
        return redisTemplate.expire(key, time, unit);
    }
    

    /** 
    * @Title: increment  
    * @Description: TODO 
    * @param @param key
    * @param @param step
    * @param @return   
    * @throws 
    * @author 张亚磊
    * @date 2017年5月25日 下午1:06:49
    */
    public long increment(String key, long step) {
        return redisTemplate.opsForValue().increment(key, step);
    }

    
    /** 
    * @Title: del  
    * @Description: 删除对应key的value;
    * @param @param key
    * @param @return   
    * @throws 
    * @author 张亚磊
    * @date 2017年5月25日 下午1:09:43
    */
    public long del(final byte[] key) {  
        return (long) redisTemplateSerializable.execute(new RedisCallback<Object>() {  
            public Long doInRedis(RedisConnection connection) {  
                return connection.del(key);  
            }  
        });  
    }  

    /** 
    * @Title: get  
    * @Description: 获取对应key的value;
    * @param @param key
    * @param @return   
    * @throws 
    * @author 张亚磊
    * @date 2017年5月25日 下午1:10:10
    */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public byte[] get(final byte[] key) {
        return (byte[]) redisTemplateSerializable.execute(new RedisCallback() {
            public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.get(key);
            }
        });
    }
    
    
    /** 
    * @Title: set  
    * @Description: TODO 
    * @param @param key
    * @param @param value
    * @param @param liveTime   
    * @throws 
    * @author 张亚磊
    * @date 2017年5月25日 下午1:10:37
    */
    public void set(final byte[] key, final byte[] value, final long liveTime) {
        redisTemplateSerializable.execute(new RedisCallback<Object>() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.set(key, value);
                if (liveTime > 0) {
                    connection.expire(key, liveTime);
                }
                return 1L;
            }
        });
    }
    
   
    /** 
    * @Title: lock  
    * @Description: 加锁机制
    * @param @param lock 锁的名称
    * @param @param expire 锁占有的时长（毫秒）
    * @param @return   
    * @return Boolean 返回类型
    * @throws 
    * @author 张亚磊
    * @date 2017年5月26日 上午11:55:03
    */
    public Boolean lock(final String lock, final int expire) {
    	System.out.println(lock+"------------"+expire);
		return (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				boolean locked = false;
				/*redisTemplate.getValueSerializer()返回的是一个无限定通配符的RedisSerializer<?>，
				 * 无法调用有通配符参数的方法serialize(? t),可以先把它转化为原始的RedisSerializer类型即可
				 * RedisSerializer serializer= redisTemplate.getValueSerializer();
				 */
				RedisSerializer serializer= redisTemplate.getValueSerializer();
				@SuppressWarnings("unchecked")
				byte[] lockValue =serializer.serialize(getDateAddMillSecond(null, expire));
				byte[] lockName = serializer.serialize(lock);
				locked = connection.setNX(lockName, lockValue);
				if (locked)
					connection.expire(lockName, TimeoutUtils.toSeconds(expire, TimeUnit.MILLISECONDS));
				return locked;
			}

		});
	}
    
    
    
    /** 
    * @Title: unDieLock  
    * @Description: 处理发生的死锁
    * @param @param lock 是锁的名称
    * @param @return   
    * @return Boolean 返回类型
    * @throws 
    * @author 张亚磊
    * @date 2017年5月26日 下午1:24:37
    */
    public Boolean unDieLock(final String lock) {
		boolean unLock = false;
		Date lockValue = (Date) redisTemplate.opsForValue().get(lock);
		if (lockValue != null && lockValue.getTime() <= (new Date().getTime())) {
			redisTemplate.delete(lock);
			unLock = true;
		}
		return unLock;
	}  
    
    
    
    /** 
     * @Title: diffDateTime  
     * @Description: 日期相减(返回秒值) 
     * @param @param date
     * @param @param date1
     * @param @return   
     * @return Long
     * @throws 
     * @author 张亚磊
     * @date 2017年5月26日 上午11:35:24
     */
     private  Long diffDateTime(Date date, Date date1) {
         return (Long) ((getMillis(date) - getMillis(date1))/1000);
     }
     
     
     /** 
     * @Title: getMillis  
     * @Description: 获取 指定日期 的毫秒数
     * @param @param date
     * @param @return   
     * @return long
     * @throws 
     * @author 张亚磊
     * @date 2017年5月26日 上午11:36:09
     */
     private  long getMillis(Date date) {
         Calendar c = Calendar.getInstance();
         c.setTime(date);
         return c.getTimeInMillis();
     }
  	
     
 	/** 
 	* @Title: getDateAddMillSecond  
 	* @Description: 获取 指定日期 后 指定毫秒后的 Date
 	* @param @param date
 	* @param @param millSecond
 	* @param @return   
 	* @return Date
 	* @throws 
 	* @author 张亚磊
 	* @date 2017年5月26日 上午11:35:50
 	*/
     private  Date getDateAddMillSecond(Date date, int millSecond) {
 		Calendar cal = Calendar.getInstance();
 		if (null != date) {// 没有 就取当前时间
 			cal.setTime(date);
 		}
 		cal.add(Calendar.MILLISECOND, millSecond);
 		return cal.getTime();
 	}
}
