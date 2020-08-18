package com.jt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.service.DubboOrderService;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Reference(check = false)
	private DubboCartService cartService;
	@Reference(check = false)
	private DubboOrderService orderService;
	/**
	 * 跳转到订单确认页面 http://www.jt.com/order/create.html
	 * 业务逻辑: 根据userId,之后查询购物车记录信息.之后在页面中展现购物车数据.
	 * 页面取值: ${carts}
	 */
	@RequestMapping("/create")
	public String create(HttpServletRequest request,Model model) {
		
		User user = (User) request.getAttribute("JT_USER");
		long userId = user.getId();
		List<Cart> cartList = cartService.findCartListByUserId(userId);
		model.addAttribute("carts", cartList);
		return "order-cart";
	}
	
	
	/**
	 * 1.url地址:http://www.jt.com/order/submit
	 * 2.参数   form表单提交
	 * 3.返回值  SysResult对象  并且包含orderId数据
	 * @return
	 */
	@RequestMapping("/submit")
	@ResponseBody   
	public SysResult saveOrder(Order order,HttpServletRequest request) {
		User user = (User) request.getAttribute("JT_USER");
		Long userId = user.getId();
		order.setUserId(userId);	//将userId进行赋值操作.
		String orderId = orderService.saveOrder(order);
		if(StringUtils.isEmpty(orderId)) {
			//说明:后端服务器异常
			return SysResult.fail();
		}
		
		return SysResult.success(orderId);
	}
	
	//http://www.jt.com/order/success.html?id=111595833611692
	//获取order对象信息   ${order.orderId}
	@RequestMapping("success")
	public String findOrderById(String id,Model model) {
		
		Order order = orderService.findOrderById(id);
		model.addAttribute("order", order);
		return "success";
	}
	
	
	
	
	
	
}
