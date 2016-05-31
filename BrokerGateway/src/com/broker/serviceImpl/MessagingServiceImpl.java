package com.broker.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import com.broker.service.MessagingService;
import com.broket.utils.httpClient.HttpClientUtil;

public class MessagingServiceImpl implements MessagingService{
	@Resource
	HttpClientUtil httpClientUtil;

	@Override
	public JSONObject postMessage(String url, JSONObject msg) {
		return httpClientUtil.postMessage(url, msg);
	}

	@Override
	public JSONObject postToAllTrader(JSONObject msg) {
		String url1 = "";
		String url2 = "";
		String url3 = "";
		List<String> ls = new ArrayList<String>();
		ls.add(url1);
		ls.add(url2);
		ls.add(url3);
		
		for (int i = 0; i < ls.size(); i++) {
			httpClientUtil.postMessage(ls.get(i), msg);
		}
		
		return null;
	}
}
