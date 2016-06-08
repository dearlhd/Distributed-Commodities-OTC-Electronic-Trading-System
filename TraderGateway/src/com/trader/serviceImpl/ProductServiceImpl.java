package com.trader.serviceImpl;

import java.util.List;

import javax.annotation.Resource;

import com.trader.dao.UserProductDao;
import com.trader.entity.UserProduct;
import com.trader.service.ProductService;

public class ProductServiceImpl implements ProductService{
	@Resource (name="userProductDAO")
	UserProductDao userProductDao;

	public void setUserProductDao(UserProductDao userProductDao) {
		this.userProductDao = userProductDao;
	}

	@Override
	public UserProduct addProduct(UserProduct up) {
		return userProductDao.addProduct(up);
	}

	@Override
	public List<UserProduct> getProducts(String trader) {
		return userProductDao.getProductsByTrader(trader);
	}

}
