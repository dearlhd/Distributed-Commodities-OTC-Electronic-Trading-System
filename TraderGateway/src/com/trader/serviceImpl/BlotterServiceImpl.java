package com.trader.serviceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import com.trader.dao.BlotterDao;
import com.trader.entity.BlotterEntry;
import com.trader.service.BlotterService;

public class BlotterServiceImpl implements BlotterService{
	@Resource (name="blotterDAO")
	BlotterDao blotterDao;

	public void setBlotterDao(BlotterDao blotterDao) {
		this.blotterDao = blotterDao;
	}

	@Override
	public BlotterEntry addBlotterEntry(BlotterEntry be) {
		return blotterDao.addBlotterEntry(be);
	}

	@Override
	public List<BlotterEntry> queryBlotterByConditions(JSONObject conds) {
		List<BlotterEntry> blotters = new ArrayList<BlotterEntry>();
		if (conds.containsKey("trader")) {
			blotters = blotterDao.getBlotterEntrysByTrader(conds.getString("trader"));
		}
		else {
			blotters = blotterDao.getBlotterEntrys();
		}
		
		String startTime = conds.getString("startTime") + " 00:00:00";
		String endTime = conds.getString("endTime") + " 23:59:59";
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d1 = df.parse(startTime);
			Date d2 = df.parse(endTime);
			for (int i = 0; i < blotters.size(); i++) {
				BlotterEntry be = blotters.get(i);
				Date date = df.parse(be.getDealTime());
				if (!(date.getTime() >= d1.getTime() && date.getTime() <= d2.getTime())) {
					blotters.remove(i);
					i--;
				}
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return blotters;
	}

}
