package com.broker.serviceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

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

	@Override
	public List<Order> queryOrderByCondition(JSONObject conds) {
		List<String> keys = redisClient.getKeys("Broker!");
		List<Order> orders = redisClient.getAllOrder(keys);
		
		if (conds.containsKey("trader")) {
			for (int i = 0; i < orders.size(); i++) {
				if (orders.get(i).getTrader() != conds.getString("trader")) {
					orders.remove(i);
					i--;
				}
			}
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
