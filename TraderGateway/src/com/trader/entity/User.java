package com.trader.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "user")
public class User implements Serializable {
	private static final long serialVersionUID = -1031655240827623642L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "userID")
	private Integer userID;

	@Column(name = "username", length = 32)
	private String username;

	@Column(name = "password", length = 32)
	private String password;

	@Column(name = "availableBalance")
	private double availableBalance;

	@Column(name = "frozenCapital")
	private double frozenCapital;

	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public double getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(double availableBalance) {
		this.availableBalance = availableBalance;
	}

	public double getFrozenCapital() {
		return frozenCapital;
	}

	public void setFrozenCapital(double frozenCapital) {
		this.frozenCapital = frozenCapital;
	}

}