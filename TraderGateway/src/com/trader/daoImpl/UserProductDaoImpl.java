package com.trader.daoImpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.trader.dao.UserProductDao;
import com.trader.entity.Order;
import com.trader.entity.UserProduct;

public class UserProductDaoImpl implements UserProductDao{
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public UserProduct addProduct(UserProduct up) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.save(up);
		tx.commit(); 
		session.close();
		return up;
	}

	@Override
	public List<UserProduct> getProductsByTrader(String trader) {
		String hql="from torder where username=:name";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("name", trader);
        
        List<UserProduct> products= (List<UserProduct>)query.list();
		return products;
	}
}
