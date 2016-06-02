package com.trader.serviceImpl;

import java.util.List;

import javax.annotation.Resource;

import com.trader.entity.Order;
import com.trader.service.RedisService;
import com.trader.utils.jedis.RedisClient;

public class RedisServiceImpl implements RedisService{
	@Resource
	private RedisClient redisClient;
	
	@Override
	public void setOrderBook(String key, List<Order> orders) {
		redisClient.setOrderBook(key, orders);
	}

	@Override
	public List<Order> getOrderBook(String key) {
		return redisClient.getOrderBook(key);
	}

}
