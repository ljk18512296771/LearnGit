package com.jt.test;

import org.junit.jupiter.api.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.params.SetParams;

public class TestRedis {
	
	/**
	 * 1.spring整合redis
	 * 报错说明:
	 * 	1).如果测试过程中报错 则检查redis配置文件  改3处
	 *  2).检查redis启动方式   redis-server redis.conf
	 *  3).检查Linux的防火墙
	 * 	做完的 测试其他命令.
	 */
	@Test
	public void testString01() {
		
		//1.创建jedis对象
		Jedis jedis = new Jedis("192.168.126.129", 6379);
		//2.操作redis
		jedis.set("a", "redis入门案例");
		String value = jedis.get("a");
		System.out.println(value);
	}
	
	@Test
	public void testString02() {
		
		//1.创建jedis对象
		Jedis jedis = new Jedis("192.168.126.129", 6379);
		//2.判断当前数据是否存在
		if(jedis.exists("a")) {
			System.out.println(jedis.get("a"));
		}else {
			
			jedis.set("a", "测试是否存在的方法");
		}
		
	}
	
	/**
	 * 1.能否简化是否存在的判断
	 * 2.如果该数据不存在时修改数据,否则不修改
	 * setnx方法:  只有当数据不存在时赋值.
	 */
	@Test
	public void testString03() {
	
		//1.创建jedis对象
		Jedis jedis = new Jedis("192.168.126.129", 6379);
		jedis.flushAll(); //清空所有的redis缓存
		jedis.setnx("a", "测试setnx方法1");
		jedis.setnx("a", "测试setnx方法2");
		System.out.println(jedis.get("a"));
	}
	
	/**
	 * 为数据添加超时时间
	 * @throws InterruptedException 
	 * setex方法  保证赋值操作和添加超时时间的操作的原子性
	 * 原子性: 要么同时成功,要么同时失败(类似事务)
	 */
	@Test
	public void testString04() throws InterruptedException {
	
		//1.创建jedis对象
		Jedis jedis = new Jedis("192.168.126.129", 6379);
		jedis.flushAll(); //清空所有的redis缓存
		
		jedis.set("a", "aaaa"); //如果程序报错,则超时方法将不会执行,改数据将永不超时
		//程序报错,意外终止!!!!!!!
		jedis.expire("a", 20);	//添加超时时间    不是原子性操作
		Thread.sleep(2000);
		System.out.println("剩余存活时间:"+jedis.ttl("a"));
		
		//2.实现原子性操作
		jedis.setex("b", 20, "原子性测试");
		System.out.println(jedis.get("b"));
		
	}
	
	
	/**
	 *
	 * 1.只有数据不存在时允许修改
	 * 2.要求实现添加超时时间,并且是原子性操作
	 * SetParams 参数说明:
	 * 	1.NX   只有key不存在时才能修改
	 * 	2.XX   只有key存在时,才能修改
	 *  3.PX   添加的时间单位是毫秒
	 *  4.EX   添加的时间单位是秒
	 */
	@Test
	public void testString05(){
	
		//1.创建jedis对象
		Jedis jedis = new Jedis("192.168.126.129", 6379);
		jedis.flushAll(); //清空所有的redis缓存
		SetParams params = new SetParams();
		params.xx().ex(20);
		jedis.set("aa", "测试A", params);	
		jedis.set("aa", "测试B", params);	
		System.out.println(jedis.get("aa"));
	}
	
	
	
	/**
	 * 存储一类数据时,可以使用hash.
	 */
	@Test
	public void testHASH(){
		//1.创建jedis对象
		Jedis jedis = new Jedis("192.168.126.129", 6379);
		jedis.flushAll(); //清空所有的redis缓存
		jedis.hset("user", "name", "tomcat");
		jedis.hset("user", "id", "100");
		System.out.println(jedis.hgetAll("user"));
	}
	
	
	@Test
	public void testList(){
		//1.创建jedis对象
		Jedis jedis = new Jedis("192.168.126.129", 6379);
		jedis.flushAll(); //清空所有的redis缓存
		jedis.lpush("list", "1","2","3","4");
		System.out.println(jedis.rpop("list"));
		
	}
	
	//控制事务
	@Test
	public void testTx(){
		//1.创建jedis对象
		Jedis jedis = new Jedis("192.168.126.129", 6379);
		jedis.flushAll(); //清空所有的redis缓存
		
		Transaction transaction = jedis.multi();//2.开启事务
		try {
			transaction.set("aaa", "aaa");
			transaction.set("bbb", "bbbbb");
			transaction.set("ccc", "cccccc");
			transaction.exec();			//事务提交
		} catch (Exception e) {
			e.printStackTrace();
			transaction.discard();		//事务回滚
		}
	}
}
