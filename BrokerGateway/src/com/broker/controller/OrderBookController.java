package com.broker.controller;

import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.broker.entity.Order;
import com.broker.service.OrderService;

@Controller
@RequestMapping("/rest")
public class OrderBookController {
	@Resource
	private OrderService orderService;

	@RequestMapping(value = "/OrderBook/{conditions}", method = RequestMethod.GET)
	@ResponseBody
	public JSONArray getOrderBook(@PathVariable("conditions") String conditions) {
		conditions = conditions.replaceAll("\\s*", "");
		String[] conds = conditions.split("&");

		JSONObject condObj = new JSONObject();
		for (int i = 0; i < conds.length; i++) {
			String[] np = conds[i].split("=");
			condObj.put(np[0], np[1]);
		}

		Order order = new Order();
		order.setProduct(condObj.getString("product"));
		order.setPeriod(condObj.getString("period"));

		List<Order> lo = orderService.getOrderBook(order);

		if (lo == null) {
			return null;
		}

		for (int i = 0; i < lo.size(); i++) {
			Order od = lo.get(i);
			System.out.println(od.getPrice() + " " + od.getSide() + " "
					+ od.getQuantity());
		}

		JSONArray orderBook = JSONArray.fromObject(lo);
		return orderBook;
	}
}
