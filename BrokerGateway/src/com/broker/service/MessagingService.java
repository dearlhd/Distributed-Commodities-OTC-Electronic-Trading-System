package com.broker.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface MessagingService {
	public JSONObject postMessage(String url, JSONObject msg);
	
	public JSONObject postPriceToAllTrader(JSONArray msg);
}
