package com.trader.utils.jedis;

import java.util.ArrayList;
import java.util.List;



import com.trader.entity.Order;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisClient {
	private Jedis jedis;
	private JedisPool jedisPool;
	private ShardedJedis shardedJedis;
	private ShardedJedisPool shardedJedisPool;

	public RedisClient() {
		initPool();
		initShardedPool();
		shardedJedis = shardedJedisPool.getResource();
		jedis = jedisPool.getResource();
		// jedis.flushDB();
	}

	private void initPool() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(5);
		config.setTestOnBorrow(false);

		jedisPool = new JedisPool(config, "127.0.0.1", 6379);
	}

	private void initShardedPool() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(5);
		config.setTestOnBorrow(false);
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo("127.0.0.1", 6379, "master"));

		shardedJedisPool = new ShardedJedisPool(config, shards);
	}

	public void setOrderBook(String key, List<Order> orders) {
		jedis.set(key.getBytes(), SerializeUtil.serialize(orders));
	}

	public List<Order> getOrderBook(String key) {
		return (List<Order>) SerializeUtil.unserialize(jedis.get(key.getBytes()));
	}

}
