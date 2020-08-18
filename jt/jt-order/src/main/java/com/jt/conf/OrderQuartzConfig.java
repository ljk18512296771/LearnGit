package com.jt.conf;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jt.quartz.OrderQuartz;


@Configuration
public class OrderQuartzConfig {
	
	//定义任务详情   JobDetail封装job任务.
	@Bean
	public JobDetail orderjobDetail() {
		//指定job的名称和持久化保存任务
		return JobBuilder
				.newJob(OrderQuartz.class)		//1.任务的类型
				.withIdentity("orderQuartz")	//2.任务名称
				.storeDurably()
				.build();
	}
	//springBoot会实现对象的自动装配  开箱即用的功能.
	//定义触发器  告知将来执行的任务是谁?
	@Bean
	public Trigger orderTrigger() {
		/*SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule()
				.withIntervalInMinutes(1)	//定义时间周期
				.repeatForever();*/
		//设定程序1分钟执行一次...
		CronScheduleBuilder scheduleBuilder 
			= CronScheduleBuilder.cronSchedule("0 0/1 * * * ?");
		return TriggerBuilder
				.newTrigger()
				.forJob(orderjobDetail())  //执行什么样的任务   *
				.withIdentity("orderQuartz") //任务名称             *
				.withSchedule(scheduleBuilder).build(); //什么时候执行
	}
}
