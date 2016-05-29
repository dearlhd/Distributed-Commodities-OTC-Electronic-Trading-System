package com.broker.controller;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.broker.entity.Order;

@Controller
@RequestMapping("/rest")
public class OrderController {
	@RequestMapping(value = "/Order", method = RequestMethod.POST)
    @ResponseBody
    public Object addOrder(@RequestBody JSONObject obj) {
		Order order = new Order ();
		obj.get("");
		
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", "注册人员信息成功");
        return jsonObject;
    }
}
