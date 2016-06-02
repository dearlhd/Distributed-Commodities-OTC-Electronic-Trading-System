package com.trader.daoImpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

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
	
	@Override
	public List<User> getUsersByUsername(User user){
		String hql="from user where username=?";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString(0, user.getUsername());
        
        List<User> users = (List<User>)query.list();
        if (users.size() == 0) 
        	return null;
        else 
        	return users;
	}
	
	@Override
	public User setUsers(User user){
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.save(user);
		tx.commit(); 
		session.close();
		return user;
	}
}
