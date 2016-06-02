package com.trader.service;

import com.trader.entity.Order;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface MessagingService {
	public int queryMartketPrice(Order order);
	
	public JSONObject postOrderToBroker (String subUrl, int brokerIndex, JSONObject msg);
	
	public JSONArray postMsgToBroker (String url, JSONObject msg);
}
