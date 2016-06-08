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

import com.trader.entity.BlotterEntry;
import com.trader.entity.Order;
import com.trader.service.AccountService;
import com.trader.service.BlotterService;
import com.trader.service.RedisService;

@Controller
@RequestMapping("/OrderBlotter")
public class BlotterController {
	@Resource(name = "blotterService")
	private BlotterService blotterService;
	
	@Resource
	RedisService redisService;
	
	@Resource(name = "accountService")
	private AccountService accountService;

	private BlotterEntry parseRequest(JSONObject obj) {
		BlotterEntry be = new BlotterEntry();

		try {
			be.setBroker(obj.getString("broker"));
			be.setProduct(obj.getString("product"));
			be.setPeriod(obj.getString("period"));
			be.setPrice(obj.getDouble("price"));
			be.setQuantity(obj.getInt("quantity"));
			be.setInitiatorTrader(obj.getString("initiatorTrader"));
			be.setInitiatorCompany(obj.getString("initiatorCompany"));
			be.setInitiatorSide(obj.getInt("initiatorSide"));
			be.setCompletionTrader(obj.getString("completionTrader"));
			be.setCompletionCompany(obj.getString("completionCompany"));
			be.setCompletionSide(obj.getInt("completionSide"));
			be.setDealTime(obj.getString("dealTime"));
		} catch (Exception e) {
			e.printStackTrace();
			//throw new WebApplicationException(400);
		}

		return be;
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
		
		JSONArray blotters = new JSONArray();
		blotters = JSONArray.fromObject(blotterService.queryBlotterByConditions(condObj));
        return blotters;
    }
	
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody JSONObject addBlotter(@RequestBody JSONObject obj) {
		System.out.println(obj.toString());
		BlotterEntry be = parseRequest(obj);
		
		double price = be.getPrice() * be.getQuantity();
		
		// deal with account changes
		Order initiatorOrder = new Order();
		initiatorOrder.setTraderCompany(be.getInitiatorTrader());
		initiatorOrder.setTrader(be.getInitiatorTrader());
		initiatorOrder.setSide(be.getInitiatorSide());
		initiatorOrder.setPrice(price);
		
		Order completeOrder = new Order();
		completeOrder.setTraderCompany(be.getCompletionCompany());
		completeOrder.setTrader(be.getCompletionTrader());
		completeOrder.setSide(be.getCompletionSide());
		completeOrder.setPrice(price);
		
		if (initiatorOrder.getTraderCompany().equals("traderCompany1")) {
			if (initiatorOrder.getSide() == 0) {
				dealBuyerAccount(initiatorOrder);
			}
			else if (initiatorOrder.getSide() == 1) {
				dealSellerAccount(initiatorOrder);
			}
		}
		
		if (completeOrder.getTraderCompany().equals("traderCompany1")) {
			if (completeOrder.getSide() == 0) {
				dealBuyerAccount(completeOrder);
			}
			else if (completeOrder.getSide() == 1) {
				dealSellerAccount(completeOrder);
			}
		}
		
		// store deal orders
		Order order = new Order();
		order.setProduct(be.getProduct());
		order.setPeriod(be.getPeriod());
		order.setQuantity(be.getQuantity());
		order.setPrice(be.getPrice());
		String key = "Trader!DealOrder:" + order.getProduct() + " " + order.getPeriod();
		List<Order> orders = redisService.getOrderList(key);
		if (orders == null) {
			orders = new ArrayList<Order>();
		}
		orders.add(order);
		redisService.setOrderList(key, orders);
		
		blotterService.addBlotterEntry(be);
		return obj;
	}
	
	private void dealBuyerAccount (Order order) {
		double amount = order.getPrice();
		accountService.payBill(order.getTrader(), amount);
	}
	
	private void dealSellerAccount (Order order) {
		double amount = order.getPrice();
		accountService.collectBill(order.getTrader(), amount);
	}
}
