package com.trader.dao;

import java.util.List;

import com.trader.entity.Order;

public interface OrderDao {
	public Order addOrder(Order order);
	public List<Order> getOrders();
	public List<Order> getOrderByUser(String username);
	
	public Order getOrderByID(int id);
}
