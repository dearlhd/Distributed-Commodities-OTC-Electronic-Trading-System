package com.broker.dao;

import java.util.List;

import com.broker.entity.BlotterEntry;

public interface BlotterDao {
	public int addBlotterEntry (BlotterEntry be);
	
	public List<BlotterEntry> getBlotterByTrader(String trader);
}
