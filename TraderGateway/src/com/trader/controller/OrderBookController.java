package com.trader.controller;

import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trader.entity.Order;
import com.trader.service.RedisService;


@Controller
@RequestMapping("/OrderBook")
public class OrderBookController {
	@Resource
	RedisService redisService;
	
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody JSONObject addOrder(@RequestBody String obj) {
		System.out.println("trader: order book " + obj.toString());
		
		JSONArray ja = JSONArray.fromObject(obj);
		
		List<Order> orders = (List<Order>) JSONArray.toCollection(ja, Order.class);
		
		if (orders == null) {
			return new JSONObject();
		}
		
		Order order = orders.get(0);
		String key = "Trader!OrderBook:" + order.getProduct() + " " + order.getPeriod();;
		
		redisService.setOrderBook( key, orders);
		
		return new JSONObject();
	}
}
