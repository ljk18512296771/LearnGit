package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.User;
import com.jt.service.UserService;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private JedisCluster jedisCluster;
	
	@RequestMapping("/getMsg")
	public String getMsg() {
		
		return "单点登录系统搭建完成!!!!";
	}
	
	/**
	 * 1.url:http://sso.jt.com/user/check/{param}/{type}
	 * 2.参数:param  需要校验的数据   
	 *       type   校验的类型
	 * 3.返回值结果:   SysResult对象     data:true/false
	 * 4.jsonp跨域访问
	 * 终极目标:  1.快  省  安全
	 * 代码优化:如果后端服务器报错,则如何控制
	 */
	@RequestMapping("/check/{param}/{type}")
	public JSONPObject checkUser(@PathVariable String param,@PathVariable Integer type,
			String callback) {
		
		boolean flag = userService.checkUser(param,type);//要求返回true/false
		SysResult sysResult = SysResult.success(flag);
		return new JSONPObject(callback, sysResult);
	}
	
	
	/**
	 * 利用jsonp获取用户信息
	 * url:http://sso.jt.com/user/query/826f4938c2a544edb7282139a35ddf56?callback=jsonp1595583195159&_=1595583195203
	 */
	@RequestMapping("/query/{ticket}")
	public JSONPObject findUserByTicket(String callback,@PathVariable String ticket) {
		
		if(jedisCluster.exists(ticket)) {
			//用户之前登录过  userJSON字符串
			String data = jedisCluster.get(ticket);
			User user = ObjectMapperUtil.toObject(data, User.class);
			System.out.println(data);
			SysResult sysResult = SysResult.success(data); //字符串当做参数返回
			//SysResult sysResult = SysResult.success(user); //对象当做参数返回
			return new JSONPObject(callback, sysResult);
		}else {
			//用户信息有误
			SysResult sysResult = SysResult.fail();
			return new JSONPObject(callback, sysResult);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}	
