package com.trader.dao;

import java.util.List;

import com.trader.entity.UserProduct;

public interface UserProductDao {
	public UserProduct addProduct(UserProduct up);
	public List<UserProduct> getProductsByTrader(String trader);
}
