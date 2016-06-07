package com.trader.controller;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trader.service.AccountService;

@Controller
@RequestMapping("/Account")
public class AccountController {
	@Resource(name = "accountService")
	private AccountService accountService;
	
	/*
	 * usage: 
	 * 		1. http get method
	 * 		   like https://localhost:8443/TraderGateway/Account/trader=luo & amount=1000
	 * 			trader is trader name, amount is the amount to increase 
	 */
	
	@RequestMapping(value = "/{conditions}", method = RequestMethod.GET)
    public @ResponseBody JSONObject addCash(@PathVariable("conditions") String conditions) {
		conditions = conditions.replaceAll("\\s*", "");
		String[] conds = conditions.split("&");
		
		JSONObject condObj = new JSONObject();
		for (int i = 0; i < conds.length; i++) {
			String[] np = conds[i].split("=");
			condObj.put(np[0], np[1]);
		}
		
		JSONObject obj = new JSONObject();
		
		if (condObj.containsKey("trader") && condObj.containsKey("amount")) {
			String trader = condObj.getString("trader");
			double amount = condObj.getDouble("amount");
			if (accountService.addCash(trader, amount) >= 0) {
				obj.put("msg", 1);
				return obj;
			}
		}
		
		obj.put("msg", -1);
		return obj;
	}
}
