package com.broker.entity;

import java.io.Serializable;

public class Order implements Serializable {
	private static final long serialVersionUID = -8093937721423856923L;
	private String orderID;
	private String orderType;
	private String product;
	private String period;
	private int quantity;
	private double price;
	private int side;
	private String orderTime;
	private String trader;
	private String traderCompany;

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getSide() {
		return side;
	}

	public void setSide(int side) {
		this.side = side;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getTrader() {
		return trader;
	}

	public void setTrader(String trader) {
		this.trader = trader;
	}

	public String getTraderCompany() {
		return traderCompany;
	}

	public void setTraderCompany(String traderCompany) {
		this.traderCompany = traderCompany;
	}

}
