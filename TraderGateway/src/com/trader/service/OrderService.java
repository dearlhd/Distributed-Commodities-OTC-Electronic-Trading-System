package com.trader.service;

import java.util.List;

import net.sf.json.JSONObject;

import com.trader.entity.Order;

public interface OrderService {
	public Order addOrder(Order order);
	public List<Order> queryOrderByConditions(JSONObject conds);
}
