package com.trader.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.trader.dao.OrderDao;
import com.trader.entity.Order;
import com.trader.service.OrderService;

@Controller
@RequestMapping("/Order")
public class OrderController {
	@Resource(name = "orderService")
	private OrderService orderService;

	private Order parseRequest(JSONObject obj) {
		Order order = new Order();

		try {
			order.setOrderType(obj.get("orderType").toString());
			order.setPeriod(obj.get("period").toString());
			order.setProduct(obj.get("product").toString());
			order.setQuantity(Integer.parseInt(obj.get("quantity").toString()));
			order.setPrice(Double.parseDouble(obj.get("price").toString()));
			order.setSide(Integer.parseInt(obj.get("side").toString()));
			order.setTrader(obj.get("trader").toString());
			
			order.setTraderCompany("traderCompany1");
		} catch (Exception e) {
			e.printStackTrace();
			//throw new WebApplicationException(400);
		}

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		Date date = new Date();
		order.setOrderTime(df.format(date));// new Date()为获取当前系统时间

		return order;
	}

	@RequestMapping(value = "/{conditions}", method = RequestMethod.GET)
    @ResponseBody
    public JSONArray getOrders(@PathVariable("conditions") String conditions) {
		conditions = conditions.replaceAll("\\s*", "");
		String[] conds = conditions.split("&");
		
		JSONObject condObj = new JSONObject();
		for (int i = 0; i < conds.length; i++) {
			String[] np = conds[i].split("=");
			condObj.put(np[0], np[1]);
		}
		
		JSONArray orderBook = new JSONArray();
		orderBook = JSONArray.fromObject(orderService.queryOrderByConditions(condObj));
        return orderBook;
    }
	
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody JSONObject addOrder(@RequestBody JSONObject obj) {
		System.out.println(obj.toString());
		Order order = parseRequest(obj);
		orderService.addOrder(order);
		return obj;
	}
	
	@RequestMapping(value = "/test/{arg}", method = RequestMethod.GET)
	public @ResponseBody JSONObject testSaveOrder(@PathVariable("arg") String arg) {
		System.out.println(arg);
		JSONObject obj = new JSONObject();
		//Order order = parseRequest(obj);
		//orderService.addOrder(order);
		return obj;
	}
}
