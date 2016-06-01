package com.trader.controller;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trader.service.UserService;

@Controller
@RequestMapping("/User")
public class UserController {
	@Resource(name="userService")
    private UserService userService;
    
    @RequestMapping(value="/getUser",method=RequestMethod.GET)
    public @ResponseBody JSONArray getUser(){
    	JSONArray obj = JSONArray.fromObject(userService.getUsers());
        return obj;
    }
    
    @RequestMapping(value = "/postUser", method = RequestMethod.POST)
    public @ResponseBody JSONObject postUser(@RequestBody JSONObject obj) {
    	System.out.println(obj.toString());
    	// 这里我没有写东西，不过post方法的参数和返回值的用法都体现了
        return obj;
    }
}
