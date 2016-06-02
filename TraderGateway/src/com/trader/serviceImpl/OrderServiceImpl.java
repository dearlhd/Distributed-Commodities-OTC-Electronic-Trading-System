package com.trader.serviceImpl;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import com.trader.dao.OrderDao;
import com.trader.entity.Order;
import com.trader.service.MessagingService;
import com.trader.service.OrderService;

public class OrderServiceImpl implements OrderService {
	@Resource (name="orderDAO")
	OrderDao orderDao;

	@Resource (name="msgService")
	MessagingService msgService;
	
	public OrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	@Override
	public Order addOrder(Order order) {
		if (msgService == null) {
			System.out.println("msgService is null");
		}
		
		int brokerIndex = msgService.queryMartketPrice(order);
		JSONObject msg = JSONObject.fromObject(order);
		msgService.postOrderToBroker("/Order", brokerIndex, msg);
		
		return orderDao.addOrder(order);
	}
	
	
}
