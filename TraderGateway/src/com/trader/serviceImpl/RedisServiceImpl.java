package com.trader.serviceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import com.trader.entity.Order;
import com.trader.service.RedisService;
import com.trader.utils.jedis.RedisClient;

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

	@Override
	public List<Order> getLargeOrders() {
		String key = "Trader!LargeOrders";
		List<Order> orders = redisClient.getOrderList(key);
		return orders;
	}
}
