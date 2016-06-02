package com.trader.service;

import java.util.List;

import com.trader.entity.Order;

public interface RedisService {
	public void setOrderBook(String key, List<Order> orders);
	public List<Order> getOrderBook(String key);
}
