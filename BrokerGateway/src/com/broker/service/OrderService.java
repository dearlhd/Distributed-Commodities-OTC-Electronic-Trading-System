package com.broker.service;

import java.util.List;

import net.sf.json.JSONObject;

import com.broker.entity.Order;

public interface OrderService {
	public double dealMarketOrder (Order order);
	public double dealLimitOrder (Order order);
	public void dealStopOrder (Order order);
	public void dealCancelOrder (Order order);
	
	public void addStopOrder (Order order);
	
	public void addOrder(Order order);
	
	public List<Order> getOrderBook(Order order);
	public void printOrderBook(Order order, int side);
	
	public List<Order> queryOrderByConditions(JSONObject conds);
}
