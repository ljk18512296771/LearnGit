package com.jt.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

public class TestRedisShards {
	
	//思考:key=shards  存储到了哪台redis中? 如何存储的?
	@Test
	public void test01() {
		//1.准备list集合  之后添加节点信息
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo("192.168.126.129", 6379));
		shards.add(new JedisShardInfo("192.168.126.129", 6380));
		shards.add(new JedisShardInfo("192.168.126.129", 6381));
		//2.创建分片对象  改API中包含了hash算法.
		ShardedJedis shardedJedis = new ShardedJedis(shards);
		shardedJedis.set("shards", "准备分片操作!!!!!");
		System.out.println(shardedJedis.get("shards"));
	}
	
}
