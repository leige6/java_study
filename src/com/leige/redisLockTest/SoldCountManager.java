package com.leige.redisLockTest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.itech.ups.app.redis.service.RedisUtilService;


public class SoldCountManager {

	
	
	private RedisUtilService redisUtilService;
	
	private final static Logger logger = LoggerFactory.getLogger(SoldCountManager.class);
	/**
	 * @throws ServiceException 
	 * 
	 * 雷------2016年6月17日
	 * 
	 * @Title: checkSoldCountByRedisDate
	 * @Description: 抢购的计数处理（用于处理超卖）
	 * @param @param key 购买计数的key
	 * @param @param limitCount 总的限购数量
	 * @param @param buyCount 当前购买数量
	 * @param @param endDate 抢购结束时间
	 * @param @param lock 锁的名称与unDieLock方法的lock相同
	 * @param @param expire 锁占有的时长（毫秒）
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	private int checkSoldCountByRedisDate(int type,String key, int limitCount, int buyCount, Date endDate, String lock, int expire) {
		int res = 0;
		if (redisUtilService.lock(lock, expire)) {
			Integer soldCount = (Integer) redisUtilService.getCacheObject(key);
            if(soldCount!=null&&soldCount>=100){
            	res=5;
			}else{
				Integer totalSoldCount = (soldCount == null ? 0 : soldCount) + buyCount;
    			if (totalSoldCount <limitCount) {
    				switch (type) {
    				case 1:
    					System.err.println("用户1抢购成功，抢购数量:"+buyCount);
    					break;
                    case 2:
                    	System.err.println("用户2抢购成功，抢购数量:"+buyCount);
    					break;
                    case 3:
                    	System.err.println("用户3抢购成功，抢购数量:"+buyCount);
    					break;
    				default:
    					break;
    				}
    				redisUtilService.setCacheObject(key, totalSoldCount, diffDateTime(endDate, new Date()));
    				res = 1;
    				System.err.println("商品累计销售数量:"+totalSoldCount);
    			}else if(totalSoldCount>limitCount&&soldCount<100){
    				res = 4;
    			}
			}
			redisUtilService.remove(lock);
		} else {
			if (redisUtilService.unDieLock(lock)) {
				logger.info("解决了出现的死锁");
				res = 2;
			} else {
				res = 3;
				System.err.println("活动太火爆啦,请稍后重试!");
			}
		}
		return res;
	}

	public static void main(String[] args) {
		SoldCountManager soldCountManager=new SoldCountManager();
		try {
			soldCountManager.rushToPurchase();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	 /** 
	* @Title: rushToPurchase  
	* @Description: 抢购测试
	* @param    
	* @return void
	 * @throws ParseException 
	* @throws 
	* @author 张亚磊
	* @date 2017年5月26日 下午1:43:48
	*/
 
	public void rushToPurchase() throws ParseException{ 
		ApplicationContext  ctx =  new ClassPathXmlApplicationContext("classpath:config/spring/applicationContext*.xml");
		redisUtilService = (RedisUtilService)ctx.getBean("redisUtilService");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final Date endDate = sdf.parse("2017-05-31 19:00:00");
		final String key="SoldCount"; //已经出售数量
		final int limitCount=100;
		final String lock="LOCK";
		final int timeout=30;
		
		
		//用户1抢购
        new Thread(new Runnable(){
            @Override
            public void run() {
            	int total1=0;
            	boolean flag=true;
                while(flag) {
                     try{
                     Random r2 = new Random();
                     Thread.sleep(r2.nextInt(1000));	 
                     Random r1 = new Random();
                     int buyCount=r1.nextInt(10)+1; //购买量区间是1-10
                     int res=checkSoldCountByRedisDate(1,key, limitCount, buyCount, endDate, lock, timeout);
                     switch (res) {
					case 0:
					case 2:
					case 3:
					    break;
					case 4:
						System.err.println("用户1此次购买:"+buyCount+"购买数量大于可售数量，请重试！");
						break;
					case 1:
						total1+=buyCount;
						break;
					case 5:
						flag=false;
						System.err.println("商品已经售罄，用户1结束购买！");
                     	System.err.println("用户1累计抢购数量:"+total1);
						break;
					default:
						break;
					}
                     Thread.sleep(r2.nextInt(1000));
                      }catch(InterruptedException e){
                          e.printStackTrace();
                     }
                }  
            }
        }).start();
         
        //用户2抢购
        new Thread(new Runnable(){
            @Override
            public void run() {
                int total2=0;
                boolean flag=true;
                while(flag) {
                     try{
                     Random r2 = new Random();
                     Thread.sleep(r2.nextInt(1000));	 
                     Thread.sleep(1000);
                     Random r1 = new Random();
                    int buyCount=r1.nextInt(10)+1; //购买量区间是1-10
                    int res=checkSoldCountByRedisDate(2,key, limitCount, buyCount, endDate, lock, timeout);
                    switch (res) {
					case 0:
					case 2:
					case 3:
						break;
					case 4:
						System.err.println("用户2此次购买:"+buyCount+"购买数量大于可售数量，请重试！");
						break;
					case 1:
						total2+=buyCount;
						break;
					case 5:
						flag=false;
						System.err.println("商品已经售罄，用户2结束购买！");
                    	System.err.println("用户2累计抢购数量:"+total2);
						break;
					default:
						break;
					}
                    Thread.sleep(r2.nextInt(1000));
                     }catch(InterruptedException e) {
                         e.printStackTrace();
                     }
                }  
            }
        }).start();
        
        //用户3抢购
        new Thread(new Runnable(){
            @Override
            public void run() {
                int total3=0;
                boolean flag=true;
                while(flag) {
                     try{
                     Random r2 = new Random();
                     Thread.sleep(r2.nextInt(1000));
                     Random r1 = new Random();
                    int buyCount=r1.nextInt(10)+1; //购买量区间是1-10
                    int res=checkSoldCountByRedisDate(3,key, limitCount, buyCount, endDate, lock, timeout);
                    switch (res) {
					case 0:
					case 2:
					case 3:
						break;
					case 4:
						System.err.println("用户3此次购买:"+buyCount+"购买数量大于可售数量，请重试！");
						break;
					case 1:
						total3+=buyCount;
						break;
					case 5:
						flag=false;
						System.err.println("商品已经售罄，用户3结束购买！");
                    	System.err.println("用户3累计抢购数量:"+total3);
						break;
					default:
						break;
					}
                    Thread.sleep(r2.nextInt(1000));
                     }catch(InterruptedException e) {
                         e.printStackTrace();
                     }
                }  
            }
        }).start();
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
	    public  long diffDateTime(Date date, Date date1) {
	        return (Long) ((getMillis(date) - getMillis(date1))/1000);
	    }
	    
	    
	    public  long getMillis(Date date) {
	        Calendar c = Calendar.getInstance();
	        c.setTime(date);
	        return c.getTimeInMillis();
	    }
}
