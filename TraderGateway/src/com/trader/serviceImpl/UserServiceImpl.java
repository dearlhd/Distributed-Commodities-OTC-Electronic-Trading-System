package com.trader.serviceImpl;

import java.util.List;

import javax.annotation.Resource;

import com.trader.dao.UserDao;
import com.trader.entity.User;
import com.trader.service.UserService;

public class UserServiceImpl implements UserService{
	@Resource (name="userDAO")
	UserDao userDao;
	
	public UserDao getUserDao() {
		return userDao;
	}
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public List<User> getUsers() {
		return userDao.getUsers();
	}

	@Override
	public List<User> getUsersByUsername(User user){
		return userDao.getUsersByUsername(user);
	}

	@Override
	public User setUsers(User user){
		List<User> list = getUsersByUsername(user);
		if (list != null) return null;
		return userDao.setUsers(user);
	}
}
