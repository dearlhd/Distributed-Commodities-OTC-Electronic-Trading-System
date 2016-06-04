package com.trader.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@RequestMapping(value = "/{conditions}", method = RequestMethod.GET)
    public @ResponseBody String getOrderBook(@PathVariable("conditions") String conditions) {
		System.out.println("Trader!OrderBook: GET " + conditions);
		conditions = conditions.replaceAll("\\s*", "");
		String[] conds = conditions.split("&");
		
		JSONObject condObj = new JSONObject();
		for (int i = 0; i < conds.length; i++) {
			String[] np = conds[i].split("=");
			condObj.put(np[0], np[1]);
		}
		String key = "Trader!OrderBook:" + condObj.getString("product") + " " + condObj.getString("period");
		
		List<Order> orders = redisService.getOrderBook(key);
		
		if (orders == null) {
			System.out.println("Trader!OrderBook: GET orders is null");
			return null;
		}
		
		for (int i = 0; i < orders.size(); i++) {
			System.out.println(orders.get(i).getProduct() + " " + orders.get(i).getPrice() + " " + orders.get(i).getQuantity() + " " + orders.get(i).getSide());
		}
		
		JSONArray orderBook = JSONArray.fromObject(orders);
		
		
        return orderBook.toString();
    }
	
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody JSONObject setOrderBook(@RequestBody String obj) {
		System.out.println("Trader!OrderBook: POST " + obj.toString());
		
		JSONArray ja = JSONArray.fromObject(obj);
		
		List<Order> orders = new ArrayList<Order>();
		
//		for (int i = 0; i < ja.size(); i++) {
//			JSONObject jsonObj = (JSONObject) ja.get(i);
//			Order od = new Order();
//			System.out.println("dong: " + jsonObj.toString());
//			//od = (Order) JSONObject.toBean(jsonObj);
//			orders.add(od);
//		}
		
		orders = (List<Order>) JSONArray.toCollection(ja, Order.class);
		
		if (orders == null || orders.size() == 0) {
			System.out.println("Trader!OrderBook: POST orders is null");
			return new JSONObject();
		}
		
		Order order = orders.get(0);
		String key = "Trader!OrderBook:" + order.getProduct() + " " + order.getPeriod();
		
		redisService.setOrderBook( key, orders);
		
		return new JSONObject();
	}
}
