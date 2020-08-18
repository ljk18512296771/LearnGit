package com.jt.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;
//mvc提供的对外的拦截器接口.
@Component  //将对象交给容器管理
public class UserInterceptor implements HandlerInterceptor{
	
	@Autowired
	private JedisCluster jedisCluster;
	/**
	 * 目的: 如果用户不登录,则不允许访问权限相关业务.
	 * 返回值: 
	 * 		 true:表示放行 
	 * 		 false: 表示拦截  一般配置重定向使用.
	 * 注意事项:必须添加拦截器策略.
	 * 业务说明:
	 * 	 用户如果已经登录,则放行,反正拦截
	 *  
	 * 如何判断用户是否登录:
	 * 	1.判断客户端是否有指定的Cookie    true
	 * 	2.应该获取cookie中的值  去redis中校验是否存在.  true
	 *  如果上述条件都满足,则应该放行请求.
	 * 
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//业务实现  1.获取cookie
		Cookie[] cookies = request.getCookies();
		if(cookies !=null && cookies.length>0) {
			for (Cookie cookie : cookies) {
				if("JT_TICKET".equals(cookie.getName())){
					//如果equals则说明cookie是存在的.
					String ticket = cookie.getValue();
					//2.redis中是否有该记录  如果有记录则放行请求.
					if(jedisCluster.exists(ticket)) {
						
						//3.动态获取用户json信息
						String userJSON = jedisCluster.get(ticket);
						User user = ObjectMapperUtil.toObject(userJSON, User.class);
						//利用request对象传递用户信息.
						request.setAttribute("JT_USER", user);
						//说明数据以及存在.可以放行
						return true;
					}
				}
			}
		}
		
		//重定向到用户登录页面
		response.sendRedirect("/user/login.html");
		return false;
	}
}
