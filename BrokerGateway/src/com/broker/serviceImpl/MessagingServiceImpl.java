package com.broker.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.broker.service.MessagingService;
import com.broket.utils.httpClient.HttpClientUtil;

public class MessagingServiceImpl implements MessagingService{
	@Resource
	HttpClientUtil httpClientUtil;
	
	List<String> traderUrls;

	public MessagingServiceImpl () {
		traderUrls = new ArrayList<String>();
		traderUrls.add("https://localhost:8443/TraderGateway");
		//traderUrls.add("");
		//traderUrls.add("");
	}
	
	@Override
	public JSONObject postMessage(String url, JSONObject msg) {
		return (JSONObject) httpClientUtil.postMessageRetObject(url, msg);
	}

	@Override
	public JSONObject postPriceToAllTrader(JSONArray msg) {
		for (int i = 0; i < traderUrls.size(); i++) {
			httpClientUtil.postMessageRetObject(traderUrls.get(i) + "/OrderBook", msg);
		}
		
		return null;
	}
}
