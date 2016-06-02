package com.trader.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "torder")
public class Order implements Serializable {
	private static final long serialVersionUID = -8093937721423856923L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "orderID")
	private int orderID;

	@Column(name = "orderType", length = 32)
	private String orderType;

	@Column(name = "product", length = 32)
	private String product;

	@Column(name = "period", length = 32)
	private String period;

	@Column(name = "quantity")
	private int quantity;

	@Column(name = "price")
	private double price;

	@Column(name = "side")
	private int side;

	@Column(name = "orderTime", length = 32)
	private String orderTime;

	@Column(name = "trader", length = 32)
	private String trader;

	@Column(name = "traderCompany", length = 32)
	private String traderCompany;

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
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
