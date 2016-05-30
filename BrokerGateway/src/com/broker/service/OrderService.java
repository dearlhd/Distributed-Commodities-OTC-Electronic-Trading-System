package com.broker.service;

import com.broker.entity.Order;

public interface OrderService {
	void dealMarketOrder (Order order);
	void dealLimitOrder (Order order);
	void dealStopOrder (Order order);
	void dealCancelOrder (Order order);
}
