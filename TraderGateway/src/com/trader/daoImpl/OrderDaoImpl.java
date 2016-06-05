package com.trader.daoImpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.trader.dao.OrderDao;
import com.trader.entity.Order;
import com.trader.entity.User;

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

	@Override
	public List<Order> getOrders() {
		String hql="from torder";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        
        List<Order> orders= (List<Order>)query.list();
		return orders;
	}

	@Override
	public List<Order> getOrderByUser(String username) {
		String hql="from torder where trader=:name";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("name", username);
        
        List<Order> orders= (List<Order>)query.list();
		return orders;
	}

	@Override
	public Order getOrderByID(int id) {
		String hql="from torder where orderID=:id";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        query.setInteger("id", id);
        
        List<Order> orders= (List<Order>)query.list();
		
        if (orders != null && orders.size() != 0) {
        	return orders.get(0);
        }
        
		return null;
	}
}
