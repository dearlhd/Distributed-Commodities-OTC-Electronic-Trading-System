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
import com.trader.service.RedisService;

public class OrderServiceImpl implements OrderService {
	@Resource (name="orderDAO")
	OrderDao orderDao;

	@Resource (name="msgService")
	MessagingService msgService;
	
	@Resource
	RedisService redisService;
	
	public OrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	@Override
	public Order addOrder(Order order) {
		if (order.getOrderType().equals("large")) {
			String key = "Trader!LargeOrders";
			List<Order> orders = redisService.getOrderList(key);
			if (orders == null) {
				orders = new ArrayList<Order>();
			}
			orders.add(order);
			redisService.setOrderList(key, orders);
			return order;
		}
		
		if (order.getOrderType().equals("cancel")) {
			Order od = orderDao.getOrderByID(order.getOrderID());
			if (od == null) {
				return new Order();
			}
			int quantity = order.getQuantity();
			od.setQuantity(quantity);
			od.setOrderType("cancel");
			
			for (int i = 0; i < 3; i++) {
				JSONObject msg = JSONObject.fromObject(od);
				msgService.postOrderToBroker("/Order", i, msg);
			}
			orderDao.cancelOrder(order.getOrderID(), quantity);
			return order;
		}
		
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
		
		String startTime = conds.getString("startTime") + " 00:00:00";
		String endTime = conds.getString("endTime") + " 23:59:59";
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d1 = df.parse(startTime);
			Date d2 = df.parse(endTime);
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

	private double computePrice (Order order) {
		String key = "Trader!DealOrder:" + order.getProduct() + " " + order.getPeriod();
		List<Order> orders = redisService.getOrderList(key);
		if (orders == null || orders.size() == 0)
			return order.getPrice();
		
		double price = 0.0;
		int quantity = 0;
		for (int i = 0; i < orders.size(); i++) {
			price += orders.get(i).getPrice() * orders.get(i).getQuantity();
			quantity += orders.get(i).getQuantity();
		}
		
		
		return (int)(price / quantity);
	}
	
	private int computeQuantity(Order order) {
		String key = "Trader!DealOrder:" + order.getProduct() + " " + order.getPeriod();
		List<Order> orders = redisService.getOrderList(key);
		
		if (orders == null || orders.size() == 0)
			return 100;
		
		int quantity = 0;
		for (int i = 0; i < orders.size(); i++) {
			quantity += orders.get(i).getQuantity();
		}
		
		return quantity / orders.size() + 1;
	}
	
	@Override
	public void dealIceberg() {
		List<Order> orders = redisService.getLargeOrders();
		if (orders == null) {
			return;
		}
		
		String key = "Trader!LargeOrders";
		for (int i = 0; i < orders.size(); i++) {
			Order order = orders.get(i);
			double price = computePrice(order);
			int oldQuantity = order.getQuantity();
			int quantity = computeQuantity(order);
			
			if (quantity >= oldQuantity) {
				quantity = oldQuantity;
				oldQuantity = 0;
			}
			
			Order newOrder = new Order();
			newOrder.setProduct(order.getProduct());
			newOrder.setPeriod(order.getPeriod());
			newOrder.setSide(order.getSide());
			newOrder.setTrader(order.getTrader());
			newOrder.setTraderCompany(order.getTraderCompany());
			newOrder.setOrderType("limit");
			newOrder.setPrice(price);
			newOrder.setQuantity(quantity);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
			Date date = new Date();
			newOrder.setOrderTime(df.format(date));// new Date()为获取当前系统时间
			
			addOrder(newOrder);
			
			if (oldQuantity == 0) {
				orders.remove(i);
				i--;
			}
			else {
				order.setQuantity(oldQuantity - quantity);
			}
		}
		redisService.setOrderList(key, orders);
	}
	
}
