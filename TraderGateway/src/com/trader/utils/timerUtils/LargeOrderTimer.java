package com.trader.utils.timerUtils;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.trader.service.OrderService;

@Component
public class LargeOrderTimer {
	@Resource(name = "orderService")
	private OrderService orderService;
	
	@Scheduled(cron = "0/60 * *  * * ? ") // 每60秒执行一次
	public void myTest() {
		System.out.println("Dealing iceberg...");
		orderService.dealIceberg();
	}
}
