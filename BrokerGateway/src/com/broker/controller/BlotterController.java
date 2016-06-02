package com.broker.controller;

import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.broker.entity.BlotterEntry;
import com.broker.service.BlotterService;

@Controller
@RequestMapping("/rest")
public class BlotterController {
	@Resource
	private BlotterService blotterService;
	
	@RequestMapping(value = "/OrderBlotter/{conditions}", method = RequestMethod.GET)
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
		blotters = JSONArray.fromObject(blotterService.getBlotterBySpecialInfo(condObj));
        return blotters;
    }
	
	
	// This is a interface just for test
	@RequestMapping(value = "/OrderBlotter", method = RequestMethod.POST)
    public @ResponseBody JSONArray getBlotter(@RequestBody JSONObject obj) {
		System.out.println(obj.toString());
    	JSONObject queryObj = new JSONObject();
    	try {
	    	queryObj.put("startTime", obj.get("startTime").toString());
	    	queryObj.put("endTime", obj.get("endTime").toString());
	    	queryObj.put("trader", obj.get("trader").toString());
	    	queryObj.put("traderCompany", obj.get("traderCompany").toString());
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	List<BlotterEntry> bel = blotterService.getBlotterBySpecialInfo(queryObj);
    	
    	if (bel == null) {
    		return new JSONArray();
    	}
    	
    	for (int i = 0; i < bel.size(); i++) {
    		System.out.println(bel.get(i).getDealTime());
    	}
    	
    	JSONArray ja = JSONArray.fromObject(bel);
    	return ja;
    }
}
