package com.trader.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "blotterentry")
public class BlotterEntry implements Serializable {
	private static final long serialVersionUID = -1144509530178499858L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "traderID")
	private int tradeID;

	@Column(name = "broker", length = 32)
	private String broker;

	@Column(name = "product", length = 32)
	private String product;

	@Column(name = "period", length = 32)
	private String period;

	@Column(name = "price")
	private double price;

	@Column(name = "quantity")
	private int quantity;

	@Column(name = "initiatorTrader", length = 32)
	private String initiatorTrader;

	@Column(name = "initiatorCompany", length = 32)
	private String initiatorCompany;

	@Column(name = "initiatorSide")
	private int initiatorSide;

	@Column(name = "completionTrader", length = 32)
	private String completionTrader;

	@Column(name = "completionCompany", length = 32)
	private String completionCompany;

	@Column(name = "completionSide")
	private int completionSide;

	@Column(name = "dealTime", length = 32)
	private String dealTime;

	public int getTradeID() {
		return tradeID;
	}

	public void setTradeID(int tradeID) {
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

	public String getDealTime() {
		return dealTime;
	}

	public void setDealTime(String dealTime) {
		this.dealTime = dealTime;
	}

}
