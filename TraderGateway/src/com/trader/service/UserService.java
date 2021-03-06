package com.trader.service;

import java.util.List;

import com.trader.entity.User;

public interface UserService {
	public List<User> getUsers();
	
	public List<User> getUsersByUsername(User user); 
	
	public User setUsers(User user);
}
