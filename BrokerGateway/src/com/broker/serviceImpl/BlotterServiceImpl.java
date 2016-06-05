package com.broker.serviceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import com.broker.entity.BlotterEntry;
import com.broker.service.BlotterService;
import com.broker.dao.BlotterDao;

public class BlotterServiceImpl implements BlotterService {
	@Resource
	BlotterDao blotterDao;

	@Override
	public int addBlotterEntry(BlotterEntry be) {
		return blotterDao.addBlotterEntry(be);
	}

	@Override
	public List<BlotterEntry> getBlotterByTrader(String trader) {

		return blotterDao.getBlotterByTrader(trader);
	}

	@Override
	public List<BlotterEntry> getBlotterBySpecialInfo(JSONObject obj) {

		List<BlotterEntry> blotters = new ArrayList<BlotterEntry>();
		
		if (!obj.containsKey("trader") || obj.get("trader").toString().equals("")) {
			blotters = getBlotterByTrader(null);
		}
		else {
			blotters = getBlotterByTrader(obj.get("trader").toString());
		}
		
		if (blotters == null) {
			return null;
		}
		
		List<BlotterEntry> results = new ArrayList<BlotterEntry>();
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startTime = df.parse(obj.getString("startTime") + " 00:00:00") ;
			Date endTime = df.parse(obj.getString("endTime") + " 23:59:59");
			

			for (int i = 0; i < blotters.size(); i++) {
				Date entryTime = df.parse(blotters.get(i).getDealTime());
				if (entryTime.getTime() >= startTime.getTime() && entryTime.getTime() <= endTime.getTime()) {
					results.add(blotters.get(i));
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return results;
	}

}
