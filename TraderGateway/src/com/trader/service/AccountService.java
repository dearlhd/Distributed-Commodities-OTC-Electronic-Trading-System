package com.trader.service;

public interface AccountService {
	public double addCash(String traderName, double cash);
	public double getAvailableBalance(String traderName);
	public double changeFrozenCapital(String traderName, double amount);
	public double transAvailableToFrozen (String traderName, double amount);
	public double transFrozenToAvailable (String traderName, double amount);
	public double depositCash(String traderName, double amount);
	public double refundCash(String traderName, double amount);
	public double payBill(String traderName, double amount);
	public double collectBill(String traderName, double amount);
}
