package com.trader.service;

import java.util.List;

import net.sf.json.JSONObject;

import com.trader.entity.BlotterEntry;

public interface BlotterService {
	public BlotterEntry addBlotterEntry(BlotterEntry be);
	public List<BlotterEntry> queryBlotterByConditions(JSONObject conds);
}
