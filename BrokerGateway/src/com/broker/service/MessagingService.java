package com.broker.service;

import net.sf.json.JSONObject;

public interface MessagingService {
	public JSONObject postMessage(String url, JSONObject msg);
	
	public JSONObject postToAllTrader(JSONObject msg);
}
