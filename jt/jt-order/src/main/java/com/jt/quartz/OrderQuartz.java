package com.jt.quartz;

import java.util.Calendar;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.OrderMapper;
import com.jt.pojo.Order;


//准备订单定时任务
@Component
public class OrderQuartz extends QuartzJobBean{

	@Autowired
	private OrderMapper orderMapper;
	
	/**
	 * 删除30分钟之后没有支付的订单,将状态由1改为6
	 * 业务实现:
	 *  如何判断超时:  create < now -30分钟
	 * 	1.sql update tb_order set status=6,updated=now() where status = 1 and 
	 */
	@Override
	@Transactional
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		
		//1.计算超时时间
		Calendar calendar = Calendar.getInstance(); //获取当前时间
		calendar.add(Calendar.MINUTE, -30);
		Date timeOut = calendar.getTime();			//获取时间
		//2.实现数据库更新
		orderMapper.updateStatus(timeOut);
		
		System.out.println("定时任务执行成!!!!!!"); 	//1分钟执行一次
	}

}
