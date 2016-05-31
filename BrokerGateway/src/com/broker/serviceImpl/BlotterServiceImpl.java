package com.broker.serviceImpl;

import javax.annotation.Resource;

import com.broker.entity.BlotterEntry;
import com.broker.service.BlotterService;
import com.broker.dao.BlotterDao;

public class BlotterServiceImpl implements BlotterService{
	@Resource
	BlotterDao blotterDao;
	
	@Override
	public int addBlotterEntry(BlotterEntry be) {
		return blotterDao.addBlotterEntry(be);
	}
	
}
