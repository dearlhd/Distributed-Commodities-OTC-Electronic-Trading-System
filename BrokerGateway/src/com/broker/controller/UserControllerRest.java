package com.broker.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.broker.entity.User;


@Controller
@RequestMapping("/rest")
public class UserControllerRest {
	@RequestMapping(value = "/hello", produces = "text/plain;charset=UTF-8")
    public @ResponseBody
    String hello() {
        return "hello";
    }

    @RequestMapping(value = "/say/{msg}", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String say(@PathVariable(value = "msg") String msg) {
        return "{\"msg\":\"you say:'" + msg + "'\"}";
    }

    @RequestMapping(value = "/User/{id}", method = RequestMethod.GET)
    @ResponseBody
    public User getUser(@PathVariable("id") int id) {
        User User = new User();
        User.setUserName("luo");
        User.setAge(30);
        User.setId(id);
        return User;
    }

    @RequestMapping(value = "/User/{id:\\d+}", method = RequestMethod.DELETE)
    public @ResponseBody
    Object deleteUser(@PathVariable("id") int id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", "删除人员信息成功");
        return jsonObject;
    }
    
    @RequestMapping(value = "/greeting", method = RequestMethod.POST,consumes = "application/json")
    public @ResponseBody User greeting(@RequestBody User user) { 
        System.out.println(user.getUserName());
        return user;
    }

    @RequestMapping(value = "/User", method = RequestMethod.POST)
    @ResponseBody
    public Object addUser(@RequestBody String str) {
    	System.out.println(str);
//    	if (user == null) {
//    		System.out.println("null user");
//    	}
//    	System.out.println(user.getId());
//    	System.out.println(user.getPassword());
//    	System.out.println(user.getAge());
//    	System.out.println(user.getUserName());
    	
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", "注册人员信息成功");
        return jsonObject;
    }

    @RequestMapping(value = "/User", method = RequestMethod.PUT)
    public @ResponseBody
    Object updateUser(User User) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", "更新人员信息成功");
        return jsonObject;
    }

    @RequestMapping(value = "/User", method = RequestMethod.PATCH)
    public @ResponseBody
    List<User> listUser(@RequestParam(value = "name", required = false, defaultValue = "") String name) {

        List<User> lstUsers = new ArrayList<User>();

        User User = new User();
        User.setUserName("luo1");
        User.setAge(25);
        User.setId(101);
        lstUsers.add(User);

        User User2 = new User();
        User2.setUserName("luo2");
        User2.setAge(23);
        User2.setId(102);
        lstUsers.add(User2);

        User User3 = new User();
        User3.setUserName("luo3");
        User3.setAge(27);
        User3.setId(103);
        lstUsers.add(User3);

        return lstUsers;
    }
}
