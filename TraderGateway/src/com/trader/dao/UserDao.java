package com.trader.dao;

import java.util.List;

import com.trader.entity.User;

public interface UserDao {
	public List<User> getUsers();
	
	public List<User> getUsersByUsername(User user); 
	
	public User setUsers(User user);
}
