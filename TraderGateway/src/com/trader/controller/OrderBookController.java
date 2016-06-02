package com.trader.controller;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/OrderBook")
public class OrderBookController {
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody JSONObject addOrder(@RequestBody String obj) {
		System.out.println("trader: order book " + obj.toString());
		return new JSONObject();
	}
}
