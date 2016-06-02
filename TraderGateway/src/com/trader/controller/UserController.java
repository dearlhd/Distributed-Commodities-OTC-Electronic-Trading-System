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

import com.trader.entity.User;
import com.trader.service.UserService;

@Controller
@RequestMapping("/User")
public class UserController {
	@Resource(name="userService")
    private UserService userService;
    
    @RequestMapping(value="/getUser/{userInfo}",method=RequestMethod.GET)
    public @ResponseBody JSONArray getUser(@PathVariable("userInfo") String userInfo){
    	JSONArray obj = null;
    	
    	userInfo = userInfo.replaceAll("\\s", "");
    	String[] str_split = userInfo.split("=");
    	if (str_split.length != 2){
    		return null;
    	}
    	else{
    		String username = str_split[1];
    		User user = new User();
    		user.setUsername(username);
    		obj = JSONArray.fromObject(userService.getUsersByUsername(user));
    		//System.out.println(username);
    	}    	
        return obj;
    }
    
    @RequestMapping(value = "/postUser", method = RequestMethod.POST)
    public @ResponseBody JSONObject postUser(@RequestBody JSONObject obj) {
    	User user = new User();
    	user.setUsername((String) obj.get("username"));
    	user.setPassword((String) obj.get("password"));
    	user.setAvailableBalance(0.0);
    	user.setFrozenCapital(0.0);
    	
    	obj = JSONObject.fromObject(userService.setUsers(user));
        return obj;
    }
}
