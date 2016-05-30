package com.broker.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.broker.entity.BlotterEntry;
import com.broker.entity.Order;
import com.broker.service.OrderService;
import com.broker.service.RedisService;

/*
 *  使用jms处理请求
 * 
 */

@Controller
@RequestMapping("/rest")
public class OrderController {
	@Resource
	private RedisService redisService;
	
	@Resource
	private OrderService orderService;

	private Order parseMessage(JSONObject obj) {
		Order order = new Order();
		order.setOrderType(obj.get("orderType").toString());
		order.setPeriod(obj.get("period").toString());
		order.setProduct(obj.get("product").toString());
		order.setQuantity(Integer.parseInt(obj.get("quantity").toString()));
		order.setPrice(Double.parseDouble(obj.get("price").toString()));
		order.setSide(Integer.parseInt(obj.get("side").toString()));

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		Date date = new Date();
		order.setOrderTime(df.format(date));// new Date()为获取当前系统时间

		String s = UUID.randomUUID().toString();
		String id = s.substring(0, 8) + s.substring(9, 13)
				+ s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
		order.setOrderID(id);
		return order;
	}
	
	@RequestMapping(value = "/Order", method = RequestMethod.POST)
	@ResponseBody
	public Object addOrder(@RequestBody JSONObject obj) {
		System.out.println("in order controller");
		Order order = parseMessage(obj);

		orderService.printOrderBook(order, 1);
		System.out.println();
		
		orderService.printOrderBook(order, 0);
		System.out.println();
		
		if (order.getOrderType().equals("market")) {
			orderService.dealMarketOrder(order);
		}
		else if (order.getOrderType().equals("limit")){
			orderService.dealLimitOrder(order);
		}
		
		if (order.getOrderType().equals("add")) {
			orderService.addOrder(order);
		}

		System.out.println("-------------------");
		orderService.printOrderBook(order, 1);
		System.out.println();
		
		orderService.printOrderBook(order, 0);
		System.out.println();
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg", "注册人员信息成功");
		return jsonObject;
	}
}
