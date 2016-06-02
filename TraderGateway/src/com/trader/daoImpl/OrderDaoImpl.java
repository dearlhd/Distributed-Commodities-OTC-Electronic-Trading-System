package com.trader.daoImpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

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
		Session session = sessionFactory.getCurrentSession();
		order = (Order)session.save(order);
		session.close();
		return order;
	}
}
