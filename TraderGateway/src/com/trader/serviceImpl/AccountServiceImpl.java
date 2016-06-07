package com.trader.serviceImpl;

import javax.annotation.Resource;

import com.trader.dao.UserDao;
import com.trader.service.AccountService;

public class AccountServiceImpl implements AccountService{
	@Resource (name="userDAO")
	UserDao userDao;
	
	final double commissionRate = 0.05;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@Override
	public double addCash(String traderName, double cash) {
		return userDao.changeAvailableBalance(traderName, cash);
	}
	
	@Override
	public double getAvailableBalance(String traderName) {
		return userDao.getAvailableBalance(traderName);
	}
	
	@Override
	public double changeFrozenCapital(String traderName, double amount) {
		return userDao.changeFrozenCapital(traderName, amount);
	}

	@Override
	public double transAvailableToFrozen(String traderName, double amount) {
		if(userDao.changeAvailableBalance(traderName, -amount) < 0)
			return -1;
		
		userDao.changeFrozenCapital(traderName, amount);
		return amount;
	}

	@Override
	public double transFrozenToAvailable(String traderName, double amount) {
		if(userDao.changeFrozenCapital(traderName, -amount) < 0)
			return -1;
		
		userDao.changeAvailableBalance(traderName, amount);
		return amount;
	}

	@Override
	public double depositCash(String traderName, double amount) {
		amount = amount * (1 + commissionRate);
		return transAvailableToFrozen (traderName, amount);
	}
	
	@Override
	public double refundCash(String traderName, double amount) {
		amount = amount * (1 + commissionRate);
		return transFrozenToAvailable(traderName, amount);
	}

	@Override
	public double payBill(String traderName, double amount) {
		amount = - amount * (1 + commissionRate);
		return changeFrozenCapital(traderName, amount);
	}

	@Override
	public double collectBill(String traderName, double amount) {
		return addCash(traderName, amount);
	}
}
