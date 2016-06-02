package com.trader.daoImpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.trader.dao.BlotterDao;
import com.trader.entity.BlotterEntry;
import com.trader.entity.Order;

public class BlotterDaoImpl implements BlotterDao{
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public BlotterEntry addBlotterEntry(BlotterEntry be) {
		Session session = sessionFactory.openSession();
		
		Transaction tx = session.beginTransaction();
		session.save(be);
		tx.commit(); 
		session.close();
		return be;
	}

	@Override
	public List<BlotterEntry> getBlotterEntrys() {
		String hql="from blotterentry";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        
        List<BlotterEntry> blotters= (List<BlotterEntry>)query.list();
		return blotters;
	}

	@Override
	public List<BlotterEntry> getBlotterEntrysByTrader(String username) {
		String hql="from blotterentry where initiatorTrader=:iname or completionTrader=:cname";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("iname", username);
        query.setString("cname", username);
        
        List<BlotterEntry> blotters= (List<BlotterEntry>)query.list();
		return blotters;
	}

}
