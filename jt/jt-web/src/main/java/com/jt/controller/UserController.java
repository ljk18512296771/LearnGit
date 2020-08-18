package com.jt.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {

	//消费者启动时,不需要检查是否有服务的提供者
	@Reference(check = false) 
	private DubboUserService  userService;
	@Autowired
	private JedisCluster jedisCluster;


	/**
	 * 实现用户页面跳转
	 * http://www.jt.com/user/register.html    后端页面register.jsp
	 * http://www.jt.com/user/login.html	        后端页面login.jsp
	 * 重点:为了实现业务功能,拦截.html结尾的请求.
	 */
	@RequestMapping("/register")
	public String register() {

		//经过视图解析器,跳转指定的页面中
		return "register";
	}

	@RequestMapping("/login")
	public String login() {

		//经过视图解析器,跳转指定的页面中
		return "login";
	}


	/** 
	 * 完成用户注册
	 * 1.url地址：  http://www.jt.com/user/doRegister
	 * 2.参数：         用户名/密码/电话号码
	 * 3.返回值：    SysResult对象
	 */
	@RequestMapping("/doRegister")
	@ResponseBody
	public SysResult doRegister(User user) {

		userService.saveUser(user);
		return SysResult.success();
	}



	/**
	 * 15分钟联系    
	 * A. 我清楚的知道我要干什么 ,代码我会写  时间问题    少
	 * B. 我大概了解干什么,思路清楚,代码会写一部分   API不熟
	 * C. 图能看懂 ,只会写controller(部分-全部) service没有思路
	 * D. 图能看懂.但是代码不会写  没思路  不会API.  
	 * 业务需求:实现单点登录
	 * url地址:  /user/doLogin
	 * 参数:     {username:_username,password:_password}
	 * 返回值:    SysResult对象
	 * 
	 * 关于Cookie参数说明:
	 * Cookie特点: Cookie默认条件下,只能在自己的网址下进行展现, 京东的网站,看不到百度的cookie.
	 * Domain: 指定域名可以实现Cookie数据的共享.
	 * Path:(权限设定) 只有在特定的路径下,才能获取Cookie.
	 * 		  网址: www.jd.com/abc/findAll
	 * 		 cookie.setPath("/aa"); 只允许/aa的路径获取该Cookie信息 
	 * 		 cookie.setPath("/");  任意网址,都可以获取Cookie信息.
	 */
	@RequestMapping("/doLogin")
	@ResponseBody	//返回JSON
	public SysResult doLogin(User user,HttpServletResponse response) {

		//1.通过user传递用户名和密码,交给业务层service进行校验,获取ticket信息(校验之后的回执)
		String ticket = userService.doLogin(user);

		if(StringUtils.isEmpty(ticket)) {
			//证明用户名或密码错误.
			return SysResult.fail();
		}


		//2.准备Cookie实现数据存储.
		Cookie cookie = new Cookie("JT_TICKET", ticket);
		cookie.setDomain("jt.com");
		cookie.setPath("/");
		cookie.setMaxAge(7*24*60*60); //7天超时
		//将cookie保存到客户端中.
		response.addCookie(cookie);

		return SysResult.success();
	}


	/**
	 * 完成用户退出操作
	 * 1.url: http://www.jt.com/user/logout.html
	 * 2.没有传递参数
	 * 3.返回值: string  重定向到系统首页
	 * 业务实现思路:
	 * 	 0.先获取cookie中的数据 NAME=JT_TICKET
	 * 	 1.删除redis中的数据     key-value    key=cookie中的value   
	 * 	 2.删除cookie记录	   根据cookie名称  设置存活时间即可.
	 * 
	 * 注意事项: request对象中只能传递cookie的name和value.不能传递其他数据参数.
	 * 所以如果需要再次操作cookie则最好设定参数,否则可能导致操作失败
	 */
	@RequestMapping("/logout")
	public String  logout(HttpServletRequest request,HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if(cookies !=null && cookies.length >0) {
			for (Cookie cookie : cookies) {
				if("JT_TICKET".equalsIgnoreCase(cookie.getName())) {
					String ticket = cookie.getValue();
					//1.删除redis数据
					jedisCluster.del(ticket);
					//2.删除cookie   立即删除cookie 0  ,  暂时不删,关闭浏览器时删除 -1
					cookie.setDomain("jt.com");
					cookie.setPath("/");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
					break;
				}
			}
		}
		//重定向到系统首页
		return "redirect:/";
	}














}
