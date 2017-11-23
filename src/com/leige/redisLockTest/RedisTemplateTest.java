package com.leige.redisLockTest;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.itech.ups.app.redis.service.RedisUtilService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:config/spring/applicationContext*.xml"})
public class RedisTemplateTest {
	@Resource
	private RedisUtilService redisUtilService;
	
	@Test  
    public  void getList(){ 
    	List<Object> productTrust=(List<Object>) redisUtilService.getCacheObject("id_2017032914160734890232");
    	if(productTrust != null && productTrust.size()>0){
			System.out.println("查询到了");
		}
	}
  
	@Test  
    public  void setExpire(){
		redisUtilService.expire("id_2017032914160734890232", 100, TimeUnit.SECONDS);
	}
	

}
