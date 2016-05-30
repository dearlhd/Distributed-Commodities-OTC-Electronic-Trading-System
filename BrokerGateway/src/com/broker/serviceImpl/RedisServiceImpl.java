package com.broker.serviceImpl;

import java.util.List;

import javax.annotation.Resource;

import com.broker.entity.Order;
import com.broker.service.RedisService;
import com.broker.utils.jedis.RedisClient;

public class RedisServiceImpl implements RedisService{
	@Resource
	private RedisClient redisClient;
	
	@Override
	public void setOrderList(String key, List<Order> orders) {
		redisClient.setOrderList(key, orders);
	}

	@Override
	public List<Order> getOrderList(String key) {
		return redisClient.getOrderList(key);
	}

}
