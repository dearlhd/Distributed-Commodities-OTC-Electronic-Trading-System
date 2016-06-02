package com.trader.controller;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trader.entity.BlotterEntry;
import com.trader.service.BlotterService;

@Controller
@RequestMapping("/OrderBlotter")
public class BlotterController {
	@Resource(name = "blotterService")
	private BlotterService blotterService;

	private BlotterEntry parseRequest(JSONObject obj) {
		BlotterEntry be = new BlotterEntry();

		try {
			be.setBroker(obj.getString("broker"));
			be.setProduct(obj.getString("product"));
			be.setPeriod(obj.getString("period"));
			be.setPrice(obj.getDouble("price"));
			be.setQuantity(obj.getInt("quantity"));
			be.setInitiatorTrader(obj.getString("initiatorTrader"));
			be.setInitiatorCompany(obj.getString("initiatorCompany"));
			be.setInitiatorSide(obj.getInt("initiatorSide"));
			be.setCompletionTrader(obj.getString("completionTrader"));
			be.setCompletionCompany(obj.getString("completionCompany"));
			be.setCompletionSide(obj.getInt("completionSide"));
			be.setDealTime(obj.getString("dealTime"));
		} catch (Exception e) {
			e.printStackTrace();
			//throw new WebApplicationException(400);
		}

		return be;
	}

	@RequestMapping(value = "/{conditions}", method = RequestMethod.GET)
    @ResponseBody
    public JSONArray getOrders(@PathVariable("conditions") String conditions) {
		conditions = conditions.replaceAll("\\s*", "");
		String[] conds = conditions.split("&");
		
		JSONObject condObj = new JSONObject();
		for (int i = 0; i < conds.length; i++) {
			String[] np = conds[i].split("=");
			condObj.put(np[0], np[1]);
		}
		
		JSONArray blotters = new JSONArray();
		blotters = JSONArray.fromObject(blotterService.queryBlotterByConditions(condObj));
        return blotters;
    }
	
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody JSONObject addOrder(@RequestBody JSONObject obj) {
		System.out.println(obj.toString());
		BlotterEntry be = parseRequest(obj);
		blotterService.addBlotterEntry(be);
		return obj;
	}
	
}
