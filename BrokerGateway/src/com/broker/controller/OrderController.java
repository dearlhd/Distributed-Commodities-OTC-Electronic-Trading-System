package com.broker.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.broker.entity.BlotterEntry;
import com.broker.entity.Order;
import com.broker.service.BlotterService;
import com.broker.service.OrderService;

/*
 *  使用jms处理请求
 * 
 */

@Controller
@RequestMapping("/rest")
public class OrderController {
	@Resource
	private OrderService orderService;
	
	@Resource
	private BlotterService blotterService;

	private Order parseMessage(JSONObject obj) {
		Order order = new Order();
		order.setOrderID(obj.get("orderID").toString());
		order.setOrderType(obj.get("orderType").toString());
		order.setPeriod(obj.get("period").toString());
		order.setProduct(obj.get("product").toString());
		order.setQuantity(Integer.parseInt(obj.get("quantity").toString()));
		order.setPrice(Double.parseDouble(obj.get("price").toString()));
		order.setSide(Integer.parseInt(obj.get("side").toString()));
		order.setTrader(obj.get("trader").toString());
		order.setTraderCompany(obj.get("traderCompany").toString());

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		Date date = new Date();
		order.setOrderTime(df.format(date));// new Date()为获取当前系统时间

		return order;
	}
	
	@RequestMapping(value = "/Order/{conditions}", method = RequestMethod.GET)
	@ResponseBody
	public JSONArray getOrders(@PathVariable("conditions") String conditions) {
		conditions = conditions.replaceAll("\\s*", "");
		String[] conds = conditions.split("&");
		
		JSONObject condObj = new JSONObject();
		for (int i = 0; i < conds.length; i++) {
			String[] np = conds[i].split("=");
			condObj.put(np[0], np[1]);
		}
		
		JSONArray orders = new JSONArray();
		orders = JSONArray.fromObject(orderService.queryOrderByConditions(condObj));
		return orders;
	}
	
	@RequestMapping(value = "/Order", method = RequestMethod.POST)
	@ResponseBody
	public Object addOrder(@RequestBody JSONObject obj) {
		System.out.println("in order controller, post method");
		Order order = parseMessage(obj);

		if (order.getOrderType().equals("market")) {
			double marketDepth = orderService.dealMarketOrder(order);
			if (marketDepth != 0.0) {
				order.setPrice(marketDepth);
				orderService.dealStopOrder(order);
			}
		}
		else if (order.getOrderType().equals("limit")){
			double marketDepth = orderService.dealLimitOrder(order);
			if (marketDepth != 0.0) {
				order.setPrice(marketDepth);
				orderService.dealStopOrder(order);
			}
		}
		else if (order.getOrderType().equals("stop")) {
			orderService.addStopOrder(order);
		}
		else if (order.getOrderType().equals("cancel")) {
			orderService.dealCancelOrder(order);
		}
		if (order.getOrderType().equals("add")) {
			orderService.addOrder(order);
		}

		return obj;
	}
	
//	@RequestMapping(value = "/OrderBook", method = RequestMethod.POST)
//    @ResponseBody
//    public JSONArray getOrderBook(@RequestBody JSONObject obj) {
//		Order order = parseMessage(obj);
//		
//		List<Order> lo = orderService.getOrderBook(order);
//		
//		if (lo == null) {
//			return null; 
//		}
//		
//		for (int i = 0; i < lo.size(); i++) {
//			Order od = lo.get(i);
//			System.out.println(od.getPrice() + " " + od.getSide() + " " + od.getQuantity());
//		}
//		
//		return JSONArray.fromObject(lo);
//    }
	
	@RequestMapping(value = "/testBlotter", method = RequestMethod.POST)
    @ResponseBody
    public Object getUser(@RequestBody JSONObject obj) {
		Order order = parseMessage(obj);
		BlotterEntry be = new BlotterEntry();
		be.setTradeID(1);
		be.setBroker("broker1");
		be.setProduct(order.getProduct());
		be.setPeriod(order.getPeriod());
		be.setPrice(order.getPrice());
		be.setInitiatorTrader(order.getTrader());
		be.setInitiatorCompany(order.getTraderCompany());
		be.setInitiatorSide(1);
		be.setCompletionTrader(order.getTrader());
		be.setCompletionCompany(order.getTraderCompany());
		be.setCompletionSide(0);
		be.setQuantity(order.getQuantity());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		Date date = new Date();
		be.setDealTime(df.format(date));
		
		blotterService.addBlotterEntry(be);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg", "注册人员信息成功");
		return jsonObject;
    }
}
