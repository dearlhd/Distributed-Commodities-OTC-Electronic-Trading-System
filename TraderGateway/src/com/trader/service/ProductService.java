package com.trader.service;

import java.util.List;

import com.trader.entity.UserProduct;

public interface ProductService {
	public UserProduct addProduct(UserProduct up);
	public List<UserProduct> getProducts (String trader);
}
