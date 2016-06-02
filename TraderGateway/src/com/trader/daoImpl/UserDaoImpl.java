package com.trader.daoImpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.trader.dao.UserDao;
import com.trader.entity.User;

public class UserDaoImpl implements UserDao{
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<User> getUsers() {
		String hql="from user";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        
        List<User> users = (List<User>)query.list();
		return users;
	}
}
