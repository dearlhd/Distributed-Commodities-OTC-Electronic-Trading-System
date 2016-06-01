package com.broker.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.broker.entity.BlotterEntry;

public interface BlotterDao {
	public int addBlotterEntry (BlotterEntry be);
	
	public List<BlotterEntry> getBlotterByTrader(@Param(value="trader") String trader);
}
