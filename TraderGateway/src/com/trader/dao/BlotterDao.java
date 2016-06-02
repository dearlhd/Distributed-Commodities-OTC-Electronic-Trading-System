package com.trader.dao;

import java.util.List;

import com.trader.entity.BlotterEntry;

public interface BlotterDao {
	public BlotterEntry addBlotterEntry(BlotterEntry be);
	public List<BlotterEntry> getBlotterEntrys();
	public List<BlotterEntry> getBlotterEntrysByTrader(String username);
}
