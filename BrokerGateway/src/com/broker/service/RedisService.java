package com.broker.service;

import java.util.List;

import net.sf.json.JSONObject;

import com.broker.entity.Order;

public interface RedisService {
	public void setOrderList(String key, List<Order> orders);
	public List<Order> getOrderList(String key);
	public List<Order> queryOrderByCondition(JSONObject conds);
}
