package com.broker.serviceImpl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.broker.dao.IUserDao;
import com.broker.entity.User;
import com.broker.service.IUserService;

@Service("userService")
public class UserServiceImpl implements IUserService {
	@Resource
	private IUserDao userDao;
	@Override
	public User getUserById(int userId) {
		return this.userDao.selectByPrimaryKey(userId);
	}

}
