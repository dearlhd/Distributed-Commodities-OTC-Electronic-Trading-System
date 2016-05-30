package com.broker.serviceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.broker.entity.BlotterEntry;
import com.broker.entity.Order;
import com.broker.service.OrderService;
import com.broker.service.RedisService;

public class OrderServiceImpl implements OrderService {
	@Resource
	private RedisService redisService;

	@Override
	public void dealMarketOrder(Order order) {
		List<Order> buyList;
		List<Order> sellList;

		final String key = "OrderBook:" + order.getProduct() + " " + order.getPeriod();

		// order side is buy
		if (order.getSide() == 0) {
			String sellKey = key + 1;
			sellList = redisService.getOrderList(sellKey);
			if (sellList == null) {
				sellList = new ArrayList<Order>();
			}
			
			int quantity = matchOrder(sellList, new ArrayList<Order>(), order);

			if (quantity > 0) {
				// TODO 当market order不能全部交易完成时
			}
		}

		// order side is sell
		else if (order.getSide() == 1) {
			String buyKey = key + 0;
			buyList = redisService.getOrderList(buyKey);
			if (buyList == null) {
				buyList = new ArrayList<Order>();
			}
			int quantity = matchOrder (buyList, new ArrayList<Order>(), order);

			if (quantity > 0) {
				// TODO 当market order不能全部交易完成时
			}

		}
	}

	@Override
	public void dealLimitOrder(Order order) {
		List<Order> buyList;
		List<Order> sellList;
		List<Order> matchList = new ArrayList<Order>();

		final String key = "OrderBook:" + order.getProduct() + " " + order.getPeriod();

		String sellKey = key + 1;
		String buyKey = key + 0;
		// buy
		if (order.getSide() == 0) {
			sellList = redisService.getOrderList(sellKey);
			if (sellList == null) {
				sellList = new ArrayList<Order>();
			}
			
			for (int i = 0; i < sellList.size(); i++) {
				if (order.getPrice() >= sellList.get(i).getPrice()) {
					matchList.add(sellList.get(i));
					sellList.remove(i);
				}
			}

			int quantity = matchOrder(matchList, sellList, order);
			if (quantity > 0) {
				order.setQuantity(quantity);
				buyList = redisService.getOrderList(buyKey);
				if (buyList == null) {
					buyList = new ArrayList<Order>();
				}
				buyList.add(order);
				redisService.setOrderList(buyKey, buyList);
			}
		}
		// sell
		else if (order.getSide() == 1) {
			buyList = redisService.getOrderList(buyKey);
			if (buyList == null) {
				buyList = new ArrayList<Order>();
			}
			for (int i = 0; i < buyList.size(); i++) {
				if (order.getPrice() <= buyList.get(i).getPrice()) {
					matchList.add(buyList.get(i));
					buyList.remove(i);
				}
			}
			
			int quantity = matchOrder(matchList, buyList, order);
			if (quantity > 0) {
				order.setQuantity(quantity);
				sellList = redisService.getOrderList(sellKey);
				if (sellList == null) {
					sellList = new ArrayList<Order>();
				}
				sellList.add(order);
				redisService.setOrderList(sellKey, sellList);
			}
		}

	}

	@Override
	public void dealStopOrder(Order order) {
		String stopKey = "StopOrder:" + order.getProduct() + " " + order.getPeriod();
		final String key = "OrderBook:" + order.getProduct() + " " + order.getPeriod();
		
		List<Order> stopList = redisService.getOrderList(stopKey);
		if (stopList == null) {
			return;
		} 
		
		for (int i = 0; i < stopList.size(); i++) {
			Order stopOrder = stopList.get(i);
			if (stopOrder.getPrice() == order.getPrice()) {
				List<Order> matchList;
				if (stopOrder.getSide() == 0) {
					matchList = redisService.getOrderList(key+1);
				}
				else {
					matchList = redisService.getOrderList(key+0);
				}
				matchOrder(matchList, new ArrayList<Order>(), stopOrder);
			}
		}
		
	}

	@Override
	public void dealCancelOrder(Order order) {
		String key = "OrderBook:" + order.getProduct() + " " + order.getPeriod();
		List<Order> matchList = new ArrayList<Order>();
		if (order.getOrderType() == "stop") {
			key = "StopOrder:" + order.getProduct() + " " + order.getPeriod();
			matchList = redisService.getOrderList(key);
		}
		
		if (order.getSide() == 0 && order.getOrderType() == "limit") {
			key = key + 0;
			matchList = redisService.getOrderList(key);
		}
		else if (order.getSide() == 1 && order.getOrderType() == "limit"){
			key = key + 1;
			matchList = redisService.getOrderList(key);
		}
		
		if (matchList == null) {
			return;
		} 
		
		for (int i = 0; i < matchList.size(); i++) {
			Order stopOrder = matchList.get(i);
			if (stopOrder.getOrderID().equals(order.getOrderID())) {
				if (stopOrder.getQuantity() == order.getQuantity()) {
					matchList.remove(i);
					redisService.setOrderList(key, matchList);
					return;
				}
				else if (stopOrder.getQuantity() > order.getQuantity()) {
					stopOrder.setQuantity(stopOrder.getQuantity() - order.getQuantity());
					redisService.setOrderList(key, matchList);
					return;
				}
			}
		}
	}

	private static int compareOrder(Order order1, Order order2) {
		double price1 = order1.getPrice();
		double price2 = order2.getPrice();

		int side = order1.getSide();

		if (side == 1) {
			if (price1 < price2) {
				return -1;
			} else if (price1 > price2) {
				return 1;
			}
		} else {
			if (price1 > price2) {
				return -1;
			} else if (price1 < price2) {
				return 1;
			}
		}

		try {
			String time1 = order1.getOrderTime();
			String time2 = order2.getOrderTime();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d1 = df.parse(time1);
			Date d2 = df.parse(time2);

			if (d1.getTime() <= d2.getTime()) {
				return -1;
			} else {
				return 1;
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void sortOrder(List<Order> orders) {
		Collections.sort(orders, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				return compareOrder((Order) o1, (Order) o2);
			}
		});
	}

	private int matchOrder(List<Order> matchList, List<Order> leftList, Order order) {
		sortOrder(matchList);
		
		int quantity = order.getQuantity();
		List<BlotterEntry> beList = new ArrayList<BlotterEntry>();
		final String key = "OrderBook:" + order.getProduct() + " "
				+ order.getPeriod();
		String matchKey;

		if (order.getSide() == 0) {
			matchKey = key + 1;
		}
		else {
			matchKey = key + 0;
		}
		while (matchList.size() > 0 && quantity > 0) {
			Order oldOrder = matchList.get(0);
			int oldQuantity = oldOrder.getQuantity();

			BlotterEntry be = new BlotterEntry();
			be.setBroker("broker1");
			be.setProduct(order.getProduct());
			be.setPeriod(order.getPeriod());
			be.setPrice(oldOrder.getPrice());
			be.setInitiatorTrader(oldOrder.getTrader());
			be.setInitiatorTrader(oldOrder.getTraderCompany());
			be.setInitiatorSide(1);
			be.setCompletionTrader(order.getTrader());
			be.setCompletionCompany(order.getTraderCompany());
			be.setCompletionSide(0);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
			Date date = new Date();
			be.setDealTime(df.format(date));

			// first node of sell List is enough
			if (oldQuantity >= quantity) {
				// generate blotter entry
				be.setQuantity(quantity);
				beList.add(be);

				// update orders in redis db
				oldOrder.setQuantity(oldQuantity - quantity);
				if (oldQuantity == quantity) {
					matchList.remove(0);
				}
				quantity -= oldQuantity;
				break;
			}
			// first node is not enough
			else {
				// generate blotter entry
				be.setQuantity(oldQuantity);
				beList.add(be);

				// update orders in redis db
				quantity -= oldQuantity;
				matchList.remove(0);
			}
		}

		matchList.addAll(leftList);
		
		redisService.setOrderList(matchKey, matchList);
		matchList = redisService.getOrderList(matchKey);

		// TODO 生成blotter entry并向交易双方发送通知

		return quantity;
	}

	@Override
	public void addOrder(Order order) {
		List<Order> orders = new ArrayList<Order>();
		final String key = "OrderBook:" + order.getProduct() + " "
				+ order.getPeriod();
		if (order.getSide() == 0) {
			String buyKey = key + 0;
			orders = redisService.getOrderList(buyKey);
			if (orders == null) {
				orders = new ArrayList<Order>();
			}
			orders.add(order);
			redisService.setOrderList(buyKey, orders);
		} else {
			String sellKey = key + 1;
			orders = redisService.getOrderList(sellKey);
			if (orders == null) {
				orders = new ArrayList<Order>();
			}
			orders.add(order);
			redisService.setOrderList(sellKey, orders);
		}
	}

	@Override
	public void printOrderBook(Order order, int side) {
		String key = "OrderBook:" + order.getProduct() + " " + order.getPeriod() + side;
		List<Order> matchList = redisService.getOrderList(key);
		
		if (matchList == null) {
			return;
		}
		
		sortOrder(matchList);
		for (int i = 0; i < matchList.size(); i++) {
			Order od = matchList.get(i);
			System.out.println(od.getOrderTime() + " " + od.getPrice() + " " + od.getSide() + " " + od.getQuantity());
		}
	}

	@Override
	public void addStopOrder(Order order) {
		String key = "StopOrder:" + order.getProduct() + " " + order.getPeriod();
		
		List<Order> stopList = redisService.getOrderList(key);
		if (stopList == null) {
			stopList = new ArrayList<Order>();
		}
		stopList.add(order);
		redisService.setOrderList(key, stopList);
	}
}
