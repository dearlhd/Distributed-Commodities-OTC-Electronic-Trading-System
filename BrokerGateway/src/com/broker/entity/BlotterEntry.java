package com.broker.entity;

import java.io.Serializable;

public class BlotterEntry implements Serializable{
	private static final long serialVersionUID = -1144509530178499858L;
	private long tradeID;
	private String broker;
	private String product;
	private String period;
	private double price;
	private int quantity;
	private String initiatorTrader;
	private String initiatorCompany;
	private int initiatorSide;
	private String completionTrader;
	private String completionCompany;
	private int completionSide;
	private String dealTime;

	public String getDealTime() {
		return dealTime;
	}

	public void setDealTime(String dealTime) {
		this.dealTime = dealTime;
	}

	public long getTradeID() {
		return tradeID;
	}

	public void setTradeID(long tradeID) {
		this.tradeID = tradeID;
	}

	public String getBroker() {
		return broker;
	}

	public void setBroker(String broker) {
		this.broker = broker;
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getInitiatorTrader() {
		return initiatorTrader;
	}

	public void setInitiatorTrader(String initiatorTrader) {
		this.initiatorTrader = initiatorTrader;
	}

	public String getInitiatorCompany() {
		return initiatorCompany;
	}

	public void setInitiatorCompany(String initiatorCompany) {
		this.initiatorCompany = initiatorCompany;
	}

	public int getInitiatorSide() {
		return initiatorSide;
	}

	public void setInitiatorSide(int initiatorSide) {
		this.initiatorSide = initiatorSide;
	}

	public String getCompletionTrader() {
		return completionTrader;
	}

	public void setCompletionTrader(String completionTrader) {
		this.completionTrader = completionTrader;
	}

	public String getCompletionCompany() {
		return completionCompany;
	}

	public void setCompletionCompany(String completionCompany) {
		this.completionCompany = completionCompany;
	}

	public int getCompletionSide() {
		return completionSide;
	}

	public void setCompletionSide(int completionSide) {
		this.completionSide = completionSide;
	}

}
