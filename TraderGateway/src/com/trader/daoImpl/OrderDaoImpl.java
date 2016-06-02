package com.trader.daoImpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.trader.dao.OrderDao;
import com.trader.entity.Order;

public class OrderDaoImpl implements OrderDao {
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Order addOrder(Order order) {
		Session session = sessionFactory.openSession();
		
		Transaction tx = session.beginTransaction();
		session.save(order);
		tx.commit(); 
		session.close();
		return order;
	}
}
