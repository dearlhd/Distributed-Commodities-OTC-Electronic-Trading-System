package com.trader.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.trader.entity.Order;
import com.trader.service.MessagingService;
import com.trader.utils.httpClient.HttpClientUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MessagingServiceImpl implements MessagingService{
	@Resource
	HttpClientUtil httpClientUtil;
	
	List<String> brokerUrls;
	
	public MessagingServiceImpl () {
		brokerUrls = new ArrayList<String>();
		brokerUrls.add("http://localhost:8888/BrokerGateway/rest");
		//brokerUrls.add("");
		//brokerUrls.add("");
	}

	@Override
	public JSONObject postOrderToBroker(String subUrl, int brokerIndex, JSONObject msg) {
		String url = brokerUrls.get(brokerIndex) + subUrl;
		JSONObject obj = (JSONObject) httpClientUtil.postMessageRetObject(url, msg);
		System.out.println("Trader! MsgService, postOrderToBroker: " + obj);
		return obj;
	}
	
	@Override
	public JSONArray postMsgToBroker(String url, JSONObject msg) {
		return (JSONArray)httpClientUtil.postMessageRetArray(url, msg);
	}

	private double getMarketDepth(JSONArray ordersArray, int side) {
		List<Order> orders = (List<Order>) JSONArray.toCollection(ordersArray, Order.class);
		
		if (orders == null) {
			return 0.0;
		}
		
		double price = 0.0;
		if (side == 0) {
			for (int i = orders.size()-1; i >= 0; i--) {
				if (orders.get(i).getSide() == 1) {
					price = orders.get(i).getPrice();
					return price;
				}
			}
		}
		else if (side == 1) {
			for (int i = 0; i < orders.size(); i++) {
				if (orders.get(i).getSide() == 0) {
					price = orders.get(i).getPrice();
					return price;
				}
			}
		}
		return price;
	}
	
	private int bestPrice (List<Double>prices, int side) {
		if (side == 0) {
			double minPrice = prices.get(0);
			int index = 0;
			for (int i = 0; i < prices.size(); i++) {
				double price = prices.get(i);
				if (price != 0 && price < minPrice) {
					minPrice = price;
					index = i;
				}
			}
			if (minPrice != 0) {
				return index;
			}
			else {
				return -1;
			}
		}
		else if (side == 1) {
			double maxPrice = prices.get(0);
			int index = 0;
			for (int i = 0; i < prices.size(); i++) {
				double price = prices.get(i);
				if (price != 0 && price > maxPrice) {
					maxPrice = price;
					index = i;
				}
			}
			if (maxPrice != 0) {
				return index;
			}
			else {
				return -1;
			}
		}
		
		return -1;
	}
	
	@Override
	public int queryMartketPrice(Order order) {
		List<Double> prices = new ArrayList<Double>();
		List<JSONArray> retMsgs = new ArrayList<JSONArray>();
		for (int i = 0; i < brokerUrls.size(); i++) {
			JSONObject obj = JSONObject.fromObject(order);
			JSONArray ja = postMsgToBroker(brokerUrls.get(i) + "/OrderBook", obj);
			retMsgs.add(i, ja);
			double price = getMarketDepth(ja, order.getSide()); 
			prices.add(i, price);
		}
		
		int index = bestPrice(prices, order.getSide());
		
		if (index == -1) {
			return 0;
		}
		
		return index;
	}
	
	

}
