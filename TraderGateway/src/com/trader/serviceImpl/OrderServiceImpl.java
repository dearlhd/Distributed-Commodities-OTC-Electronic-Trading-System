package com.trader.serviceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
		int brokerIndex = msgService.queryMartketPrice(order);
		JSONObject msg = JSONObject.fromObject(order);
		msgService.postOrderToBroker("/Order", brokerIndex, msg);
		
		return orderDao.addOrder(order);
	}

	@Override
	public List<Order> queryOrderByConditions(JSONObject conds) {
		List<Order> orders = new ArrayList<Order>();
		if (conds.containsKey("trader")) {
			orders = orderDao.getOrderByUser(conds.getString("trader"));
		}
		else {
			orders = orderDao.getOrders();
		}
		
		String startTime = conds.getString("startTime");
		String endTime = conds.getString("endTime");
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date d1 = df.parse(startTime);
			Date d2 = df.parse(endTime);
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i = 0; i < orders.size(); i++) {
				Order order = orders.get(i);
				Date date = df.parse(order.getOrderTime());
				if (!(date.getTime() >= d1.getTime() && date.getTime() <= d2.getTime())) {
					orders.remove(i);
					i--;
				}
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return orders;
	}
	
}
