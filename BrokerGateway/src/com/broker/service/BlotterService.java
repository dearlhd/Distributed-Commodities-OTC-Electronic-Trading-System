package com.broker.service;

import java.util.List;

import net.sf.json.JSONObject;

import com.broker.entity.BlotterEntry;

public interface BlotterService {
	public int addBlotterEntry (BlotterEntry be);
	
	public List<BlotterEntry> getBlotterBySpecialInfo(JSONObject obj);
	public List<BlotterEntry> getBlotterByTrader(String trader);
}
