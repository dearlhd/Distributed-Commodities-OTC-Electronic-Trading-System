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

}
