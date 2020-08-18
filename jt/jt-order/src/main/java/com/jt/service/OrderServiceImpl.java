package com.jt.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.OrderItemMapper;
import com.jt.mapper.OrderMapper;
import com.jt.mapper.OrderShippingMapper;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.OrderShipping;

@Service
public class OrderServiceImpl implements DubboOrderService {
	
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	
	
	@Transactional //控制事务.
	@Override
	public String saveOrder(Order order) {
		String orderId = ""+order.getUserId() + System.currentTimeMillis();
		Date date = new Date();
		
		//1.实现订单入库
		order.setOrderId(orderId)
			 .setStatus(1)   //未付款
			 .setCreated(date)
			 .setUpdated(date);
		orderMapper.insert(order);
		System.out.println("订单入库成功!!!");
		
		//2.订单物流入库
		OrderShipping orderShipping = order.getOrderShipping();
		orderShipping.setOrderId(orderId)
					 .setCreated(date)
					 .setUpdated(date);
		orderShippingMapper.insert(orderShipping);
		System.out.println("订单物流入库成功!!!!");
		
		//3.订单商品入库
		List<OrderItem> list = order.getOrderItems();
		for (OrderItem orderItem : list) {
			
			orderItem.setOrderId(orderId)
					 .setCreated(date)
					 .setUpdated(date);
			orderItemMapper.insert(orderItem);
		}
		System.out.println("订单商品入库成功!!!!");
		
		return orderId;
	}


	@Override
	public Order findOrderById(String orderId) {
		
		Order order = orderMapper.selectById(orderId);
		OrderShipping orderShipping = orderShippingMapper.selectById(orderId);
		QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("order_id", orderId);
		List<OrderItem> orderItems = orderItemMapper.selectList(queryWrapper);
		
		return order.setOrderShipping(orderShipping).setOrderItems(orderItems);
	}
	
	
	
	
	
	
	
}
