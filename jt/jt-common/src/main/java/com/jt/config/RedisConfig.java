package com.jt.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

@Configuration 	//我是一个配置类    一般都会与@Bean联用
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {
	
	/**
	 * spring整合Redis集群
	 */
	@Value("${redis.nodes}")
	private String redisNodes;
	
	@Bean
	public JedisCluster jedisCluster() {
		Set<HostAndPort> nodeSet = new HashSet<HostAndPort>();
		String[] clusters = redisNodes.split(",");
		for (String cluster : clusters) {	//host:port
			String host = cluster.split(":")[0];
			int port = Integer.parseInt(cluster.split(":")[1]);
			nodeSet.add(new HostAndPort(host, port));
		}
		return new JedisCluster(nodeSet);
	}
	
	
	
	
	
	
	
	/**
	 * 
	@Value("${redis.nodes}")
	private String redisNodes;   //node,node,node
	 */
	/*整合分片实现Redis内存扩容*/
	/**
	@Bean
	public ShardedJedis shardedJedis() {
		String[] nodes = redisNodes.split(",");  //节点数组
		//动态获取Redis节点信息.
		List<JedisShardInfo> list = new ArrayList<JedisShardInfo>();
		for (String node : nodes) { //node= host:port ---->[host,port]
			String host = node.split(":")[0];
			int port = Integer.parseInt(node.split(":")[1]);
			list.add(new JedisShardInfo(host, port));
		}
		//返回分片对象
		return new ShardedJedis(list);
	}
	
	**/
	
	/**
	 * 单台测试
	
	@Value("${redis.host}")
	private String host;
	@Value("${redis.port}")
	private Integer port;
	
	//将返回值的结果交给spring容器进行管理,如果以后想要使用该对象则可以直接注入.
	@Bean
	public Jedis jedis() {
		
		return new Jedis(host, port);
	}
	 */
}
