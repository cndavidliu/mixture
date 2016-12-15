package com.mauvesu.mixture.facility.distribution.lock;

import static com.mauvesu.mixture.facility.util.RedisUtil.*;

import java.util.concurrent.TimeUnit;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * implement distributed lock using redis setnx and expire(or getset) 
 * 
 * @author mauvesu
 *
 */
public class RedisLock {
	
	private ShardedJedisPool pool;
	private int expire = 10;
	private int interval = 100;
	
	public RedisLock(Config config) {
		this.pool = generatePool(config);
	}

	public void lock(String name) throws InterruptedException {	
		ShardedJedis jedis = null;
		try {
			jedis = this.pool.getResource();
			while (true) {
				long result = jedis.setnx(name, String.valueOf(System.currentTimeMillis()));
				if (result == 1) {
					jedis.expire(name, expire);
					return;
				}
				Thread.sleep(interval);
			}
		} finally {
			if (jedis != null)
				jedis.close();
		}
		
	}

	public boolean tryLock(String name) {
		ShardedJedis jedis = null;
		try {
			jedis = this.pool.getResource();
			long result = jedis.setnx(name, String.valueOf(System.currentTimeMillis()));
			if (result == 1) {
				jedis.expire(name, this.expire);
				return true;
			}
			return false;
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}

	public void unlock(String name) {
		ShardedJedis jedis = null;
		try {
			jedis = this.pool.getResource();
			jedis.del(name);
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}


	public boolean tryLock(String name, long time, TimeUnit unit) throws InterruptedException {
		ShardedJedis jedis = null;
		try {
			jedis = this.pool.getResource();
			long limit = System.nanoTime() + unit.toNanos(time);
			long cInterval = interval > unit.toMillis(time) ? unit.toMillis(time) / 2 : interval;
			while (System.nanoTime() <= limit) {
				long result = jedis.setnx(name, String.valueOf(System.currentTimeMillis()));
				if (result == 1) {
					jedis.expire(name, expire);
					return true;
				}
				Thread.sleep(cInterval);
			}
			return false;
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}
	
	public void setExpire(int expire) {
		this.expire = expire;
	}
	
	public int getExpire() {
		return this.expire;
	}
}
