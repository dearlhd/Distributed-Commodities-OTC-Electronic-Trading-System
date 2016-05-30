package com.broker.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.broker.entity.BlotterEntry;
import com.broker.entity.Order;
import com.broker.service.RedisService;

/*
 *  使用jms处理请求
 * 
 */

@Controller
@RequestMapping("/rest")
public class OrderController {
	@Resource
	private RedisService redisService;

	private Order parseMessage(JSONObject obj) {
		Order order = new Order();
		order.setOrderType(obj.get("orderType").toString());
		order.setPeriod(obj.get("period").toString());
		order.setProduct(obj.get("product").toString());
		order.setQuantity(Integer.parseInt(obj.get("quantity").toString()));
		order.setPrice(Double.parseDouble(obj.get("price").toString()));
		order.setSide(Integer.parseInt(obj.get("side").toString()));

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		Date date = new Date();
		order.setOrderTime(df.format(date));// new Date()为获取当前系统时间

		String s = UUID.randomUUID().toString();
		String id = s.substring(0, 8) + s.substring(9, 13)
				+ s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
		order.setOrderID(id);
		return order;
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

	private void dealMarketOrder(Order order) {
		List<Order> buyList;
		List<Order> sellList;

		final String key = "OrderBook:" + order.getProduct() + " " + order.getPeriod();

		// order side is buy
		if (order.getSide() == 0) {
			String sellKey = key + order.getSide();
			sellList = redisService.getOrderList(sellKey);
			if (sellList == null) {
				sellList = new ArrayList<Order>();
			}
			sortOrder(sellList);

			for (int i = 0; i < sellList.size(); i++) {
				Order od = sellList.get(i);
				System.out.println(od.getOrderTime() + " " + od.getPrice()
						+ " " + od.getSide() + " " + od.getQuantity());
			}

			int quantity = order.getQuantity();
			List<BlotterEntry> beList = new ArrayList<BlotterEntry>();

			// match best orders in order book
			while (sellList.size() > 0 && quantity > 0) {
				Order oldOrder = sellList.get(0);
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
				SimpleDateFormat df = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");// 设置日期格式
				Date date = new Date();
				be.setDealTime(df.format(date));

				// first node of sell List is enough
				if (oldQuantity >= quantity) {
					// generate blotter entry
					be.setQuantity(quantity);
					beList.add(be);

					// update orders in redis db
					oldOrder.setQuantity(oldQuantity - quantity);
					quantity -= oldQuantity;
					if (oldQuantity == quantity) {
						sellList.remove(0);
					}
					break;
				}
				// first node is not enough
				else {
					// generate blotter entry
					be.setQuantity(oldQuantity);
					beList.add(be);

					// update orders in redis db
					quantity -= oldQuantity;
					sellList.remove(0);
				}
			}

			redisService.setOrderList(sellKey, sellList);
			sellList = redisService.getOrderList(sellKey);
			System.out.println();
			for (int i = 0; i < sellList.size(); i++) {
				Order od = sellList.get(i);
				System.out.println(od.getOrderTime() + " " + od.getPrice()
						+ " " + od.getSide() + " " + od.getQuantity());
			}

			// TODO 生成blotter entry并向交易双方发送通知

			if (quantity > 0) {
				// TODO 当market order不能全部交易完成时
			}
		}

		// order side is sell
		else if (order.getSide() == 1) {
			String buyKey = key + order.getSide();
			buyList = redisService.getOrderList(buyKey);
			if (buyList == null) {
				buyList = new ArrayList<Order>();
			}
			sortOrder(buyList);

			for (int i = 0; i < buyList.size(); i++) {
				Order od = buyList.get(i);
				System.out.println(od.getOrderTime() + " " + od.getPrice()
						+ " " + od.getSide() + " " + od.getQuantity());
			}

			int quantity = order.getQuantity();
			List<BlotterEntry> beList = new ArrayList<BlotterEntry>();

			// match best orders in order book
			while (buyList.size() > 0 && quantity > 0) {
				Order oldOrder = buyList.get(0);
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
				SimpleDateFormat df = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");// 设置日期格式
				Date date = new Date();
				be.setDealTime(df.format(date));

				// first node of buy List is enough
				if (oldQuantity >= quantity) {
					// generate blotter entry
					be.setQuantity(quantity);
					beList.add(be);

					// update orders in redis db
					oldOrder.setQuantity(oldQuantity - quantity);
					quantity -= oldQuantity;
					if (oldQuantity == quantity) {
						buyList.remove(0);
					}
					break;
				}
				// first node is not enough
				else {
					// generate blotter entry
					be.setQuantity(oldQuantity);
					beList.add(be);

					// update orders in redis db
					quantity -= oldQuantity;
					buyList.remove(0);
				}
			}

			redisService.setOrderList(buyKey, buyList);
			buyList = redisService.getOrderList(buyKey);
			System.out.println();
			for (int i = 0; i < buyList.size(); i++) {
				Order od = buyList.get(i);
				System.out.println(od.getOrderTime() + " " + od.getPrice()
						+ " " + od.getSide() + " " + od.getQuantity());
			}

			// TODO 生成blotter entry并向交易双方发送通知

			if (quantity > 0) {
				// TODO 当market order不能全部交易完成时
			}

		}
	}

	private void dealLimitOrder (Order order) {
		List<Order> buyList;
		List<Order> sellList;

		final String key = "OrderBook:" + order.getProduct() + " " + order.getPeriod();
		
		// buy
		if (order.getSide() == 0) {
			String sellKey = key + order.getSide();
			sellList = redisService.getOrderList(sellKey);
			if (sellList == null) {
				sellList = new ArrayList<Order>();
			}
			sortOrder(sellList);
			
			int quantity = order.getQuantity();
			List<BlotterEntry> beList = new ArrayList<BlotterEntry>();
			
			while (sellList.size() > 0) {
				
			}
			
		}
		
		// sell
		else if (order.getSide() == 1) {
			
		}
		
	}
	
	@RequestMapping(value = "/Order", method = RequestMethod.POST)
	@ResponseBody
	public Object addOrder(@RequestBody JSONObject obj) {
		System.out.println("in order controller");
		Order order = parseMessage(obj);

		if (order.getOrderType().equals("market")) {
			dealMarketOrder(order);
		}
		else if (order.getOrderType().equals("limit")){
			dealLimitOrder(order);
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg", "注册人员信息成功");
		return jsonObject;
	}
}
