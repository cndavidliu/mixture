package com.mauvesu.mixture.facility.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

public class RedisUtil {
	
	public static Jedis generateSingle(Config config) {
		Jedis jedis = new Jedis(config.getHost(), config.getPort());
		jedis.auth(config.getAuth());
		return jedis;
	}

	public static ShardedJedisPool generatePool(Config config) {
		JedisShardInfo shard = new JedisShardInfo(config.getHost(), config.getPort(), 2000);
		shard.setPassword(config.getAuth());
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		shards.add(shard);

		GenericObjectPoolConfig jedisPoolConfig = new GenericObjectPoolConfig();
		jedisPoolConfig.setTestOnBorrow(false);
		jedisPoolConfig.setTestOnReturn(false);
		jedisPoolConfig.setMaxTotal(config.getMaxTotal());
		jedisPoolConfig.setMaxWaitMillis(1000 * 1);
		return new ShardedJedisPool(jedisPoolConfig, shards);
	}
	
	public static class Config {
		private String host;
		private int port;
		private String auth;
		private int maxTotal = 100;
		
		public Config(String host, int port, String auth) {
			this.host = host;
			this.port = port;
			this.auth = auth;
		}
		
		public void setMaxTotal(int maxTotal) {
			this.maxTotal = maxTotal;
		}
		
		public int getMaxTotal() {
			return this.maxTotal;
		}

		public String getHost() {
			return host;
		}

		public int getPort() {
			return port;
		}

		public String getAuth() {
			return auth;
		}
	}
}
